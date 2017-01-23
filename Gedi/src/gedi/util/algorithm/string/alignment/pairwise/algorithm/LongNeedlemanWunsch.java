package gedi.util.algorithm.string.alignment.pairwise.algorithm;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import gedi.util.algorithm.string.alignment.pairwise.Alignment;
import gedi.util.algorithm.string.alignment.pairwise.AlignmentMode;
import gedi.util.algorithm.string.alignment.pairwise.scoring.LongScoring;



public class LongNeedlemanWunsch {

	
	private long[][] A;
	
	private long[] a;
	
	private int max_i;
	private int max_j;

	public LongNeedlemanWunsch() {
		this((1<<8)-1);
	}
	
	public LongNeedlemanWunsch(int maxLength) {
		A = new long[(maxLength+1)][(maxLength+1)];
		a = new long[maxLength+1];
	}
	
	public void freeSpace() {
		A = null;
		a = null;
	}

	private void ensureSize(int n, int m) {
		if (A==null || A.length<n || A[0].length<m) {
			int nn = 1, nm = 1;
			for (;nn<n; nn<<=1);
			for (;nm<m; nm<<=1);
			A = new long[nn][nm];
		}
	}
	
	private void ensureSize(int m) {
		if (a==null || a.length<m) {
			int nm = 1;
			for (;nm<m; nm<<=1);
			a = new long[nm];
		}
	}

	
	public long align(final LongScoring<?> scoring, final int n, final int m, long gap, final AlignmentMode mode) {

		final long INF = Long.MIN_VALUE-gap;
		
		ensureSize(m+1);
		
		final boolean initZeroFirst = mode!=AlignmentMode.Global && mode!=AlignmentMode.PrefixSuffix;
		final boolean initZeroSecond = mode!=AlignmentMode.Global;

		long max = INF;
		int max_i = 0;
		int max_j = 0;

		final long[] A = this.a;
		long d,a,as;

		A[0] = 0;

		A[1] = initZeroSecond?0:gap;

		for (int j=2; j<=m; j++) 
			A[j] = initZeroSecond?0:A[j-1]+gap;

		for (int i=1; i<=n; i++) {
			d = INF;
			a = A[0];
			A[0] = initZeroFirst?0:A[0]+gap;
			
			if (mode==AlignmentMode.Local)
				for (int j=1; j<=m; j++) {
					d = A[j-1]+gap;
					as = A[j];
					A[j] = Math.max(Math.max(0,a+scoring.getLong(i-1,j-1)), Math.max(as+gap, d));
					a = as;
					if (A[j]>max) {
						max = A[j];
						max_i = i; 
						max_j = j;
					}
				}
			else if (mode==AlignmentMode.Global) {
				for (int j=1; j<=m; j++) {
					d = A[j-1]+gap;
					as = A[j];
					A[j] = Math.max(a+scoring.getLong(i-1,j-1), Math.max(as+gap, d));
					a = as;
				}
			}
			else if (mode==AlignmentMode.Freeshift)
				for (int j=1; j<=m; j++) {
					d = A[j-1]+gap;
					as = A[j];
					A[j] = Math.max(a+scoring.getLong(i-1,j-1), Math.max(as+gap, d));
					a = as;
					if ((j==m || i==n) && A[j]>max) {
						max = A[j];
						max_i = i; 
						max_j = j;
					}
				}
			else
				for (int j=1; j<=m; j++) {
					d = A[j-1]+gap;
					as = A[j];
					A[j] = Math.max(a+scoring.getLong(i-1,j-1), Math.max(as+gap, d));
					a = as;
					if (j==m && A[j]>max) {
						max = A[j];
						max_i = i; 
						max_j = j;
					}
				}
		}
		
		if (mode==AlignmentMode.Global) {
			max = A[m];
			max_i = n; 
			max_j = m;
		}

		this.max_i = max_i;
		this.max_j = max_j;

		return max;
	}
	
	
	public long align(final LongScoring<?> scoring, final int n, final int m, long gap, final AlignmentMode mode, final Alignment alignment) {

		final long INF = Long.MIN_VALUE-gap;
		
		ensureSize(n+1, m+1);
		
		final boolean initZeroFirst = mode!=AlignmentMode.Global && mode!=AlignmentMode.PrefixSuffix;
		final boolean initZeroSecond = mode!=AlignmentMode.Global;

		long max = INF;
		int max_i = 0;
		int max_j = 0;

		final long[][] A = this.A;

		A[0][0] = 0;
		A[0][1] = initZeroSecond?0:gap;
		A[1][0] = initZeroFirst?0:gap;

		for (int j=2; j<=m; j++) 
			A[0][j] = initZeroSecond?0:A[0][j-1]+gap;

		for (int i=1; i<=n; i++) {

			A[i][0] = initZeroFirst?0:A[i-1][0]+gap;
			
			if (mode==AlignmentMode.Local)
				for (int j=1; j<=m; j++) {
					A[i][j] = Math.max(Math.max(0,A[i-1][j-1]+scoring.getLong(i-1,j-1)), Math.max(A[i-1][j], A[i][j-1])+gap);
					if (A[i][j]>max) {
						max = A[i][j];
						max_i = i; 
						max_j = j;
					}
				}
			else if (mode==AlignmentMode.Global) {
				for (int j=1; j<=m; j++) {
					A[i][j] = Math.max(A[i-1][j-1]+scoring.getLong(i-1,j-1), Math.max(A[i-1][j], A[i][j-1])+gap);
				}
			}
			else if (mode==AlignmentMode.Freeshift)
				for (int j=1; j<=m; j++) {
					A[i][j] = Math.max(A[i-1][j-1]+scoring.getLong(i-1,j-1), Math.max(A[i-1][j], A[i][j-1])+gap);
					if ((j==m || i==n) && A[i][j]>max) {
						max = A[i][j];
						max_i = i; 
						max_j = j;
					}
				}
			else
				for (int j=1; j<=m; j++) {
					A[i][j] = Math.max(A[i-1][j-1]+scoring.getLong(i-1,j-1), Math.max(A[i-1][j], A[i][j-1])+gap);
					if (j==m && A[i][j]>max) {
						max = A[i][j];
						max_i = i; 
						max_j = j;
					}
				}
		}
		
		if (mode==AlignmentMode.Global) {
			max = A[n][m];
			max_i = n; 
			max_j = m;
		}

		this.max_i = max_i;
		this.max_j = max_j;

		if (alignment==null)
			return max;
		
//		return max;
//	}
//
//	public void backtrack(Alignment alignment, LongScoring scoring, long gapOpen, long gapExtend, AlignmentMode mode) {

		alignment.clear();
		
		int i = this.max_i;
		int j = this.max_j;

//		long gapFirst = gapOpen+gapExtend;

//		long[][] A = this.A;
//		long[][] I = this.I;
//		long[][] D = this.D;
		

		do {
			long s = A[i][j];
			if (s == A[i-1][j-1]+scoring.getLong(i-1,j-1)) {
				alignment.add(i-1,j-1);
				i--;
				j--;
			} else if (s == A[i-1][j]) {
				i--;
			} else {
				j--;
			}

		} while (i!=0 && j!=0 && (mode!=AlignmentMode.Local || A[i][j]!=0));
		
		return max;
	}


	public void printMatrices(char[] s1, char[] s2, Writer out) throws IOException {
		out.write("A\n");
		printMatrix(A,s1,s2,out);
	}
	
	private void printMatrix(long[][] mat, char[] s1, char[] s2, Writer out) throws IOException {
		
		out.write("\t");
		for (int j=0; j<s2.length; j++) {
			out.write("\t");
			out.write(s2[j]);
		}
		out.write("\n");
		
		for (int i=0; i<=s1.length; i++) {
			if (i>0)
				out.write(s1[i-1]);
			for (int j=0; j<=s2.length; j++) {
				out.write("\t");
				out.write(String.format(Locale.US,"%d",mat[i][j]));
			}
			out.write("\n");
		}
	}



	

}
