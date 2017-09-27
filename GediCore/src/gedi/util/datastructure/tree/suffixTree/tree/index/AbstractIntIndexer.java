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

package gedi.util.datastructure.tree.suffixTree.tree.index;

public abstract class AbstractIntIndexer extends AbstractIndexer<int[]> implements IntIndexer {

	@Override
	protected int[] createEmpty(int nodes) {
		return new int[nodes];
	}

//	protected abstract void createNew_internal(SuffixTree tree, int[] index);
//
//	@Override
//	public int[] createNew(SuffixTree tree) {
//		int[] re = new int[tree.getStorage().getMaxNode()+1];
//		createNew_internal(tree, re);
//		tree.setNodeAttribute(name(), re);
//		return re;
//	}
//	
//	@Override
//	public int[] get(SuffixTree tree) {
//		int[] re = tree.getIntAttributes(name());
//		if (re==null)
//			return createNew(tree);
//		else
//			return re;
//	}
//
//	@Override
//	public boolean has(SuffixTree tree) {
//		return tree.getIntAttributes(name())!=null;
//	}
//	
//	@Override
//	public int hashCode() {
//		return name().hashCode();
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		return obj instanceof IntIndexer && ((IntIndexer)obj).name().equals(name());
//	}


}
