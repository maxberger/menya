package menya.sandbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * A simple test with Splines and thickness.
 * 
 * @author Max
 * @version $Revision$
 */
public class HelloCurve3 extends JComponent {

    static double[][] POINTS = { { 100, 300, 5 }, { 200, 200, 5 },
            { 300, 200, 5 }, { 400, 300, 7 }, { 500, 300, 10 },
            { 600, 200, 12 }, { 700, 250, 10 }, { 750, 100, 1 },
            { 650, 150, 3 }, { 600, 100, 7 }, { 500, 50, 5 }, { 400, 150, 5 } };

    private static class TestPathIterator implements PathIterator {

        final double[][] points;

        final int count;

        final double[][] slope;
        final double[][] inverseslope;

        static final double POINTINESS = 0.2;

        int current = 0;

        public TestPathIterator(final double[][] p) {
            this.points = p;
            this.count = this.points.length;
            this.slope = new double[this.count][];
            this.slope[0] = new double[] { 0, 0 };
            this.slope[this.count - 1] = new double[] { 0, 0 };
            for (int i = 1; i < this.count - 1; i++) {
                this.slope[i] = new double[2];
                this.slope[i][0] = (this.points[i + 1][0] - this.points[i - 1][0])
                        * TestPathIterator.POINTINESS;
                this.slope[i][1] = (this.points[i + 1][1] - this.points[i - 1][1])
                        * TestPathIterator.POINTINESS;
            }

            this.inverseslope = new double[this.count][];
            for (int i = 0; i < this.count; i++) {
                this.inverseslope[i] = new double[2];

                double theta = Math.atan2(this.slope[i][1], this.slope[i][0]);
                theta += Math.PI / 2.0;

                final double size = this.points[i][2] / 2;

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
                d[0] = this.points[this.current][0];
                d[1] = this.points[this.current][1];
            } else if (this.current < this.count) {
                retVal = PathIterator.SEG_CUBICTO;

                d[0] = this.points[this.current - 1][0]
                        + this.inverseslope[this.current - 1][0]
                        + this.slope[this.current - 1][0];
                d[1] = this.points[this.current - 1][1]
                        + this.inverseslope[this.current - 1][1]
                        + this.slope[this.current - 1][1];

                d[2] = this.points[this.current][0]
                        + this.inverseslope[this.current][0]
                        - this.slope[this.current][0];
                d[3] = this.points[this.current][1]
                        + this.inverseslope[this.current][1]
                        - this.slope[this.current][1];
                d[4] = this.points[this.current][0]
                        + this.inverseslope[this.current][0];
                d[5] = this.points[this.current][1]
                        + this.inverseslope[this.current][1];
                for (final double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println();
            } else {
                retVal = PathIterator.SEG_CUBICTO;
                final int bcurr = this.count * 2 - this.current - 2;
                d[0] = this.points[bcurr + 1][0]
                        - this.inverseslope[bcurr + 1][0]
                        - this.slope[bcurr + 1][0];
                d[1] = this.points[bcurr + 1][1]
                        - this.inverseslope[bcurr + 1][1]
                        - this.slope[bcurr + 1][1];

                d[2] = this.points[bcurr][0] - this.inverseslope[bcurr][0]
                        + this.slope[bcurr][0];
                d[3] = this.points[bcurr][1] - this.inverseslope[bcurr][1]
                        + this.slope[bcurr][1];
                d[4] = this.points[bcurr][0] - this.inverseslope[bcurr][0];
                d[5] = this.points[bcurr][1] - this.inverseslope[bcurr][1];
                for (final double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println(" / " + this.current);
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

    private static class TestShape implements Shape {

        public boolean contains(final Point2D p) {
            return false;
        }

        public boolean contains(final Rectangle2D r) {
            return false;
        }

        public boolean contains(final double x, final double y) {
            return false;
        }

        public boolean contains(final double x, final double y, final double w,
                final double h) {
            return false;
        }

        public Rectangle getBounds() {
            return null;
        }

        public Rectangle2D getBounds2D() {
            return null;
        }

        public PathIterator getPathIterator(final AffineTransform at) {
            return new TestPathIterator(HelloCurve3.POINTS);
        }

        public PathIterator getPathIterator(final AffineTransform at,
                final double flatness) {
            return this.getPathIterator(at);
        }

        public boolean intersects(final Rectangle2D r) {
            return false;
        }

        public boolean intersects(final double x, final double y,
                final double w, final double h) {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.fill(new TestShape());
        g2d.setColor(Color.BLUE);
        g2d.draw(new TestShape());
        g2d.setColor(Color.RED);
        for (final double[] element : HelloCurve3.POINTS) {
            g2d.fill(new Rectangle2D.Double(element[0] - element[2] / 2.0,
                    element[1] - element[2] / 2.0, element[2], element[2]));
        }
    }

    /**
     * Test bi-cubic curves with points with different thickness.
     * 
     * @param args
     *            not used.
     */
    public static void main(final String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame mainFrame = new JFrame("Hello, Curve!");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                mainFrame.setVisible(true);
                mainFrame.setContentPane(new HelloCurve3());
            }
        });
    }
}
