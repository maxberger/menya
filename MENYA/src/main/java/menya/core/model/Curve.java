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
package menya.core.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import menya.gui.PathShape;

/**
 * implements the necessary functionality to be a drawn curve within a document.
 * 
 * @author Dominik
 * @version $Revision$
 */
public class Curve implements GraphicalData, Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Point> path = new ArrayList<Point>();

    /**
     * Default constructor.
     */
    public Curve() {

    }

    /** {@inheritDoc} */
    public void draw(final Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fill(new PathShape(this.path));
    }

    /**
     * Adds a new Point to a curve.
     * 
     * @param p
     *            The point to add.
     */
    public void add(final Point p) {
        this.path.add(p);
    }
}
