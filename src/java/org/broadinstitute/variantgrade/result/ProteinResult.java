package org.broadinstitute.variantgrade.result;

import org.broadinstitute.variantgrade.bean.DiseaseOddsRatio;
import org.broadinstitute.variantgrade.bean.OddsRatioBean;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by mduby on 12/27/15.
 */
public class ProteinResult {
    // instance variables
    private Integer position;
    private String inputAllele;
    private String referenceAllele;
    private Double heatAmount;
    private Double heatAmountStandardDeviation;
    private String referenceCodon;
    private String alternateCodon;
    private String scientificAlleleCode;
    private String geneReferenceAllele;
    private String geneInputAllele;
    private int genePosition;
    private int chromosomePosition;
    private OddsRatioBean inputOddsRatio;
    private Double logP;
    private Double pValue;
    private Double inputPrevalence;
    private Double oddsRatioOfDisease;
    private DiseaseOddsRatio diseaseOddsRatio;
    private String diabetesRiskString;
    private String aminoAcidReference;
    private String aminoAcidAlllele;

    // cmiter specific properties
    private String somaticIarcMutationCount;
    private String germlineIarcMutationCount;
    private String germlineExacMutationCount;
    private String transcriptionalActivityYeastPercent;
    private String mutationProbability;

    // constants
    public static final String EFFECT_BENIGN_STRING             = "not pathogenic";
    public static final String EFFECT_DELETERIOUS_STRING        = "pathogenic";
    public static final String EFFECT_NEUTRAL_STRING            = "neutral";
    public static final String NOT_AVAILABLE                    = "NA";

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

    public Double getHeatAmountStandardDeviation() {
        return heatAmountStandardDeviation;
    }

    public void setHeatAmountStandardDeviation(Double heatAmountStandardDeviation) {
        this.heatAmountStandardDeviation = heatAmountStandardDeviation;
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

    public int getChromosomePosition() {
        return chromosomePosition;
    }

    public void setChromosomePosition(int chromosomePosition) {
        this.chromosomePosition = chromosomePosition;
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
            return "chr3:" + this.getChromosomePosition() + "_" + this.getGeneReferenceAllele() + "/" + this.getGeneInputAllele();
        } else {
            return null;
        }
    }

    public String getDiabetesRiskString() {
        return diabetesRiskString;
    }

    public void setDiabetesRiskString(String diabetesRiskString) {
        this.diabetesRiskString = diabetesRiskString;
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

    public Double getOddsRatioOfDisease() {
        return oddsRatioOfDisease;
    }

    public void setOddsRatioOfDisease(Double oddsRatioOfDisease) {
        this.oddsRatioOfDisease = oddsRatioOfDisease;
    }

    public String getAminoAcidReference() {
        return aminoAcidReference;
    }

    public void setAminoAcidReference(String aminoAcidReference) {
        this.aminoAcidReference = aminoAcidReference;
    }

    public String getAminoAcidAlllele() {
        return aminoAcidAlllele;
    }

    public void setAminoAcidAlllele(String aminoAcidAlllele) {
        this.aminoAcidAlllele = aminoAcidAlllele;
    }

    public String getSomaticIarcMutationCount() {
        return somaticIarcMutationCount;
    }

    public void setSomaticIarcMutationCount(Double somIarcMutationCount) {
        if (somIarcMutationCount == null) {
            this.somaticIarcMutationCount = NOT_AVAILABLE;

        } else {
            this.somaticIarcMutationCount = String.valueOf(somIarcMutationCount.intValue());
        }
    }

    public String getGermlineIarcMutationCount() {
        return this.germlineIarcMutationCount;
    }

    public void setGermlineIarcMutationCount(Double germIarcMutationCount) {
        if (germIarcMutationCount == null) {
            this.germlineIarcMutationCount = NOT_AVAILABLE;

        } else {
            this.germlineIarcMutationCount = String.valueOf(germIarcMutationCount.intValue());
        }
    }

    public String getGermlineExacMutationCount() {
        return this.germlineExacMutationCount;
    }

    public void setGermlineExacMutationCount(Double germExacMutationCount) {
        if (germExacMutationCount == null) {
            this.germlineExacMutationCount = NOT_AVAILABLE;

        } else {
            this.germlineExacMutationCount = String.valueOf(germExacMutationCount.intValue());
        }
    }

    public String getTranscriptionalActivityYeastPercent() {
        return transcriptionalActivityYeastPercent;
    }

    public void setTranscriptionalActivityYeastPercent(Double transcripActivityYeastPercent) {
        if (transcripActivityYeastPercent == null) {
            this.transcriptionalActivityYeastPercent = NOT_AVAILABLE;

        } else {
            this.transcriptionalActivityYeastPercent = String.valueOf(transcripActivityYeastPercent) + "%";
        }
    }

    public String getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(Double mutationProbabilityPercent) {
        if (mutationProbabilityPercent == null) {
            this.mutationProbability = NOT_AVAILABLE;

        } else {
            this.mutationProbability = String.valueOf(mutationProbabilityPercent) + "%";
        }
    }

    public String getOddsRatioOfDiseaseString() {
        // local variables
        String oddsRatioString = "";

        // get the string
        if (this.getOddsRatioOfDisease() != null) {
            if (this.getOddsRatioOfDisease() < 0.001) {
                oddsRatioString = "less than 0.1%";
            } else if (this.getOddsRatioOfDisease() > 0.999) {
                oddsRatioString = "more than 99.9%";
            } else {
                NumberFormat formatter = new DecimalFormat("##.#%");
                oddsRatioString = formatter.format(this.getOddsRatioOfDisease());
            }
        }

        // return
        return oddsRatioString;
    }

    public void setpValue(Double pValue) {
        this.pValue = pValue;
    }

    public String getEffect() {
        // local variables
        String effect = ProteinResult.EFFECT_NEUTRAL_STRING;

        if (this.getInputPrevalence() != null) {
            if (this.getInputPrevalence() <= 0) {
                effect = ProteinResult.EFFECT_DELETERIOUS_STRING;
            } else if (this.getInputPrevalence() >= 1) {
                effect = ProteinResult.EFFECT_BENIGN_STRING;
            }
        }

        // set according to logp value
        // only set if not already set as deleterious/benign
        if ((ProteinResult.EFFECT_NEUTRAL_STRING.equalsIgnoreCase(effect)) && (this.logP != null)) {
            if (this.logP < 0.0) {
                effect = ProteinResult.EFFECT_DELETERIOUS_STRING;
            } else {
                effect = ProteinResult.EFFECT_BENIGN_STRING;
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

    public Double getInputPrevalence() {
        return inputPrevalence;
    }

    public void setInputPrevalence(Double inputPrevalence) {
        this.inputPrevalence = inputPrevalence;
    }

    public void setDiseaseOddsRatio(DiseaseOddsRatio diseaseOddsRatio) {
        this.diseaseOddsRatio = diseaseOddsRatio;
    }

    /**
     * returns the odds disease ratio in string format
     *
     * @return
     */
    public String getOddsOfCausingDisease() {
        // local variables
        String diseaseOdds = "";

        // if not null odds, create string
        if (this.diseaseOddsRatio != null) {
            diseaseOdds = this.diseaseOddsRatio.getBenignOdds() + ":" + this.diseaseOddsRatio.getDeleteriousOdds();
        }

        // return
        return diseaseOdds;
    }

    public String getPValueClinicalScientificNotation() {
        // local variables
        String pValueScientific = "";
        Double tempDouble = null;

        // check to make sure proper prevalence was entered
        if (this.getInputPrevalence() != null) {
            if ((this.getInputPrevalence() <= 0) || (this.getInputPrevalence() >= 1)) {
                return "";
            }
        }

        // transform the p value
        if (this.getpValue() != null) {
            /*
            // if benign, then return 1 - pValue
            if (this.getEffect().equalsIgnoreCase(ProteinResult.EFFECT_BENIGN_STRING)) {
                tempDouble = 1.0 - this.getpValue();

            } else {
                tempDouble = this.getpValue();
            }
            */

            // transform
            tempDouble = this.getpValue();
//            NumberFormat formatter = new DecimalFormat("0E0");
            NumberFormat formatter = new DecimalFormat("#.######");
            pValueScientific = formatter.format(tempDouble);
            pValueScientific = String.valueOf(tempDouble);
        }

        // return
        return pValueScientific;
    }
}
