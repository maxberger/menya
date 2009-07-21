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

public class HelloCurve extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 8569442036086773881L;

    private static class TestPathIterator implements PathIterator {

        static int count = 5;

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
            switch (this.current) {
            case 0:
                retVal = PathIterator.SEG_MOVETO;
                d[0] = 100.0;
                d[1] = 300.0;
                break;
            case 1:
                retVal = PathIterator.SEG_QUADTO;
                d[0] = 200.0;
                d[1] = 195.0;
                d[2] = 300.0;
                d[3] = 300.0;
                break;
            case 2:
                retVal = PathIterator.SEG_LINETO;
                d[0] = 300.0;
                d[1] = 300.0;
                break;
            case 3:
                retVal = PathIterator.SEG_QUADTO;
                d[0] = 200.0;
                d[1] = 205.0;
                d[2] = 100.0;
                d[3] = 300.0;
                break;
            }
            return retVal;
        }

        public int getWindingRule() {
            return PathIterator.WIND_NON_ZERO;
        }

        public boolean isDone() {
            return this.current >= TestPathIterator.count;
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
                mainFrame.setContentPane(new HelloCurve());
            }
        });
    }
}
