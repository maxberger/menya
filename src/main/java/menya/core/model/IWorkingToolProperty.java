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

/**
 * a general working tool property can be for example a color.
 * 
 * @author dominik
 * 
 * @param <T>
 *            the generic type value that this property holds
 */
public interface IWorkingToolProperty<T> extends
		Comparable<IWorkingToolProperty<?>> {

	/**
	 * retrieves the name of this property. Please note that each pen can hold
	 * only one property with this name at the same time.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * retrieves the value of this property.
	 * 
	 * @return
	 */
	public T getValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();
}
