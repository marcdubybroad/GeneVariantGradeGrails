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
    public String getReferenceAtGenePosition(int position) throws GradeException {
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
        int codonStartPosition = -1;
        int codonEndPostion = -1;

        // for the position, make sure it is contained in this segment
        if (!isPositionInThisRegion(position)) {
            throw new GradeException("the position given is not in this gene region with start: " + this.regionStart + " and end: " + this.regionEnd);
        }

        // translate position
        relativePosition = this.getRelativePosition(position);

        // get the codon
        moduloPosition = relativePosition % 3;
        if (moduloPosition == 1) {
            // position is at start of codon
            codonStartPosition = relativePosition;
            codonEndPostion = relativePosition + 2;

        } else if (moduloPosition == 2) {
            // position is in middle of codon
            codonStartPosition = relativePosition - 1;
            codonEndPostion = relativePosition + 1;

        } else {
            // position is at end of codon
            codonStartPosition = relativePosition - 2;
            codonEndPostion = relativePosition;
        }

        // get the codon
        // remember that relative positions are 1 based array, but strings are 0 besed arrays
        if (codonEndPostion == this.regionSequence.length()) {
            codon = this.regionSequence.substring(codonStartPosition - 1);
        } else {
            codon = this.regionSequence.substring(codonStartPosition - 1, codonEndPostion);
        }

        // return
        return codon;
    }

    /**
     * returns the new codon for an allele at a position
     *
     * @param position
     * @param allele
     * @return
     * @throws GradeException
     */
    public String getNewCodonForAlleleAtPosition(int position, String allele) throws GradeException {
        // local variables
        String codon = null;
        int modulo = -1;
        String newCodon = null;

        // get the codon at the position
        codon = this.getCodonAtPosition(position);

        // based on the modulo, replace the appropriate letter
        modulo = position % 3;
        if (modulo == 1) {
            newCodon = allele + codon.substring(1);

        } else if (modulo == 2) {
            newCodon = codon.substring(0, 1) + allele + codon.substring(2);

        } else {
            newCodon = codon.substring(0, 2) + allele;
        }

        // return
        return newCodon;
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
