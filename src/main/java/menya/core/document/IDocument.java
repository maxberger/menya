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

package menya.core.document;

import java.io.IOException;
import java.util.List;

/**
 * This interfaces represents a MENYA annotation document.
 * 
 * @author Dominik
 * @author Max
 * @version $Revision$
 */
public interface IDocument {

    /**
     * @return An unmodifiable view as list of pages in this document.
     */
    List<IPage> getPages();

    /**
     * Closes the document. <em>Should</em> be called before disposing the
     * object.
     */
    void close();

     /**
     * Create a PDF from the current document.
     *
     * @param filename
     *            filename to save as
     * @throws IOException
     *             if saving fails.
     */
    void toPDF(final String filename) throws IOException;
}
