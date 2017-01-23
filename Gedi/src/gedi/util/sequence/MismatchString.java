package gedi.util.sequence;

import gedi.util.ArrayUtils;
import gedi.util.SequenceUtils;
import gedi.util.StringUtils;
import gedi.util.sequence.MismatchString.MismatchStringPart;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MismatchString extends AbstractList<MismatchStringPart> {

	static Pattern p1 = Pattern.compile("^([0-9]+)");
	static Pattern p2 = Pattern.compile("^(?:([ACGTN]|\\^[ACGTN]*)([0-9]+))");
	
	private MismatchStringPart[] components;
	private int length;
	
	private MismatchString() {}
	
	public MismatchString(String mmstring) {
		String ori = mmstring;
		
		mmstring = mmstring.toUpperCase();
		
		// correct errorneous mismatch strings without leading or trailing 0
		if (!StringUtils.isInt(mmstring.substring(0, 1)))
			mmstring = 0+mmstring;
		if (!StringUtils.isInt(mmstring.substring(mmstring.length()-1)))
			mmstring = mmstring+0;
		
		
		Matcher m = p1.matcher(mmstring);
		ArrayList<MismatchStringPart> c = new ArrayList<MismatchStringPart>();
		
		while (m.find()) {
			for (int i=0; i<m.groupCount(); i++) {
				String g = m.group(i+1);
				if (c.size()%2==0)
					c.add(new StretchOfMatches(Integer.parseInt(g)));
				else if (g.startsWith("^")) {
					c.add(new Deletion(g.substring(1)));
					length++;
				}
				else
					c.add(new Mismatch(g.charAt(0)));
				length+=c.get(c.size()-1).getGenomicLength();
			}
			
			mmstring = mmstring.substring(m.end());
			m = p2.matcher(mmstring);
		}
		if (mmstring.length()>0)
			throw new IllegalArgumentException("Cannot parse rest as mismatch string: "+ori);
		
		components = c.toArray(new MismatchStringPart[0]);
	}
	
	public String reconstitute(String reference) {
		if (reference==null) return null;
		
		StringBuilder sb = new StringBuilder();
		int p = 0;
		for (MismatchStringPart c : components) {
			int l = c.getGenomicLength();
			if (c instanceof Mismatch) {
				sb.append(((Mismatch)c).genomicChar);
				p+=l;
			}
			else if (c instanceof StretchOfMatches) {
				sb.append(reference.substring(p,p+l));
				p+=l;
			}
			else
				sb.append(((Deletion)c).genomicChars);
			
		}
		return sb.toString();
	}
	
	public MismatchString reverseComplement() {
		MismatchStringPart[] ncomponents = components.clone();
		for (int i=0; i<ncomponents.length; i++) 
			ncomponents[i] = ncomponents[i].complement();
		ArrayUtils.reverse(ncomponents);
		MismatchString re = new MismatchString();
		re.components = ncomponents;
		re.length = length;
		return re;
	}

	
	public MismatchString changeReference(String reference) {
		int p=0;
		MismatchStringPart[] ncomponents = components.clone();
		
		for (int i=0; i<ncomponents.length; i++) {
			int l = ncomponents[i].getGenomicLength();
			if (ncomponents[i] instanceof Mismatch) {
				ncomponents[i] = new Mismatch(reference.charAt(p));
			}
			p+=l;
		}
		MismatchString re = new MismatchString();
		re.components = ncomponents;
		re.length = length;
		return re;
	}
	
//	public MismatchString reverse() {
//		MismatchStringPart[] revcomponents = components.clone();
//		ArrayUtils.reverse(revcomponents);
//		for (int i=0; i<revcomponents.length; i++)
//			if (revcomponents[i] instanceof Deletion)
//				revcomponents[i] = new Deletion(StringUtils.reverse(((Deletion)revcomponents[i]).genomicChars).toString());
//		MismatchString re = new MismatchString();
//		re.components = revcomponents;
//		re.length = length;
//		return re;
//	}
	
	public int getGenomicLength() {
		return length;
	}
	
	public int getNumMismatches() {
		int re = 0;
		for (MismatchStringPart s : components)
			if (s instanceof Mismatch)
				re++;
		return re;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (MismatchStringPart s : components)
			sb.append(s.toString());
		return sb.toString();
	}
	

	public static interface MismatchStringPart {
		int getGenomicLength();

		MismatchStringPart complement();
	}
	
	public static class StretchOfMatches implements MismatchStringPart {

		private int length;
		
		public StretchOfMatches(int length) {
			this.length = length;
		}

		public int getGenomicLength() {
			return length;
		}
		
		@Override
		public String toString() {
			return length+"";
		}

		@Override
		public MismatchStringPart complement() {
			return new StretchOfMatches(length);
		}
		
	}
	
	public static class Mismatch implements MismatchStringPart {

		private char genomicChar;
		
		public Mismatch(char genomicChar) {
			this.genomicChar = genomicChar;
		}

		public int getGenomicLength() {
			return 1;
		}
		
		public char getGenomicChar() {
			return genomicChar;
		}
		
		@Override
		public String toString() {
			return genomicChar+"";
		}
		@Override
		public MismatchStringPart complement() {
			return new Mismatch(SequenceUtils.getDnaComplement(genomicChar));
		}
		
	}
	
	public static class Deletion implements MismatchStringPart {

		private String genomicChars;
		
		public Deletion(String genomicChars) {
			this.genomicChars = genomicChars;
		}

		public int getGenomicLength() {
			return genomicChars.length();
		}
		
		public String getGenomicChars() {
			return genomicChars;
		}
		
		@Override
		public String toString() {
			return "^"+genomicChars;
		}
		@Override
		public MismatchStringPart complement() {
			return new Deletion(SequenceUtils.getDnaComplement(genomicChars));
		}
	}

	/**
	 * Mismatch letters are characters from s!
	 * @param reference
	 * @param s
	 * @return
	 */
	public static MismatchString infer(String reference, String s) {
		if (reference.length()!=s.length()) throw new IllegalArgumentException("Cannot infer mismatch string for sequences of differing lengths!");
		ArrayList<MismatchStringPart> parts = new ArrayList<MismatchStringPart>();
		
		int m = 0;
		int i=0; 
		for (; i<s.length() && s.charAt(i)==reference.charAt(i); i++)
			m++;
		parts.add(new StretchOfMatches(m));
		
		while (i<s.length()) {
			parts.add(new Mismatch(s.charAt(i)));
			m = 0;
			for (i++; i<s.length() && s.charAt(i)==reference.charAt(i); i++)
				m++;
			parts.add(new StretchOfMatches(m));
		}
		
		MismatchString re = new MismatchString();
		re.components = parts.toArray(new MismatchStringPart[0]);
		re.length = s.length();
		return re;
	}

	@Override
	public MismatchStringPart get(int index) {
		return components[index];
	}

	@Override
	public int size() {
		return components.length;
	}


	
}
