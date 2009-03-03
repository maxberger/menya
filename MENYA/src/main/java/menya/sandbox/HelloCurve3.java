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

        public TestPathIterator(double[][] p) {
            this.points = p;
            count = points.length;
            slope = new double[count][];
            slope[0] = new double[] { 0, 0 };
            slope[count - 1] = new double[] { 0, 0 };
            for (int i = 1; i < (count - 1); i++) {
                slope[i] = new double[2];
                slope[i][0] = (points[i + 1][0] - points[i - 1][0])
                        * POINTINESS;
                slope[i][1] = (points[i + 1][1] - points[i - 1][1])
                        * POINTINESS;
            }

            inverseslope = new double[count][];
            for (int i = 0; i < count; i++) {
                inverseslope[i] = new double[2];
                double slopelength = Math.sqrt(slope[i][0] * slope[i][0]
                        + slope[i][1] * slope[i][1]);
                if (slopelength > 0.0001) {
                    inverseslope[i][0] = (slope[i][1] / slopelength)
                            * (points[i][2] / 2);
                    inverseslope[i][1] = (slope[i][0] / slopelength)
                            * (points[i][2] / 2);
                } else {
                    inverseslope[i][0] = 0.0;
                    inverseslope[i][1] = 0.0;
                }
            }
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
            } else if (current < count) {
                retVal = SEG_CUBICTO;

                d[0] = points[current - 1][0] + inverseslope[current - 1][0]
                        + slope[current - 1][0];
                d[1] = points[current - 1][1] + inverseslope[current - 1][1]
                        + slope[current - 1][1];

                d[2] = points[current][0] + inverseslope[current][0]
                        - slope[current][0];
                d[3] = points[current][1] + inverseslope[current][1]
                        - slope[current][1];
                d[4] = points[current][0] + inverseslope[current][0];
                d[5] = points[current][1] + inverseslope[current][1];
                for (double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println();
            } else {
                retVal = SEG_CUBICTO;
                int bcurr = (count * 2) - current - 2;
                d[0] = points[bcurr + 1][0] - inverseslope[bcurr + 1][0]
                        - slope[bcurr + 1][0];
                d[1] = points[bcurr + 1][1] - inverseslope[bcurr + 1][1]
                        - slope[bcurr + 1][1];

                d[2] = points[bcurr][0] - inverseslope[bcurr][0]
                        + slope[bcurr][0];
                d[3] = points[bcurr][1] - inverseslope[bcurr][1]
                        + slope[bcurr][1];
                d[4] = points[bcurr][0] - inverseslope[bcurr][0];
                d[5] = points[bcurr][1] - inverseslope[bcurr][1];
                for (double dd : d) {
                    System.out.print(" " + dd);
                }
                System.out.println(" / " + current);
            }
            return retVal;
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return current >= (2 * count - 1);
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
            return new TestPathIterator(POINTS);
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        for (int i = 0; i < POINTS.length; i++) {
            g2d.fill(new Rectangle2D.Double(POINTS[i][0] - POINTS[i][2] / 2.0,
                    POINTS[i][1] - POINTS[i][2] / 2.0, POINTS[i][2],
                    POINTS[i][2]));
        }
        g2d.setColor(Color.BLACK);
        g2d.fill(new TestShape());
        g2d.setColor(Color.BLUE);
        g2d.draw(new TestShape());
    }

    public static void main(String args[]) {
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
