package menya.core.model;

import java.util.ArrayDeque;
import java.util.Deque;

import menya.core.document.IDocument;
import menya.core.document.ILayer;
import menya.core.document.layers.CurveLayer;
import menya.core.document.layers.ImageLayer;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision: 11 $
 */
public class Document implements IDocument {

    private final ArrayDeque<ILayer> layers;

    /**
     * Default Constructor.
     */
    public Document() {
        this.layers = new ArrayDeque<ILayer>();

        this.layers.add(new ImageLayer());
    }

    /** {@inheritDoc} */
    public ILayer getBackground() {
        return this.layers.peekFirst();
    }

    /** {@inheritDoc} */
    public Deque<ILayer> getLayers() {
        return this.layers.clone();
    }

    /** {@inheritDoc} */
    public CurveLayer getActiveLayer() {
        final ILayer top = this.layers.peekLast();
        if (top instanceof CurveLayer) {
            return (CurveLayer) top;
        } else {
            final CurveLayer c = new CurveLayer();
            this.layers.add(c);
            return c;
        }
    }
}
