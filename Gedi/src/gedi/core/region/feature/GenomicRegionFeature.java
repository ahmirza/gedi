package gedi.core.region.feature;

import gedi.core.reference.ReferenceSequence;
import gedi.core.region.GenomicRegion;
import gedi.util.ReflectionUtils;
import gedi.util.StringUtils;
import gedi.util.datastructure.charsequence.MaskedCharSequence;
import gedi.util.functions.EI;
import gedi.util.mutable.MutableMonad;
import gedi.util.nashorn.JSFunction;
import gedi.util.nashorn.JSPredicate;
import gedi.util.orm.Orm;
import gedi.util.userInteraction.results.ResultProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.script.ScriptException;

public interface GenomicRegionFeature<O> extends Consumer<Set<O>> {

	default boolean dependsOnData() {
		return false;
	}
	
	
	/**
	 * Merge the given features and produce a result; is called with null if not run in multithread mode
	 * @param o
	 */
	default void produceResults(GenomicRegionFeature<O>[] o) {
	}
	GenomicRegionFeature<O> copy();
	
	GenomicRegionFeatureProgram getProgram();
	void setProgram(GenomicRegionFeatureProgram program);
	void setId(String id);
	String getId();
	int getMinValues();
	int getMaxValues();
	
	default Iterator<? extends O> getUniverse() {
		return null;
	}
	
	
	void setInputNames(String[] inputs);
	int getInputLength();
	String getInputName(int index);
	<I> Set<I> getInput(int i);
	<I> Set<I> getInput(String name);
	default <I> I getInput(String name, int index) {
		return (I) EI.wrap(getInput(name).iterator()).skip(index).next();
	}
	default <I> I getInput(int i, int index) {
		return (I) EI.wrap(getInput(i).iterator()).skip(index).next();
	}
	
	default <I> I getUniqueInput(int i, I notUnique) {
		Set<Object> in = getInput(i);
		if (in.size()==1) return (I) in.iterator().next();
		return notUnique;
	}
	
	default <I> I getUniqueInput(String name, I notUnique) {
		Set<Object> in = getInput(name);
		if (in.size()==1) return (I) in.iterator().next();
		return notUnique;
	}
	
	default boolean isUniqueInput(int i) {
		Set<Object> in = getInput(i);
		return (in.size()==1);
	}
	
	default boolean isUniqueInput(String name) {
		Set<Object> in = getInput(name);
		return (in.size()==1);
	}
	
	
	void setData(Object data);
	
	default void addCondition(String[] inputs, String cond) throws ScriptException {
		Predicate<Set<String>>[] setCond = parseSetConditions(cond);
		addCondition(f->{
			boolean allmatch = true;
			for (int j=0; j<setCond.length; j++) 
				if (!setCond[j].test(f.getProgram().getInputById(inputs[j]))) {
					allmatch = false;
					break;
				}
			return allmatch;
		});
	};
	
	default void addCondition(String js) throws ScriptException {
		StringBuilder code = new StringBuilder();
		code.append("function() {\n");
		if (js.contains(";")) {
			code.append(js);
			code.append("}");
		} else {
			code.append("return "+js+";\n}");
		}
		
		addCondition(new JSPredicate(true, code.toString()));
	};
	
	void addCondition(Predicate<GenomicRegionFeature<O>> condition);
	boolean hasCondition();
	
	
	
	boolean setGenomicRegion(ReferenceSequence reference, GenomicRegion region);
	<I> void setInput(int index, Set<I> input);
	
	void begin();
	void end();
	
	
	public static Predicate<Set<String>>[] parseSetConditions(String cond) {
		String[] f = MaskedCharSequence.maskQuotes(cond, ' ').splitAndUnmask(',');//StringUtils.splitAtUnquoted(cond, ',');
		Predicate<Set<String>>[] re = new Predicate[f.length];
		for (int i=0; i<f.length; i++) {
			re[i] = parseSetCondition(f[i]);
		}
		return re;
	}
	
	
	public static Predicate<Set<String>> parseSetCondition(String cond) {
		if (!cond.startsWith("[") || !cond.endsWith("]")) throw new RuntimeException("Entries must be enclosed in []");
		String ff = cond.substring(1, cond.length()-1).trim();
		
		Predicate<Set<String>> re = null;
		
		switch (ff) {
		case "": re = s->s.isEmpty(); break;
		case "U": re = s->s.size()==1; break;
		case "N": re = s->s.size()>1; break;
		case "?": re = s->s.size()<=1; break;
		case "+": re = s->s.size()>=1; break;
		case "*": re = s->true; break;
		default:
			HashSet<String> ts = new HashSet<String>();
			HashSet<Integer> is = new HashSet<Integer>();
			for (String e : MaskedCharSequence.maskQuotes(ff, ' ').splitAndUnmask(';')) {//StringUtils.splitAtUnquoted(ff,';')) {
				if (StringUtils.isInt(e))
					is.add(Integer.parseInt(e));
				else if (!e.startsWith("'") || !e.endsWith("'")) 
					throw new RuntimeException("Entries must be quoted!");
				else 
					ts.add(e.substring(1, e.length()-1));
			}
			if (ts.size()>0 && is.size()>0)
				throw new RuntimeException("You cannot mix Strings and Integers in the entries!");
			else if (ts.size()>0)
				re = s->s.equals(ts);
			else if (is.size()>0)
				re = s->s.equals(is);
			else
				throw new RuntimeException("No entries!");
					
			break;
		}
		
		return re;
	}
	
	default void addPredicate(String js) throws ScriptException {
		StringBuilder code = new StringBuilder();
		code.append("function(f) {\n");
		if (js.contains(";")) {
			code.append(js);
			code.append("}");
		} else {
			code.append("return "+js+";\n}");
		}
		
		addPredicate(new JSPredicate(true, code.toString()));
	}
	default void addFunction(String js) throws ScriptException {
		StringBuilder code = new StringBuilder();
		code.append("function(f) {\n");
		if (js.contains(";")) {
			code.append(js);
			code.append("}");
		} else {
			code.append("return "+js+";\n}");
		}
		
		addFunction(new JSFunction(true, code.toString()));
	}
	
	default void addField(String field) throws ScriptException {
		MutableMonad<Function> mut = new MutableMonad<Function>();
		addFunction(o->{
			if (mut.Item==null) mut.Item = Orm.getFieldGetter(o.getClass(), field);
			return mut.Item.apply(o);
		});
	}
	
	default void addMethod(String method, String... params){
		MutableMonad<BiFunction<Object,String[],Object>> mut = new MutableMonad<BiFunction<Object,String[],Object>>();
		addFunction(o->{
			if (mut.Item==null){
				mut.Item = ReflectionUtils.findMethod(o, method, params);
				if (mut.Item==null)
					throw new IllegalArgumentException("Method "+method+" not found for "+o.getClass().getName()+" for parameters "+Arrays.toString(params));
			}
			
			return mut.Item.apply(o,params);
		});
	}
	
	default void addConstant(String c) throws ScriptException {
		addFunction(o->c);
	}
	
	
	default void addPredicate(Predicate<O> predicate) {
		addFunction((o)->predicate.test(o)?o:null);
	}
	
	/**
	 * Postprocessing of annotation set: The function can transform each annotation (e.g. using all inputs of this feature)
	 * to another annotation or remove it (return null).
	 * @param function
	 */
	<T> void addFunction(Function<O,T> function);
	
	void applyCommands(Set o);
	default void addResultProducers(ArrayList<ResultProducer> re) {};
	
	
	
}