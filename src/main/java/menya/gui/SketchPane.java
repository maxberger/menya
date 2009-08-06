/*
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import jpen.PButton;
import jpen.PButtonEvent;
import jpen.PKind;
import jpen.PLevelEvent;
import jpen.Pen;
import jpen.PenManager;
import jpen.PLevel.Type;
import jpen.event.PenAdapter;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.CurveLayer;
import menya.core.model.Curve;
import menya.core.model.GraphicalData;
import menya.core.model.Point;

/**
 * The pane where one can draw on.
 * 
 * @author Max
 * @version $Revision$
 */
public class SketchPane extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final float MAX_PEN_WIDTH = 5.0f;

    private static final float DEFAULT_PRESSURE = 0.5f;

    private final IPage currentPage;

    private final CurveLayer activeLayer;

    private Curve currentCurve;

    private BufferedImage cachedImage;

    /**
     * Default constructor.
     * 
     * @param page
     *            the Page to display in this pane.
     */
    public SketchPane(final IPage page) {
        this.currentPage = page;
        this.activeLayer = page.getActiveLayer();
        // this.addMouseListener(this);
        // this.addMouseMotionListener(this);

        final PenManager penManager = new PenManager(this);
        penManager.pen.addListener(new PenAdapter() {
            @Override
            public void penLevelEvent(final PLevelEvent ev) {
                SketchPane.this.penLevelEvent(ev);
            }

            @Override
            public void penButtonEvent(final PButtonEvent ev) {
                SketchPane.this.penButtonEvent(ev);
            }
        });
        this.setSize(this.getPreferredSize());
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        for (final ILayer l : this.currentPage.getLayers()) {
            if (l.hasChanged()) {
                this.cachedImage = null;
            }
        }
        if (this.cachedImage == null) {
            final Dimension2D pageSize = this.currentPage.getPageSize();
            final BufferedImage img = new BufferedImage((int) Math
                    .ceil(pageSize.getWidth()), (int) Math.ceil(pageSize
                    .getHeight()), BufferedImage.TYPE_INT_RGB);

            final Graphics2D g2dImage = img.createGraphics();
            g2dImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2dImage.setColor(Color.WHITE);
            g2dImage.fill(new Rectangle2D.Double(0, 0, pageSize.getWidth(),
                    pageSize.getHeight()));

            for (final ILayer l : this.currentPage.getLayers()) {
                for (final GraphicalData gd : l.getGraphicalData()) {
                    gd.draw(g2dImage);
                }
            }
            this.cachedImage = img;
        }

        final Graphics2D g2dReal = (Graphics2D) g;
        g2dReal.drawImage(this.cachedImage, null, 0, 0);
        g2dReal.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (this.currentCurve != null) {
            this.currentCurve.draw(g2dReal);
        }
    }

    private Dimension dim2DtoDim(final Dimension2D d2d) {
        return new Dimension((int) Math.ceil(d2d.getWidth()), (int) Math
                .ceil(d2d.getHeight()));
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
        return this.dim2DtoDim(this.currentPage.getPageSize());
    }

    /** {@inheritDoc} */
    public void penButtonEvent(final PButtonEvent ev) {
        final PButton button = ev.button;
        if (PButton.Type.LEFT.equals(button.getType())) {
            final Point p = this.toPoint(ev.pen);

            if (button.value) {
                this.startCurve(p);
            } else {
                this.stopCurve(p);
            }
        }
    }

    private void stopCurve(final Point p) {
        if (this.currentCurve == null) {
            return;
        }
        this.currentCurve.add(p);
        this.currentCurve.smoothPath();
        this.activeLayer.addCurve(this.currentCurve);
        this.currentCurve = null;
        // TODO: Maybe erase path that was drawn during drag.
        this.repaint();
    }

    private void startCurve(final Point p) {
        this.currentCurve = new Curve();
        this.currentCurve.add(p);
    }

    private Point toPoint(final Pen pen) {
        final float posx = pen.getLevelValue(Type.X);
        final float posy = pen.getLevelValue(Type.Y);
        final float pressure;
        if (PKind.Type.STYLUS.equals(pen.getKind().getType())) {
            pressure = pen.getLevelValue(Type.PRESSURE);
        } else {
            pressure = SketchPane.DEFAULT_PRESSURE;
        }
        // TODO: Improve!
        return new Point(posx, posy, pressure * SketchPane.MAX_PEN_WIDTH);
    }

    /** {@inheritDoc} */
    public void penLevelEvent(final PLevelEvent ev) {
        if (this.currentCurve == null) {
            return;
        }
        final Point newP = this.toPoint(ev.pen);
        // final Point old = this.currentPath.get(this.currentPath.size() - 1);
        // if (newP.distanceTo(old) > 10.0) {
        this.currentCurve.add(newP);
        // }
        this.repaint();
    }

    /** {@inheritDoc} */
    public void mouseMoved(final MouseEvent arg0) {
        // ignore this event.
    }

}
