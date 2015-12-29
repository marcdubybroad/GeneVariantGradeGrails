package org.broadinstitute.variantgrade.result;

/**
 * Created by mduby on 12/28/15.
 */
public class GeneResult {
    // instance variables
    private String referenceCodon;
    private String newCodon;
    private int proteinPosition;
    private int genePosition;
    private String referenceProteinAllele;
    private String newProteinAllele;
    private Double heatAmount;
    private boolean isProteinCodingPosition;

    /**
     * returns trie if the new codon is a stop codon
     *
     * @return
     */
    public boolean isResultStopCodon() {
        // local variables
        boolean isStop = false;

        if (this.newCodon != null) {
            if ("taa".equals(this.newCodon) || "tag".equals(this.newCodon) || "tga".equals(this.newCodon)) {
                isStop = true;
            }
        }

        // return
        return isStop;
    }

    public String getReferenceCodon() {
        return referenceCodon;
    }

    public void setReferenceCodon(String referenceCodon) {
        this.referenceCodon = referenceCodon;
    }

    public String getNewCodon() {
        return newCodon;
    }

    public void setNewCodon(String newCodon) {
        this.newCodon = newCodon;
    }

    public int getProteinPosition() {
        return proteinPosition;
    }

    public void setProteinPosition(int proteinPosition) {
        this.proteinPosition = proteinPosition;
    }

    public int getGenePosition() {
        return genePosition;
    }

    public void setGenePosition(int genePosition) {
        this.genePosition = genePosition;
    }

    public String getReferenceProteinAllele() {
        return referenceProteinAllele;
    }

    public void setReferenceProteinAllele(String referenceProteinAllele) {
        this.referenceProteinAllele = referenceProteinAllele;
    }

    public String getNewProteinAllele() {
        return newProteinAllele;
    }

    public void setNewProteinAllele(String newProteinAllele) {
        this.newProteinAllele = newProteinAllele;
    }

    public Double getHeatAmount() {
        return heatAmount;
    }

    public void setHeatAmount(Double heatAmount) {
        this.heatAmount = heatAmount;
    }

    public boolean isProteinCodingPosition() {
        return isProteinCodingPosition;
    }

    public void setProteinCodingPosition(boolean isProteinCodingPosition) {
        this.isProteinCodingPosition = isProteinCodingPosition;
    }
}
