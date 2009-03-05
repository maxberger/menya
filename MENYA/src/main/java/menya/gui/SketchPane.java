package menya.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import menya.core.model.Point;

/**
 * The pane where one can draw on.
 * 
 * @author Max
 * @version $Revision: 11 $
 */
public class SketchPane extends JComponent implements MouseListener,
        MouseMotionListener {

    private final List<List<Point>> paths = new ArrayList<List<Point>>();

    private List<Point> currentPath;

    /**
     * Default constructor.
     */
    public SketchPane() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawPaths(g2d);
    }

    private void drawPaths(final Graphics2D g2d) {
        // TODO: Path may have different colors
        g2d.setColor(Color.BLACK);
        for (final List<Point> path : this.paths) {
            // TODO: Cache pathShape
            g2d.fill(new PathShape(path));
        }
        if (this.currentPath != null) {
            g2d.fill(new PathShape(this.currentPath));
        }
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize() {
        // TODO: Find sensible values.
        return new Dimension(50, 50);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
        // TODO: Find sensible values.
        return new Dimension(800, 600);
    }

    /** {@inheritDoc} */
    public void mouseClicked(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mouseEntered(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mouseExited(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mousePressed(final MouseEvent arg0) {
        final Point p = this.toPoint(arg0);
        this.currentPath = new ArrayList<Point>();
        this.currentPath.add(p);
    }

    private Point toPoint(final MouseEvent arg0) {
        // TODO: Improve!
        return new Point(arg0.getX(), arg0.getY(), 3.0);
    }

    /** {@inheritDoc} */
    public void mouseReleased(final MouseEvent arg0) {
        if (this.currentPath == null) {
            return;
        }
        this.currentPath.add(this.toPoint(arg0));
        // TODO: Smooth Path
        this.paths.add(this.currentPath);
        this.currentPath = null;
        // TODO: Maybe erase path that was drawn during drag.
        this.repaint();
    }

    /** {@inheritDoc} */
    public void mouseDragged(final MouseEvent arg0) {
        if (this.currentPath == null) {
            return;
        }
        final Point newP = this.toPoint(arg0);
        // final Point old = this.currentPath.get(this.currentPath.size() - 1);
        // if (newP.distanceTo(old) > 10.0) {
        this.currentPath.add(newP);
        // }
        this.repaint();
    }

    /** {@inheritDoc} */
    public void mouseMoved(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
