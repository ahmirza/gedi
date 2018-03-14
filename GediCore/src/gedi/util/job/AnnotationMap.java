/**
 * 
 *    Copyright 2017 Florian Erhard
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */
package gedi.util.job;


import java.util.HashMap;
import java.util.function.Function;

public class AnnotationMap<F,T> extends HashMap<F,T> { 

	private String name;
	private Class<T> cls;
	
	public AnnotationMap(String name, Class<T> cls) {
		this.name = name;
		this.cls = cls;
	}

	
	public String getName() {
		return name;
	}
	
	public Class<T> getAnnotationClass() {
		return cls;
	}
	
	public AnnotationMap<F,T> clone(Function<F,F> keyMap,Function<T,T> valueMap) {
		AnnotationMap<F, T> re = new AnnotationMap<F, T>(name, cls);
		for (F k : keySet()) {
			F nk = keyMap.apply(k);
			if (nk!=null)
				re.put(nk,valueMap.apply(get(k)));
		}
		return re;
	}
	
}
