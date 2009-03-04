package menya.core.document;

import java.util.Stack;


/**
 * This interfaces represents a MENYA annotation document.
 * 
 * @author Dominik
 * @version $Revision$
 */
public interface IDocument {

    /**
     * retrieves the background of this document.
     * Basically would it be the lowest layer.
     * @return the background of this document
     */
    ILayer getBackground();
    
    /**
     * retrieves all layers of this document.
     * @return the document layers as stack
     */
    Stack<ILayer> getLayers();
}
