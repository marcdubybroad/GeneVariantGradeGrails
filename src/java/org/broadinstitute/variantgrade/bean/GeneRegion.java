package org.broadinstitute.variantgrade.bean;

import org.broadinstitute.variantgrade.util.GradeException;

/**
 * Created by mduby on 12/25/15.
 */
public class GeneRegion {
    // instance variables
    private String regionSequence;
    private int regionStart;
    private int regionEnd;

    /**
     * default constructor
     *
     * @param start
     * @param end
     * @param sequence
     */
    public GeneRegion(int start, int end, String sequence) {
        this.regionStart = start;
        this.regionEnd = end;
        this.regionSequence = sequence;
    }

    /**
     * return the relative position within this gene region
     *
     * @param position
     * @return
     * @throws GradeException
     */
    protected int getRelativePosition(int position) throws GradeException {
        // local variables
        int relativePosition = -1;

        // check to make sure position is good
        if (position < this.regionStart) {
            throw new GradeException("got position: " + position + " that is less than the start position: " + this.regionStart);
        }
        if (position > this.regionEnd) {
            throw new GradeException("got position: " + position + " that is greater than the end position: " + this.regionEnd);
        }

        // get the relative position
        relativePosition = position - this.regionStart + 1;

        // return
        return relativePosition;
    }

    /**
     * returns the reference allele at the given position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getReferenceAtPosition(int position) throws GradeException {
        // local variables
        String reference = null;
        int relativePosition = -1;

        // get the relative position
        relativePosition = this.getRelativePosition(position);

        // get the reference at the position
        reference = this.regionSequence.substring(relativePosition - 1, relativePosition);

        // return
        return reference;
    }

    /**
     * returns the codon for the given position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getCodonAtPosition(int position) throws GradeException {
        // local variables
        String codon = null;
        int relativePosition = -1;
        int moduloPosition;

        // for the position, make sure it is contained in this segment
        if (!isPositionInThisRegion(position)) {
            throw new GradeException("the position given is not in this gene region with start: " + this.regionStart + " and end: " + this.regionEnd);
        }

        // translate position
        relativePosition = this.getRelativePosition(position);

        // get the codon
        moduloPosition = relativePosition % 3;
        if (moduloPosition == 0) {
            // position is at start of codon
            codon = this.regionSequence.substring(relativePosition, relativePosition + 4);

        } else if (moduloPosition == 1) {
            // position is in middle of codon
            codon = this.regionSequence.substring(relativePosition - 1, relativePosition + 3);

        } else {
            // position is at end of codon
            codon = this.regionSequence.substring(relativePosition - 2, relativePosition + 2);
        }

        // return
        return codon;
    }

    /**
     * returns if the given position is in the gene region
     *
     * @param position
     * @return
     */
    public boolean isPositionInThisRegion(int position) {
        // local variables
        boolean isInRegion = false;

        // if in region, set to true
        if ((position >= this.regionStart) && (position <= this.regionEnd)) {
            isInRegion = true;
        }

        // return
        return isInRegion;
    }

    public String getRegionSequence() {
        return regionSequence;
    }

    public int getRegionStart() {
        return regionStart;
    }

    public int getRegionEnd() {
        return regionEnd;
    }
}
