package org.broadinstitute.variantgrade.input;

/**
 * Created by mduby on 12/31/15.
 */
public class SearchInputBean {
    // instance variables
    private String inputString;

    // protein variables
    private String proteinInputAllele;
    private String proteinReferenceAllele;
    private int proteinPosition;
    private boolean isProteinInput;

    // genotype variables
    private String chromosome;
    private int genePosition;
    private int chromosomePosition;
    private String geneReferenceAllele;
    private String geneInputAllele;

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

    public void setIsProteinInput(boolean isProteinInput) {
        this.isProteinInput = isProteinInput;
    }

    public void setProteinInput(boolean isProteinInput) {
        this.isProteinInput = isProteinInput;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getGenePosition() {
        return genePosition;
    }

    public void setGenePosition(int genePosition) {
        this.genePosition = genePosition;
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

    public int getChromosomePosition() {
        return chromosomePosition;
    }

    public void setChromosomePosition(int chromosomePosition) {
        this.chromosomePosition = chromosomePosition;
    }
}
