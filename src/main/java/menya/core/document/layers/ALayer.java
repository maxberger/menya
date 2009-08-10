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

/* $Id$ */

package menya.core.document.layers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import menya.core.document.IEditableLayer;
import menya.core.document.ILayer;
import menya.core.model.IWorkingTool;

/**
 * is the abstract super class of all layers.
 * 
 * @author Dominik
 * @version $Revision$
 */
public abstract class ALayer implements ILayer {

	private List<IWorkingTool> pens = new LinkedList<IWorkingTool>();
	private IWorkingTool activePen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.document.ILayer#castEditableLayer()
	 */
	@Override
	public IEditableLayer castEditableLayer() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<IWorkingTool> getAllPens() {
		return Collections.unmodifiableList(pens);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addPen(IWorkingTool pen) {
		pens.add(pen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void removePen(IWorkingTool pen) {
		pens.remove(pen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isEditable() {
		return getAllPens().size() > 0 && castEditableLayer() != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IWorkingTool getDefaultPen() {
		return getAllPens().get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IWorkingTool getActivePen() {
		if (activePen != null) {
			return activePen;
		} else {
			return getDefaultPen();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setActivePen(IWorkingTool pen)
			throws IllegalArgumentException {
		if (getAllPens().contains(pen)) {
			activePen = pen;
		} else {
			throw new IllegalArgumentException("The specified pen " + pen
					+ " is not part of this layer.");
		}
	}
}
