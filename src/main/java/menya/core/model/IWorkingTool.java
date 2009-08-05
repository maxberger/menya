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
package menya.core.model;

import java.util.Set;

/**
 * is a general tool that has certain properties like a drawing color, the
 * associated physical device and processors. A pen is more a general kind of
 * working tool.
 * 
 * @author dominik
 * 
 */
public interface IWorkingTool {

	/**
	 * retrieve all properties of this working tool.
	 * 
	 * @return a set of properties
	 */
	public Set<IWorkingToolProperty<?>> getProperties();

	/**
	 * retrieves all processors of this working tool.
	 * 
	 * @see IProcessor
	 * @return
	 */
	public Set<IProcessor> getProcessors();
}
