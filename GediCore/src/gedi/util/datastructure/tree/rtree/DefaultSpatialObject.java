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
package gedi.util.datastructure.tree.rtree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DefaultSpatialObject implements SpatialObject {

	private Rectangle2D bounds;
	
	
	public DefaultSpatialObject(Point2D point) {
		this.bounds = new Rectangle2D.Double(point.getX(), point.getY(),0,0);
	}
	
	public DefaultSpatialObject(Rectangle2D bounds) {
		this.bounds = bounds;
	}


	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}

}
