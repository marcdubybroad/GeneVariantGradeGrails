package org.broadinstitute.variantgrade.result;

/**
 * Created by mduby on 12/27/15.
 */
public class ProteinResult {
    // instance variables
    private Integer position;
    private String inputAllele;
    private String referenceAllele;
    private Double heatAmount;
    private String referenceCodon;
    private String scientificAlleleCode;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getInputAllele() {
        return inputAllele;
    }

    public void setInputAllele(String inputAllele) {
        this.inputAllele = inputAllele;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public Double getHeatAmount() {
        return heatAmount;
    }

    public void setHeatAmount(Double heatAmount) {
        this.heatAmount = heatAmount;
    }

    public String getReferenceCodon() {
        return referenceCodon;
    }

    public void setReferenceCodon(String referenceCodon) {
        this.referenceCodon = referenceCodon;
    }

    public String getScientificAlleleCode() {
        return scientificAlleleCode;
    }

    public void setScientificAlleleCode(String scientificAlleleCode) {
        this.scientificAlleleCode = scientificAlleleCode;
    }
}
