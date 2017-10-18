package org.broadinstitute.variantgrade.bean;

import org.broadinstitute.variantgrade.util.GradeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mduby on 12/22/15.
 */
public class PositionMatrixBean {
    // instance variables
    private int position;
    private String referenceLetter;
    private Map<String, Double> heatMap = new HashMap<String, Double>();

    /**
     * default constructor
     *
     * @param position
     * @param reference
     */
    public PositionMatrixBean(int position, String reference) {
        this.position = position;
        this.referenceLetter = reference;
    }

    /**
     * add new heat entry
     *
     * @param referenceLetter
     * @param heatAmount
     * @throws GradeException
     */
    public void addHeatEntry(String referenceLetter, Double heatAmount) throws GradeException {
        // make sure no entry there yet
        if (this.heatMap.get(referenceLetter) != null) {
            throw new GradeException("Got duplicate entry at letter: " + referenceLetter);
        }

        // if not, enter new entry
        this.heatMap.put(referenceLetter, heatAmount);
    }

    /**
     * return the heat number
     *
     * @param referenceLetter
     * @return
     * @throws GradeException
     */
    public Double getHeatNumber(String referenceLetter) throws GradeException {
        return this.heatMap.get(referenceLetter);
    }

    public String getReferenceLetter() {
        return referenceLetter;
    }
}
