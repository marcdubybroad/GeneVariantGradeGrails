package org.broadinstitute.variantgrade.heatmap;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

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
}
