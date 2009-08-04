// CHECKSTYLE:OFF This is a sandbox file.
package menya.sandbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class HelloCurve2 extends JComponent {

    private static final long serialVersionUID = 1L;

    private static class TestPathIterator implements PathIterator {

        static double[][] points = { { 100, 300 }, { 200, 200 }, { 300, 200 },
                { 400, 300 }, { 500, 300 }, { 600, 200 }, { 700, 250 },
                { 750, 100 }, { 650, 150 }, { 600, 100 }, { 500, 50 },
                { 400, 150 } };

        final int count = TestPathIterator.points.length;

        static final double POINTINESS = 0.3;

        int current = 0;

        public TestPathIterator() {

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
                d[0] = TestPathIterator.points[this.current][0];
                d[1] = TestPathIterator.points[this.current][1];
            } else if (this.current == 1) {
                d[0] = TestPathIterator.points[this.current][0]
                        + (TestPathIterator.points[this.current + 1][0] - TestPathIterator.points[this.current][0])
                        * TestPathIterator.POINTINESS;
                d[1] = TestPathIterator.points[this.current][1]
                        + (TestPathIterator.points[this.current + 1][1] - TestPathIterator.points[this.current][1])
                        * TestPathIterator.POINTINESS;

                d[2] = TestPathIterator.points[this.current][0]
                        + (TestPathIterator.points[this.current][0] - TestPathIterator.points[this.current + 1][0])
                        * TestPathIterator.POINTINESS;
                d[3] = TestPathIterator.points[this.current][1]
                        + (TestPathIterator.points[this.current][1] - TestPathIterator.points[this.current + 1][1])
                        * TestPathIterator.POINTINESS;
                d[4] = TestPathIterator.points[this.current][0];
                d[5] = TestPathIterator.points[this.current][1];

            } else if (this.current == this.count - 1) {
                retVal = PathIterator.SEG_LINETO;
                d[0] = TestPathIterator.points[this.current][0];
                d[1] = TestPathIterator.points[this.current][1];
            } else {
                retVal = PathIterator.SEG_CUBICTO;

                d[0] = TestPathIterator.points[this.current - 1][0]
                        - (TestPathIterator.points[this.current - 1][0] - TestPathIterator.points[this.current][0])
                        * TestPathIterator.POINTINESS;
                d[1] = TestPathIterator.points[this.current - 1][1]
                        - (TestPathIterator.points[this.current - 1][1] - TestPathIterator.points[this.current][1])
                        * TestPathIterator.POINTINESS;

                d[2] = TestPathIterator.points[this.current][0]
                        + (TestPathIterator.points[this.current][0] - TestPathIterator.points[this.current + 1][0])
                        * TestPathIterator.POINTINESS;
                d[3] = TestPathIterator.points[this.current][1]
                        + (TestPathIterator.points[this.current][1] - TestPathIterator.points[this.current + 1][1])
                        * TestPathIterator.POINTINESS;
                d[4] = TestPathIterator.points[this.current][0];
                d[5] = TestPathIterator.points[this.current][1];
                for (final double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println();
            }
            return retVal;
        }

        public int getWindingRule() {
            return PathIterator.WIND_NON_ZERO;
        }

        public boolean isDone() {
            return this.current >= this.count;
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
            return new TestPathIterator();
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

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        g2d.draw(new TestShape());
    }

    public static void main(final String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame mainFrame = new JFrame("Hello, Curve!");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                mainFrame.setVisible(true);
                mainFrame.setContentPane(new HelloCurve2());
            }
        });
    }
}
