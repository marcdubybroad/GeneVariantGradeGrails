package org.broadinstitute.variantgrade.result;

import org.broadinstitute.variantgrade.bean.OddsRatioBean;

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
    private String alternateCodon;
    private String scientificAlleleCode;
    private String geneReferenceAllele;
    private String geneInputAllele;
    private int genePosition;
    private OddsRatioBean inputOddsRatio;
    private Double logP;
    private Double pValue;

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

    public String getGeneReferenceAllele() {
        return geneReferenceAllele;
    }

    public void setGeneReferenceAllele(String geneReferenceAllele) {
        this.geneReferenceAllele = geneReferenceAllele;
    }

    public String getGeneInputAllele() {
        return geneInputAllele;
    }

    public void setGeneInputAllele(String geneInputAllele) {
        this.geneInputAllele = geneInputAllele;
    }

    public int getGenePosition() {
        return genePosition;
    }

    public void setGenePosition(int genePosition) {
        this.genePosition = genePosition;
    }

    public String getVariantDisplay() {
        if ((this.getGeneInputAllele() != null) && (this.getGenePosition() != -1) && (this.getGeneReferenceAllele() != null)) {
            return "chr3:" + this.getGenePosition() + "_" + this.getGeneReferenceAllele() + "/" + this.getGeneInputAllele();
        } else {
            return null;
        }
    }

    public String getAlternateCodon() {
        return alternateCodon;
    }

    public void setAlternateCodon(String alternateCodon) {
        this.alternateCodon = alternateCodon;
    }

    public OddsRatioBean getInputOddsRatio() {
        return inputOddsRatio;
    }

    public void setInputOddsRatio(OddsRatioBean inputOddsRatio) {
        this.inputOddsRatio = inputOddsRatio;
    }

    public Double getLogP() {
        return logP;
    }

    public void setLogP(Double logP) {
        this.logP = logP;
    }

    public Double getpValue() {
        return pValue;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public String getEffect() {
        // local variables
        String effect = "neutral";

        // set according to logp value
        if (this.logP != null) {
            if (this.logP < 1.0) {
                effect = "deleterious";
            } else {
                effect = "benign";
            }
        }

        // return
        return effect;
    }

    /**
     * returns trie if the new codon is a stop codon
     *
     * @return
     */
    public boolean isResultStopCodon() {
        // local variables
        boolean isStop = false;

        if (this.alternateCodon != null) {
            if ("taa".equals(this.alternateCodon) || "tag".equals(this.alternateCodon) || "tga".equals(this.alternateCodon)) {
                isStop = true;
            }
        }

        // return
        return isStop;
    }
}
