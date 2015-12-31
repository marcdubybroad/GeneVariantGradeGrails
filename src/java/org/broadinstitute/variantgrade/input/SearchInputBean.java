package org.broadinstitute.variantgrade.input;

/**
 * Created by mduby on 12/31/15.
 */
public class SearchInputBean {
    // instance variables
    private String inputString;
    private String proteinInputAllele;
    private String proteinReferenceAllele;
    private int proteinPosition;
    private boolean isProteinInput;

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getProteinInputAllele() {
        return proteinInputAllele;
    }

    public void setProteinInputAllele(String proteinInputAllele) {
        this.proteinInputAllele = proteinInputAllele;
    }

    public String getProteinReferenceAllele() {
        return proteinReferenceAllele;
    }

    public void setProteinReferenceAllele(String proteinReferenceAllele) {
        this.proteinReferenceAllele = proteinReferenceAllele;
    }

    public int getProteinPosition() {
        return proteinPosition;
    }

    public void setProteinPosition(int proteinPosition) {
        this.proteinPosition = proteinPosition;
    }

    public boolean isProteinInput() {
        return isProteinInput;
    }

    public void setProteinInput(boolean isProteinInput) {
        this.isProteinInput = isProteinInput;
    }
}
