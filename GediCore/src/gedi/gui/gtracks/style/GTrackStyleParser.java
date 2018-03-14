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
package gedi.gui.gtracks.style;

import java.util.function.Function;

import gedi.util.PaintUtils;
import gedi.util.dynamic.DynamicObject;

public interface GTrackStyleParser extends Function<DynamicObject,Object> {
	
	
	public static class GTrackNameStyleParser implements GTrackStyleParser {
		@Override
		public Object apply(DynamicObject t) {
			return t.asString();
		}
	}

	
	public static class GTrackColorStyleParser implements GTrackStyleParser {
		@Override
		public Object apply(DynamicObject t) {
			return PaintUtils.parseColor(t.asString());
		}
	}

	public static class GTrackFillStyleParser implements GTrackStyleParser {
		@Override
		public Object apply(DynamicObject t) {
			return PaintUtils.parseColor(t.asString());
		}
	}
	
	public static class GTrackSizeStyleParser implements GTrackStyleParser {
		@Override
		public Object apply(DynamicObject t) {
			return t.asDouble();
		}
	}

	
}