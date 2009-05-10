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

/**
 * Repesents a single point.
 * 
 * @author Max
 * @author Dominik
 * @version $Revision$
 */
public class Point {

    private final double x;
    private final double y;
    private final double size;

    // TODO: Other attributes, such as tilt or color

    /**
     * Create a new Point.
     * 
     * @param xP
     *            x-position
     * @param yP
     *            y-position
     * @param s
     *            size
     */
    public Point(final double xP, final double yP, final double s) {
        this.x = xP;
        this.y = yP;
        this.size = s;
    }

    /**
     * @return X-Coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return Y-Coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return Size
     */
    public double getSize() {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append(this.x);
        b.append('/');
        b.append(this.y);
        b.append(" (");
        b.append(this.size);
        b.append(')');
        return b.toString();
    }

    /**
     * Calculate the distance between this point and other.
     * 
     * @param other
     *            the other point
     * @return distance.
     */
    public double distanceTo(final Point other) {
        return Math.sqrt((this.x - other.x) * (this.x - other.x)
                + (this.y - other.y) * (this.y - other.y));
    }
}
