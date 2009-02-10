package menya.core.document;

import menya.core.background.IBackground;

/**
 * This interfaces represents a MENYA annotation document.
 * 
 * @author Dominik
 * @version $Revision$
 */
public interface IDocument {

    /**
     * retrieves the background of this document.
     * 
     * @return the background of this document
     */
    IBackground getBackground();
}
