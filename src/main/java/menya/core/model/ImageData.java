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

package menya.core.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Represents a static image.
 * 
 * @author Max
 * @version $Revision$
 */
public class ImageData implements GraphicalData {

    private final BufferedImage image;

    /**
     * Create a new ImageData from a given Image.
     * 
     * @param img
     *            the image to use.
     */
    public ImageData(final BufferedImage img) {
        this.image = img;
    }

    /** {@inheritDoc} */
    public void draw(final Graphics2D g2d) {
        g2d.drawImage(this.image, null, 0, 0);
    }

}
