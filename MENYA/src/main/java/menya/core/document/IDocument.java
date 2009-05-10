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

package menya.core.document;

import java.util.Deque;

import menya.core.document.layers.CurveLayer;

/**
 * This interfaces represents a MENYA annotation document.
 * 
 * @author Dominik
 * @version $Revision$
 */
public interface IDocument {
    /**
     * retrieves the background of this document. Basically would it be the
     * lowest (=first) layer. Thus it is the same as if we would call
     * getLayers().peekFirst().
     * 
     * @return the background of this document or null if no layers are present.
     */
    ILayer getBackground();

    /**
     * retrieves all layers of this document.
     * 
     * @return the document layers as stack
     */
    Deque<ILayer> getLayers();

    /**
     * Retrieves an active curve layer. This method never returns null - if no
     * curve layer is present, a new one must be created.
     * 
     * @return the active curve layer.
     */
    CurveLayer getActiveLayer();
}
