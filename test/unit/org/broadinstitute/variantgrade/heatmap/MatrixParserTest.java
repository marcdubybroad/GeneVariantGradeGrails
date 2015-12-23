package org.broadinstitute.variantgrade.heatmap;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
        File matrixFile = this.getClass().getrenew File("./matrixHeat.csv");
        this.matrixParser.setHeatMapFile(matrixFile);
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


}
