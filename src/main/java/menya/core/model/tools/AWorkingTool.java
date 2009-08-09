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

/**
 * @author dominik
 * 
 */
public abstract class AWorkingTool implements IWorkingTool {

	private Set<IProcessor> processors = new TreeSet<IProcessor>();

	private float maxWidth;

	/**
	 * 
	 */
	public AWorkingTool() {
		setupInternal();
	}

	/**
	 * you should setup this pens processors and properties by implementing this
	 * method.
	 */
	protected abstract void setup();

	private void setupInternal() {
		loadDefaultFields();
		loadDefaultProperties();
		setup();
	}

	/**
	 * 
	 */
	private void loadDefaultFields() {
		maxWidth = 5.0f;
		// TODO add more fields
	}

	private void loadDefaultProperties() {
		// TODO
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.IWorkingTool#getProcessors()
	 */
	@Override
	public final Set<IProcessor> getProcessors() {
		return Collections.unmodifiableSet(processors);
	}

	/**
	 * is a implementation of the defined method that applies a identity
	 * function to the pressure measured.
	 * 
	 * @see menya.core.model.IWorkingTool#applyPressureFunction(float)
	 */
	@Override
	public float applyPressureFunction(float levelValue) {
		return levelValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.model.IWorkingTool#getMaxWidth()
	 */
	@Override
	public float getMaxWidth() {
		return maxWidth;
	}

	protected void setMaxWidth(float width) {
		this.maxWidth = width;
	}
}
