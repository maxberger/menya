package menya.sandbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class HelloCurve2 extends JComponent {

    private static class TestPathIterator implements PathIterator {

        static double[][] points = { { 100, 300 }, { 200, 200 }, { 300, 200 },
                { 400, 300 }, { 500, 300 }, { 600, 200 }, { 700, 250 },
                { 750, 100 }, { 650, 150 }, { 600, 100 }, { 500, 50 },
                { 400, 150 } };

        final int count = points.length;

        static final double POINTINESS = 0.3;

        int current = 0;

        public TestPathIterator() {

        }

        public int currentSegment(float[] arg0) {
            double[] d = new double[arg0.length];
            int retVal = this.currentSegment(d);
            for (int i = 0; i < arg0.length; i++) {
                arg0[i] = (float) d[i];
            }
            return retVal;
        }

        public int currentSegment(double[] d) {
            int retVal = SEG_CLOSE;

            if (current == 0) {
                retVal = SEG_MOVETO;
                d[0] = points[current][0];
                d[1] = points[current][1];
            } else if (current == 1) {
                d[0] = points[current][0]
                        + (points[current + 1][0] - points[current][0])
                        * POINTINESS;
                d[1] = points[current][1]
                        + (points[current + 1][1] - points[current][1])
                        * POINTINESS;

                d[2] = points[current][0]
                        + (points[current][0] - points[current + 1][0])
                        * POINTINESS;
                d[3] = points[current][1]
                        + (points[current][1] - points[current + 1][1])
                        * POINTINESS;
                d[4] = points[current][0];
                d[5] = points[current][1];

            } else if (current == count - 1) {
                retVal = SEG_LINETO;
                d[0] = points[current][0];
                d[1] = points[current][1];
            } else {
                retVal = SEG_CUBICTO;

                d[0] = points[current - 1][0]
                        - (points[current - 1][0] - points[current][0])
                        * POINTINESS;
                d[1] = points[current - 1][1]
                        - (points[current - 1][1] - points[current][1])
                        * POINTINESS;

                d[2] = points[current][0]
                        + (points[current][0] - points[current + 1][0])
                        * POINTINESS;
                d[3] = points[current][1]
                        + (points[current][1] - points[current + 1][1])
                        * POINTINESS;
                d[4] = points[current][0];
                d[5] = points[current][1];
                for (double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println();
            }
            return retVal;
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return current >= count;
        }

        public void next() {
            current++;
        }

    }

    private static class TestShape implements Shape {

        public boolean contains(Point2D p) {
            return false;
        }

        public boolean contains(Rectangle2D r) {
            return false;
        }

        public boolean contains(double x, double y) {
            return false;
        }

        public boolean contains(double x, double y, double w, double h) {
            return false;
        }

        public Rectangle getBounds() {
            return null;
        }

        public Rectangle2D getBounds2D() {
            return null;
        }

        public PathIterator getPathIterator(AffineTransform at) {
            return new TestPathIterator();
        }

        public PathIterator getPathIterator(AffineTransform at, double flatness) {
            return this.getPathIterator(at);
        }

        public boolean intersects(Rectangle2D r) {
            return false;
        }

        public boolean intersects(double x, double y, double w, double h) {
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(new TestShape());
    }

    public static void main(String args[]) {
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
