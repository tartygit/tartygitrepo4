package com.cth.sdm.handler;

import java.io.File;

public interface DocumentHandler {

    /**
     * Returns the handler identifier/name (e.g., DEFAULT_HANDLER, XML_HANDLER)
     */
    String getHandlerName();

    /**
     * Determines whether this handler can process the given file
     */
    boolean supports(File file);

    /**
     * Processes the file picked up from watched folder
     */
    void process(File file);
}
