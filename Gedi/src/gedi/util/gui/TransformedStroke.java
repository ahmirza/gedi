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

package gedi.util.gui;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * Use this if you don't want your {@link AffineTransform} to resize the line width!
 * @author flo
 *
 */
public class TransformedStroke implements Stroke
{
	private AffineTransform transform;
	private AffineTransform inverse;
	private Stroke stroke;


	public TransformedStroke(Stroke base, AffineTransform at)
	{
		this.transform = new AffineTransform(at);
		try {
			this.inverse = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException(e);
		}
		this.stroke = base;
	}


	public Shape createStrokedShape(Shape s) {
		Shape sTrans = transform.createTransformedShape(s);
		Shape sTransStroked = stroke.createStrokedShape(sTrans);
		Shape sStroked = inverse.createTransformedShape(sTransStroked);
		return sStroked;
	}

}

