/*
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.core.document;

import menya.core.model.Curve;

/**
 * This interface marks editable layers and adds methods that take care of that
 * functionality.
 * 
 * @author dominik
 * 
 */
public interface IEditableLayer extends ILayer {

	/**
	 * Adds a new curve to the layer.
	 * 
	 * @param curve
	 *            the curve to add.
	 */
	public void addCurve(final Curve curve);
}
