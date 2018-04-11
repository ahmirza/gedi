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

import java.util.ArrayList;

public class Place {

	private Class<?> cls;
	private int id;
	private ArrayList<Transition> consumers = new ArrayList<Transition>();
	private Transition producer;
	
	
	Place(Class<?> cls, int id) {
		this.cls = cls;
		this.id = id;
	}
	
	Place addConsumer(Transition t) {
		consumers.add(t);
		return this;
	}
	
	Place setProducer(Transition t) {
		if (producer!=null) throw new RuntimeException("Only one producer allowed!");
		this.producer = t;
		return this;
	}
	
	public Class<?> getTokenClass() {
		return cls;
	}
	
	@Override
	public String toString() {
		return "{"+id+":"+cls.getSimpleName()+"}";
	}
	
	public boolean isSource() {
		return producer==null;
	}
	
	public boolean isSink() {
		return consumers.isEmpty();
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<Transition> getConsumers() {
		return consumers;
	}
	
	public Transition getProducer() {
		return producer;
	}
	
}
