package org.broadinstitute.variantgrade.bean;

/**
 * Created by mduby on 12/25/15.
 */
public class CodingSegment {
    // instance variables
    private int startPosition;
    private int endPosition;

    public CodingSegment(int start, int end) {
        this.startPosition = start;
        this.endPosition = end;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }
}
