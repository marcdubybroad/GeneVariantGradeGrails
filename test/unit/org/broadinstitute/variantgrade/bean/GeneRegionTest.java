package org.broadinstitute.variantgrade.bean;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mduby on 12/28/15.
 */
public class GeneRegionTest extends TestCase {
    // instance variables
    GeneRegion geneRegion;

    @Before
    public void setUp() throws Exception {
        this.geneRegion = new GeneRegion(25, 36, "aaatttcccggg");
    }

    @Test
    public void testGetRelativePosition() {
        // local variables
        int relativePosition = -1;

        try {
            // get the relative position
            relativePosition = this.geneRegion.getRelativePosition(25);

        } catch (GradeException exception) {
            fail("got error getting relative position: " + exception.getMessage());
        }

        // test
        assertEquals(1, relativePosition);

        try {
            // get the relative position
            relativePosition = this.geneRegion.getRelativePosition(28);

        } catch (GradeException exception) {
            fail("got error getting relative position: " + exception.getMessage());
        }

        // test
        assertEquals(4, relativePosition);

        try {
            // get the relative position
            relativePosition = this.geneRegion.getRelativePosition(35);

        } catch (GradeException exception) {
            fail("got error getting relative position: " + exception.getMessage());
        }

        // test
        assertEquals(11, relativePosition);

        try {
            // get the relative position
            relativePosition = this.geneRegion.getRelativePosition(36);

        } catch (GradeException exception) {
            fail("got error getting relative position: " + exception.getMessage());
        }

        // test
        assertEquals(12, relativePosition);
    }

    @Test
    public void testGetCodonAtPosition() {
        // local variables
        int position = -1;
        String expectedCodon = null;
        String resultCodon = null;

        // get the codon
        position = 25;
        expectedCodon = "aaa";
        try {
            resultCodon = this.geneRegion.getCodonAtPosition(position);

        } catch (GradeException exception) {
            fail("got error retrieving codon at position: " + exception.getMessage());
        }

        // test
        assertNotNull(resultCodon);
        assertEquals(expectedCodon, resultCodon);

        // get the codon
        position = 35;
        expectedCodon = "ggg";
        try {
            resultCodon = this.geneRegion.getCodonAtPosition(position);

        } catch (GradeException exception) {
            fail("got error retrieving codon at position: " + exception.getMessage());
        }

        // test
        assertNotNull(resultCodon);
        assertEquals(expectedCodon, resultCodon);

        // get the codon
        position = 36;
        expectedCodon = "ggg";
        try {
            resultCodon = this.geneRegion.getCodonAtPosition(position);

        } catch (GradeException exception) {
            fail("got error retrieving codon at position: " + exception.getMessage());
        }

        // test
        assertNotNull(resultCodon);
        assertEquals(expectedCodon, resultCodon);

    }

}
