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

import menya.core.Constants;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.CurveLayer;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision$
 */
public class Page implements IPage {

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
        return this.layers.clone();
    }

    /** {@inheritDoc} */
    public CurveLayer getActiveLayer() {
        final ILayer top = this.layers.peekLast();
        if (top instanceof CurveLayer) {
            return (CurveLayer) top;
        } else {
            final CurveLayer c = new CurveLayer();
            this.layers.add(c);
            return c;
        }
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
        this.layers.add(layer);
    }
}
