package org.broadinstitute.variantgrade.bean;

/**
 * Created by mduby on 1/23/16.
 */
public class DiseaseOddsRatio {
    // instance variables
    private int benignOdds = 1;
    private int deleteriousOdds = 1;


    public int getBenignOdds() {
        return benignOdds;
    }

    public void setBenignOdds(int benignOdds) {
        this.benignOdds = benignOdds;
    }

    public int getDeleteriousOdds() {
        return deleteriousOdds;
    }

    public void setDeleteriousOdds(int deleteriousOdds) {
        this.deleteriousOdds = deleteriousOdds;
    }
}
