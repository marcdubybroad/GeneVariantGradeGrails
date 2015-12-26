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
        relativePosition = position - this.regionStart - 1;

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
}
