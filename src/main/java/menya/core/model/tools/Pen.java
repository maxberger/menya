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
package menya.core.model.tools;

import java.awt.Color;

import menya.core.model.properties.ColorProperty;
import menya.core.model.properties.PropertyConstants;

/**
 * @author dominik
 * 
 */
public class Pen extends AWorkingTool {

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.tools.AWorkingTool#setup()
	 */
	@Override
	protected void setup() {
		// this general pen has no processors

		// set property "default color"
		addProperty(new ColorProperty(PropertyConstants.DEFAULT_COLOR,
				Color.BLACK));
	}

}
