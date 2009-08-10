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

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import menya.core.Constants;
import menya.core.document.ILayer;
import menya.core.document.IPage;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision$
 */
public class Page implements IPage {

	private static final Logger LOGGER = Logger
			.getLogger(Page.class.toString());

	/**
     * 
     */
	private static final double A4_HEIGHT = 29.7;

	/**
     * 
     */
	private static final double A4_WIDTH = 21.0;

	private final ArrayDeque<ILayer> layers;

	// 210 mm x 297 mm is A4
	private Dimension2D size = new Dimension(
			(int) (Page.A4_WIDTH * Constants.CM_TO_PT),
			(int) (Page.A4_HEIGHT * Constants.CM_TO_PT));

	private ILayer activeLayer;

	/**
	 * Default Constructor.
	 */
	public Page() {
		this.layers = new ArrayDeque<ILayer>();
	}

	/** {@inheritDoc} */
	public ILayer getBackground() {
		return this.layers.peekFirst();
	}

	/** {@inheritDoc} */
	public Deque<ILayer> getLayers() {
		// TODO alter this to use some sort of unmodifiable Deque
		return this.layers.clone();
	}

	public void setActiveLayer(ILayer layer) throws IllegalArgumentException {
		if (layers.contains(layer)) {
			activeLayer = layer;
		} else {
			throw new IllegalArgumentException(
					"The layer specified is not part of this document.");
		}
	}

	/** {@inheritDoc} */
	public ILayer getActiveLayer() {
		if (activeLayer == null) {
			setActiveLayer(layers.peekLast());
		}
		return activeLayer;
		/*
		 * final ILayer top = this.layers.peekLast(); if (top instanceof
		 * CurveLayer) { return (CurveLayer) top; } else { final CurveLayer c =
		 * new CurveLayer(); this.layers.add(c); return c; }
		 */
	}

	/** {@inheritDoc} */
	public Dimension2D getPageSize() {
		return this.size;
	}

	/**
	 * Setter for page size.
	 * 
	 * @param newSize
	 *            the new Page size.
	 */
	public void setPageSize(final Dimension2D newSize) {
		this.size = (Dimension2D) newSize.clone();
	}

	/**
	 * Add a new layer on top of this page.
	 * 
	 * @param layer
	 *            the new layer.
	 */
	public void addLayer(final ILayer layer) {
		LOGGER.info("addLayer(" + layer + ")");
		this.layers.add(layer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.document.IPage#getAllPens()
	 */
	@Override
	public final List<IWorkingTool> getAllPens() {
		List<IWorkingTool> pens = new LinkedList<IWorkingTool>();
		for (ILayer l : getLayers()) {
			pens.addAll(l.getAllPens());
		}
		return pens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see menya.core.document.IPage#getDefaultPen()
	 */
	@Override
	public final IWorkingTool getDefaultPen() {
		return getActiveLayer().getDefaultPen();
	}

	@Override
	public final IWorkingTool getActivePen() {
		return getActiveLayer().getActivePen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * menya.core.document.IPage#setActivePen(menya.core.model.IWorkingTool)
	 */
	@Override
	public void setActivePen(IWorkingTool pen) throws IllegalArgumentException {
		getActiveLayer().setActivePen(pen);
	}
}
