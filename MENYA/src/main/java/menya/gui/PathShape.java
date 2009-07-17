package menya.gui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import menya.core.model.Point;

/**
 * Shape for a Path.
 * 
 * @author Max
 * @version $Revision$
 */
public class PathShape implements Shape {

    private static final double POINTINESS = 0.2;

    private static final int SOLE_X = 0;
    private static final int SOLE_Y = 1;

    private static final int START_X = 0;
    private static final int START_Y = 1;
    private static final int MID_X = 2;
    private static final int MID_Y = 3;
    private static final int END_X = 4;
    private static final int END_Y = 5;

    private final List<Point> path;

    private static class PathShapeIterator implements PathIterator {

        private final int count;

        private final double[][] slope;
        private final double[][] inverseslope;
        private final List<Point> points;
        private final AffineTransform transform;
        private final boolean flattened;

        private int current;

        protected PathShapeIterator(final List<Point> p,
                final AffineTransform t, final boolean flat) {
            this.flattened = flat;
            this.transform = t;
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
                retVal = this.fillInStart(d);
            } else if (this.current < this.count) {
                retVal = this.fillInFirstHalf(d);
            } else {
                retVal = this.fillInSecondHalf(d);
            }
            if (this.transform != null) {
                this.transform.transform(d, 0, d, 0, d.length / 2);
            }
            return retVal;
        }

        /**
         * @param d
         * @return
         */
        private int fillInSecondHalf(final double[] d) {
            int retVal;
            final int bcurr = this.count * 2 - this.current - 2;
            final double midX = this.points.get(bcurr).getX()
                    - this.inverseslope[bcurr][0] + this.slope[bcurr][0];
            final double midY = this.points.get(bcurr).getY()
                    - this.inverseslope[bcurr][1] + this.slope[bcurr][1];
            if (this.flattened) {
                retVal = PathIterator.SEG_LINETO;
                d[PathShape.SOLE_X] = midX;
                d[PathShape.SOLE_Y] = midY;
            } else {
                retVal = PathIterator.SEG_CUBICTO;
                d[PathShape.START_X] = this.points.get(bcurr + 1).getX()
                        - this.inverseslope[bcurr + 1][0]
                        - this.slope[bcurr + 1][0];
                d[PathShape.START_Y] = this.points.get(bcurr + 1).getY()
                        - this.inverseslope[bcurr + 1][1]
                        - this.slope[bcurr + 1][1];

                d[PathShape.MID_X] = midX;
                d[PathShape.MID_Y] = midY;
                d[PathShape.END_X] = this.points.get(bcurr).getX()
                        - this.inverseslope[bcurr][0];
                d[PathShape.END_Y] = this.points.get(bcurr).getY()
                        - this.inverseslope[bcurr][1];
            }
            return retVal;
        }

        /**
         * @param d
         * @return
         */
        private int fillInFirstHalf(final double[] d) {
            int retVal;
            final double midX = this.points.get(this.current).getX()
                    + this.inverseslope[this.current][0]
                    - this.slope[this.current][0];
            final double midY = this.points.get(this.current).getY()
                    + this.inverseslope[this.current][1]
                    - this.slope[this.current][1];
            if (this.flattened) {
                retVal = PathIterator.SEG_LINETO;
                d[PathShape.SOLE_X] = midX;
                d[PathShape.SOLE_Y] = midY;
            } else {
                retVal = PathIterator.SEG_CUBICTO;

                d[PathShape.START_X] = this.points.get(this.current - 1).getX()
                        + this.inverseslope[this.current - 1][0]
                        + this.slope[this.current - 1][0];
                d[PathShape.START_Y] = this.points.get(this.current - 1).getY()
                        + this.inverseslope[this.current - 1][1]
                        + this.slope[this.current - 1][1];
                d[PathShape.MID_X] = midX;
                d[PathShape.MID_Y] = midY;
                d[PathShape.END_X] = this.points.get(this.current).getX()
                        + this.inverseslope[this.current][0];
                d[PathShape.END_Y] = this.points.get(this.current).getY()
                        + this.inverseslope[this.current][1];
            }
            return retVal;
        }

        /**
         * @param d
         * @return
         */
        private int fillInStart(final double[] d) {
            int retVal;
            retVal = PathIterator.SEG_MOVETO;
            d[PathShape.SOLE_X] = this.points.get(this.current).getX();
            d[PathShape.SOLE_Y] = this.points.get(this.current).getY();
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
        return this.contains(arg0.getX(), arg0.getY());
    }

    /** {@inheritDoc} */
    public boolean contains(final double x, final double y) {
        return new Area(this).contains(x, y);
    }

    /** {@inheritDoc} */
    public boolean contains(final Rectangle2D arg0) {
        return this.contains(arg0.getX(), arg0.getY(), arg0.getWidth(), arg0
                .getHeight());
    }

    /** {@inheritDoc} */
    public boolean contains(final double x, final double y, final double w,
            final double h) {
        return new Area(this).contains(x, y, w, h);
    }

    /** {@inheritDoc} */
    public Rectangle getBounds() {
        return new Area(this).getBounds();
    }

    /** {@inheritDoc} */
    public Rectangle2D getBounds2D() {
        return new Area(this).getBounds2D();
    }

    /** {@inheritDoc} */
    public PathIterator getPathIterator(final AffineTransform at) {
        return new PathShapeIterator(this.path, at, false);
    }

    /** {@inheritDoc} */
    public PathIterator getPathIterator(final AffineTransform at,
            final double distance) {
        // TODO: distance is not properly supported.
        return new PathShapeIterator(this.path, at, true);
    }

    /** {@inheritDoc} */
    public boolean intersects(final Rectangle2D arg0) {
        return this.intersects(arg0.getX(), arg0.getY(), arg0.getWidth(), arg0
                .getHeight());
    }

    /** {@inheritDoc} */
    public boolean intersects(final double x, final double y, final double w,
            final double h) {
        return new Area(this).intersects(x, y, w, h);
    }

}
