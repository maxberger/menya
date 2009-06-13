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

/* $Id: ImageLayer.java 50 2009-05-10 08:36:17Z berger.max $ */

package menya.core.document.layers;

import menya.core.model.GraphicalData;

/**
 * A single layer on a single page.
 * 
 * @author Max
 */
public interface IPageLayer {
    /**
     * @return The graphics for this layer.
     */
    public Iterable<GraphicalData> getGraphicalData();
}
