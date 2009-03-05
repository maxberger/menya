package menya.core.model;

/**
 * Repesents a single point.
 * 
 * @author Max
 * @version $Revision: 11 $
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
