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

/* $Id: ALayer.java 32 2009-03-06 10:48:52Z berger.max@gmail.com $ */

package menya.core.document.layers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import menya.core.model.GraphicalData;
import menya.core.model.ImageData;

/**
 * Represents an image layer.
 * 
 * @author Max
 * @version $Revision: 123 $
 */
public class ImageLayer extends ALayer {

    private final ImageData image;

    /**
     * Default constructor.
     */
    public ImageLayer() {
        BufferedImage bimage;
        ImageData img;
        try {
            bimage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/2367382540_a2d7b51a5d_b.jpg"));
            img = new ImageData(bimage);
        } catch (final IOException e) {
            img = null;
        }
        this.image = img;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Iterable<GraphicalData> getGraphicalData() {
        if (this.image == null) {
            return Collections.emptyList();
        } else {
            return (List) Collections.singletonList(this.image);
        }
    }

}
