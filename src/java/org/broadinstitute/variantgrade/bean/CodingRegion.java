package org.broadinstitute.variantgrade.bean;

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

    public List<CodingSegment> getSegmentList() {
        return segmentList;
    }

    public String getName() {
        return name;
    }
}
