package org.broadinstitute.variantgrade.heatmap;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

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
    }

    @Test
    public void testSetup() {
        try {
            assertNotNull(this.matrixParser);
            this.matrixParser.populate();

            assertNotNull(this.matrixParser.getHeatMap());
            assertTrue(this.matrixParser.getHeatMap().size() > 0);

        } catch (GradeException exception) {
            fail("got error initializing parser: " + exception.getMessage());
        }
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

}
