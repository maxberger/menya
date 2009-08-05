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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import menya.core.model.IProcessor;
import menya.core.model.IWorkingTool;
import menya.core.model.IWorkingToolProperty;

/**
 * @author dominik
 * 
 */
public abstract class AWorkingTool implements IWorkingTool {

	private Set<IProcessor> processors = new TreeSet<IProcessor>();

	private Set<IWorkingToolProperty<?>> properties = new TreeSet<IWorkingToolProperty<?>>();

	/**
	 * you should setup this pens processors and properties by implementing this
	 * method.
	 */
	protected abstract void setup();

	/**
	 * replaces all prior added processors with the provided processors.
	 * 
	 * @param processors
	 */
	protected final void setProcessors(Collection<IProcessor> processors) {
		this.processors.clear();
		this.processors.addAll(processors);
	}

	/**
	 * adds a processor.
	 * 
	 * @param p
	 */
	protected final void addProcessor(IProcessor p) {
		this.processors.add(p);
	}

	/**
	 * replaces all prior added properties with the provided properties.
	 * 
	 * @param properties
	 */
	protected final void setProperties(
			Collection<IWorkingToolProperty<?>> properties) {
		this.properties.clear();
		this.properties.addAll(properties);
	}

	/**
	 * adds a property.
	 * 
	 * @param p
	 */
	protected final void addProperty(IWorkingToolProperty<?> p) {
		this.properties.add(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.IWorkingTool#getProcessors()
	 */
	@Override
	public final Set<IProcessor> getProcessors() {
		return Collections.unmodifiableSet(processors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.IWorkingTool#getProperties()
	 */
	@Override
	public final Set<IWorkingToolProperty<?>> getProperties() {
		return Collections.unmodifiableSet(properties);
	}

	public final boolean hasProperty(IWorkingToolProperty<?> p) {
		return properties.contains(p);
	}

}
