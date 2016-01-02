package org.broadinstitute.variantgrade.bean;

import org.broadinstitute.variantgrade.util.GradeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mduby on 12/25/15.
 */
public class CodingRegion {
    // instance variables
    private List<CodingSegment> segmentList = new ArrayList<CodingSegment>();
    private String name;

    public CodingRegion(String name) {
        this.name = name;
    }

    public void addCodingSegment(CodingSegment segment) {
        this.segmentList.add(segment);
    }

    /**
     * returns true if the position is in this coding region's segments; false otherwise
     *
     * @param position
     * @return
     */
    public boolean isPositionInCodingRegion(int position) {
        // local variables
        boolean isInRegion = false;

        // loop through segments and see if position in any
        for (int i = 0; i < this.segmentList.size(); i++) {
            CodingSegment segment = this.segmentList.get(i);

            if ((position >= segment.getStartPosition()) && (position <= segment.getEndPosition())) {
                isInRegion = true;
                break;
            }
        }

        // return
        return isInRegion;
    }

    /**
     * get the protein position from the gene coding position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public int getProteinPositionForCodingRegionAllele(int position) throws GradeException {
        // local variables
        int proteinCodingPosition = -1;
        int tempAllelePosition = 0;

        // make sure in this coding region
        if (!this.isPositionInCodingRegion(position)) {
            throw new GradeException("the position: " + position + " is not in this coding region");
        }

        // go through all coding regions as necessary until get protein position
        for (int i = 0; i < this.getSegmentList().size(); i++) {
            // get the segment
            CodingSegment segment = this.getSegmentList().get(i);

            // if the position is in the segment, then calculate where
            if (segment.isPositionInSegment(position)) {
                // calculate the protein position from there
                tempAllelePosition = tempAllelePosition + 1 + (position - segment.getStartPosition());
                break;

            } else {
                tempAllelePosition = tempAllelePosition + 1 + (segment.getEndPosition() - segment.getStartPosition());
            }
        }

        // now convert to protein position
        proteinCodingPosition = (((tempAllelePosition -1)/ 3) + 1);

        // return
        return proteinCodingPosition;
    }

    /**
     * translates the gene position to the coding sequence position
     *
     * @param genePosition
     * @return
     * @throws GradeException
     */
    public int translateGenePositionToCodingSequencePosition(int genePosition) throws GradeException {
        // local variables
        int codingSequencePosition = 0;

        // check that in coding regions
        if (!this.isPositionInCodingRegion(genePosition)) {
            throw new GradeException("position: " + genePosition + " is not in the protein coding region");
        }

        // loop though the coding regions and add the positions
        for (CodingSegment segment: this.segmentList) {
            if (!segment.isPositionInSegment(genePosition)) {
                codingSequencePosition = codingSequencePosition + segment.getEndPosition() - segment.getStartPosition() + 1;
            } else {
                codingSequencePosition = codingSequencePosition + genePosition - segment.getStartPosition() + 1;
                break;
            }
        }

        // return
        return codingSequencePosition;
    }

    public List<CodingSegment> getSegmentList() {
        return segmentList;
    }

    public String getName() {
        return name;
    }
}
