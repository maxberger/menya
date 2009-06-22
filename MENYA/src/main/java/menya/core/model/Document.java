package menya.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import menya.core.document.IDocument;
import menya.core.document.IPage;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision$
 */
public class Document implements IDocument {

    private final List<IPage> pages;

    /**
     * Default Constructor.
     */
    public Document() {
        this.pages = new ArrayList<IPage>();
        this.pages.add(new Page());
        this.pages.add(new Page());
    }

    /** {@inheritDoc} */
    public List<IPage> getPages() {
        return Collections.unmodifiableList(this.pages);
    }

}
