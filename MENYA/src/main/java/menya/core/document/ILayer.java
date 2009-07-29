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

import java.io.IOException;

import menya.core.model.GraphicalData;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * This interface represents a common background for a document. A layer
 * basically provides graphical data.
 * 
 * @see menya.core.document.IDocument
 * @see menya.core.model.GraphicalData
 * @author Dominik
 * @author Max
 * @version $Revision$
 */
public interface ILayer {
    /**
     * retrieves the graphical data provided by this layer.
     * 
     * @return A Set of graphical data.
     */
    Iterable<GraphicalData> getGraphicalData();

    /**
     * Simple check if this layer has changed since {@link #getGraphicalData()}
     * was last called.
     * <p>
     * If getGraphicalData was never called the result is undefined.
     * 
     * @return false only if the data has not changed.
     */
    boolean hasChanged();

    /**
     * Serialize this layer to the given PDF content stream.
     * 
     * @param contentStream
     *            PDF Content stream.
     * @param page
     *            the current page this layer originate of.
     * @throws IOException
     *             if the layer cannot be written to the content stream.
     */
    void toPdf(PDPageContentStream contentStream, IPage page)
            throws IOException;
}
