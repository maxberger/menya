/*
 * Copyright 2009 - 2009 by Menya Project
 * 
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id$ */

package menya.core.document.layers;

import java.util.ArrayList;
import java.util.List;

import menya.core.model.Curve;
import menya.core.model.GraphicalData;

/**
 * Represents a layer with curves drawn.
 * 
 * @author Max
 * @version $Revision$
 */
public class CurveLayer extends ALayer {

    private final List<Curve> curves = new ArrayList<Curve>();

    private boolean hasChanged = true;

    /**
     * Default constructor.
     */
    public CurveLayer() {
        // empty on purpose.
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Iterable<GraphicalData> getGraphicalData() {
        final Iterable<Curve> i = this.curves;
        final Iterable d = i;
        this.hasChanged = false;
        return d;
    }

    /**
     * Adds a new curve to the layer.
     * 
     * @param curve
     *            the curve to add.
     */
    public void addCurve(final Curve curve) {
        this.hasChanged = true;
        this.curves.add(curve);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChanged() {
        return this.hasChanged;
    }

}
