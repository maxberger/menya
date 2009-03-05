package menya.gui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import menya.core.model.Point;

/**
 * Shape for a Path.
 * 
 * @author Max
 * @version $Revision: 123 $
 */
public class PathShape implements Shape {

    private final List<Point> path;
    private static final double POINTINESS = 0.2;

    private static class PathShapeIterator implements PathIterator {

        private static final double ALMOST_ZERO = 0.0001;

        private final int count;

        private final double[][] slope;
        private final double[][] inverseslope;
        private final List<Point> points;

        private int current;

        public PathShapeIterator(final List<Point> p) {
            this.points = p;
            this.count = this.points.size();
            this.slope = new double[this.count][];
            this.slope[0] = new double[] { 0, 0 };
            this.slope[this.count - 1] = new double[] { 0, 0 };
            for (int i = 1; i < this.count - 1; i++) {
                this.slope[i] = new double[2];
                this.slope[i][0] = (this.points.get(i + 1).getX() - this.points
                        .get(i - 1).getX())
                        * PathShape.POINTINESS;
                this.slope[i][1] = (this.points.get(i + 1).getY() - this.points
                        .get(i - 1).getY())
                        * PathShape.POINTINESS;
            }

            this.inverseslope = new double[this.count][];
            for (int i = 0; i < this.count; i++) {
                this.inverseslope[i] = new double[2];

                double theta = Math.atan2(this.slope[i][1], this.slope[i][0]);
                theta += Math.PI / 2.0;
                final double size = this.points.get(i).getSize() / 2;

                this.inverseslope[i][0] = Math.cos(theta) * size;
                this.inverseslope[i][1] = Math.sin(theta) * size;
            }
        }

        public int currentSegment(final float[] arg0) {
            final double[] d = new double[arg0.length];
            final int retVal = this.currentSegment(d);
            for (int i = 0; i < arg0.length; i++) {
                arg0[i] = (float) d[i];
            }
            return retVal;
        }

        public int currentSegment(final double[] d) {
            int retVal = PathIterator.SEG_CLOSE;

            if (this.current == 0) {
                retVal = PathIterator.SEG_MOVETO;
                d[0] = this.points.get(this.current).getX();
                d[1] = this.points.get(this.current).getY();
            } else if (this.current < this.count) {
                retVal = PathIterator.SEG_CUBICTO;

                d[0] = this.points.get(this.current - 1).getX()
                        + this.inverseslope[this.current - 1][0]
                        + this.slope[this.current - 1][0];
                d[1] = this.points.get(this.current - 1).getY()
                        + this.inverseslope[this.current - 1][1]
                        + this.slope[this.current - 1][1];

                d[2] = this.points.get(this.current).getX()
                        + this.inverseslope[this.current][0]
                        - this.slope[this.current][0];
                d[3] = this.points.get(this.current).getY()
                        + this.inverseslope[this.current][1]
                        - this.slope[this.current][1];
                d[4] = this.points.get(this.current).getX()
                        + this.inverseslope[this.current][0];
                d[5] = this.points.get(this.current).getY()
                        + this.inverseslope[this.current][1];
            } else {
                retVal = PathIterator.SEG_CUBICTO;
                final int bcurr = this.count * 2 - this.current - 2;
                d[0] = this.points.get(bcurr + 1).getX()
                        - this.inverseslope[bcurr + 1][0]
                        - this.slope[bcurr + 1][0];
                d[1] = this.points.get(bcurr + 1).getY()
                        - this.inverseslope[bcurr + 1][1]
                        - this.slope[bcurr + 1][1];

                d[2] = this.points.get(bcurr).getX()
                        - this.inverseslope[bcurr][0] + this.slope[bcurr][0];
                d[3] = this.points.get(bcurr).getY()
                        - this.inverseslope[bcurr][1] + this.slope[bcurr][1];
                d[4] = this.points.get(bcurr).getX()
                        - this.inverseslope[bcurr][0];
                d[5] = this.points.get(bcurr).getY()
                        - this.inverseslope[bcurr][1];
            }
            return retVal;
        }

        public int getWindingRule() {
            return PathIterator.WIND_NON_ZERO;
        }

        public boolean isDone() {
            return this.current >= 2 * this.count - 1;
        }

        public void next() {
            this.current++;
        }

    }

    /**
     * Create a new PathShape for the given Path.
     * 
     * @param p
     *            the Path.
     */
    public PathShape(final List<Point> p) {
        this.path = p;
    }

    /** {@inheritDoc} */
    public boolean contains(final Point2D arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
    public boolean contains(final Rectangle2D arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
    public boolean contains(final double arg0, final double arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
    public boolean contains(final double arg0, final double arg1,
            final double arg2, final double arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
    public Rectangle getBounds() {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public Rectangle2D getBounds2D() {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public PathIterator getPathIterator(final AffineTransform arg0) {
        // TODO: Apply transforms
        return new PathShapeIterator(this.path);
    }

    /** {@inheritDoc} */
    public PathIterator getPathIterator(final AffineTransform arg0,
            final double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public boolean intersects(final Rectangle2D arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /** {@inheritDoc} */
    public boolean intersects(final double arg0, final double arg1,
            final double arg2, final double arg3) {
        // TODO Auto-generated method stub
        return false;
    }

}
