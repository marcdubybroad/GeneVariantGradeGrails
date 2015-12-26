package org.broadinstitute.variantgrade.heatmap;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by mduby on 12/22/15.
 */
public class MatrixParserTest extends TestCase {
    // local variables
    MatrixParser matrixParser;

    @Before
    public void setUp() throws Exception {
        // set the json builder
        this.matrixParser = MatrixParser.getMatrixParser();
        InputStream matrixStream = this.getClass().getResourceAsStream("./matrixHeat.csv");
        this.matrixParser.setHeatMapStream(matrixStream);
        try {
            this.matrixParser.populate();
        } catch (GradeException exception) {
            fail("got error initializing parser: " + exception.getMessage());
        }
    }

    @Test
    public void testSetup() {
            assertNotNull(this.matrixParser);
            assertNotNull(this.matrixParser.getHeatMap());
            assertTrue(this.matrixParser.getHeatMap().size() > 0);
    }

    @Test
    public void testGetHeatAmount() {
        // local variables
        int position = 245;
        String referenceLetter = "G";
        Double result = null;

        // get heat amount and test
        try {
            result = this.matrixParser.getHeatAtPositionAndLetter(position, referenceLetter);

        } catch (GradeException exception) {
            fail("got heat exception: " + exception.getMessage());
        }

        // test
        assertNotNull(result);
        assertEquals(new Double(0.130984994891909), result);
    }

    @Test
    public void testGetReferenceLetterList() {
        // local variables
        List<String> referenceLetterList = this.matrixParser.getReferenceLetterList();

        // test
        assertNotNull(referenceLetterList);
        assertTrue(referenceLetterList.size() > 0);
        assertEquals(20, referenceLetterList.size());
    }

    @Test
    public void testGetProteinString() {
        // local variables
        String proteinString;

        // get the string
        proteinString = this.matrixParser.getProteinString();

        // test
        assertNotNull(proteinString);
        assertEquals(64, proteinString.length());
    }

    @Test
    public void testGetCodonToAminoAcidMap() {
        // local variables
        Map<String, String> codonMap = null;

        // get the map
        try {
            codonMap = this.matrixParser.getCodonToAminoAcidMap();

        } catch (GradeException exception) {
            fail("got error building codon translation map: " + exception.getMessage());
        }

        // test
        assertNotNull(codonMap);
        assertEquals(64, codonMap.size());
        assertEquals("Q", codonMap.get("caa"));
    }

    @Test
    public void testGetStringArrays() {
        // local variables
        String[] splitArray = null;
        String testLine = "      181 atttcataac tagacataaa aatcacaagt gggaacatgt cagagtcatt caaaaagtag\n";
        String testLine2 = "   153481 ctcttttttt ttcttccttg atgccaa\n";

        // get the string arrays
        try {
            splitArray = this.matrixParser.getStringArraysFromString(testLine);

        } catch (GradeException exception) {
            fail("got error splitting gene string: " + exception.getMessage());
        }

        // test
        assertNotNull(splitArray);
        assertTrue(splitArray.length > 0);
        assertEquals(6, splitArray.length);
        for (int i = 0; i < splitArray.length; i++) {
            assertNotNull(splitArray[i]);
            assertEquals(10, splitArray[i].length());
        }

        // get the string arrays
        try {
            splitArray = this.matrixParser.getStringArraysFromString(testLine2);

        } catch (GradeException exception) {
            fail("got error splitting gene string: " + exception.getMessage());
        }

        // test again
        assertNotNull(splitArray);
        assertTrue(splitArray.length > 0);
        assertEquals(3, splitArray.length);
        for (int i = 0; i < splitArray.length; i++) {
            assertNotNull(splitArray[i]);
            if (i == splitArray.length - 1) {
                assertEquals(7, splitArray[i].length());
            } else {
                assertEquals(10, splitArray[i].length());
            }
        }
    }
}
