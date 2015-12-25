package org.broadinstitute.variantgrade.util;

/**
 * Created by mduby on 12/22/15.
 */
public class GradeException extends Exception {
    // instance variables
    private String webMessage = null;

    // constants
    public static final String MESSAGE_NO_CDS_EFFECT        = "No transcript effect";
    public static final String MESSAGE_NO_PROTEIN_EFFECT    = "No protein effect";
    public static final String MESSAGE_SYSTEM_ERROR         = "System error";

    public GradeException(String message) {
        super(message);
        this.webMessage = MESSAGE_SYSTEM_ERROR;
    }

    public GradeException(String message, String webMessage) {
        super(message);
        this.webMessage = webMessage;
    }

    public String getWebMessage() {
        return webMessage;
    }
}