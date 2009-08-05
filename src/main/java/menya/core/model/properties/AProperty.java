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
package menya.core.model.properties;

import menya.core.model.IWorkingToolProperty;

/**
 * @author dominik
 * 
 */
public abstract class AProperty<T> implements IWorkingToolProperty<T> {

	private String name;

	private T value;

	public final void setName(String name) {
		this.name = name;
	}

	public final void setValue(T value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.WorkingToolProperty#getName()
	 */
	@Override
	public final String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.WorkingToolProperty#getValue()
	 */
	@Override
	public final T getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(IWorkingToolProperty<?> o) {
		return getName().compareTo(o.getName());
	}
}
