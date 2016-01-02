package org.broadinstitute.variantgrade.heatmap;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.bean.Gene;
import org.broadinstitute.variantgrade.bean.GeneRegion;
import org.broadinstitute.variantgrade.bean.PositionMatrixBean;
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
        matrixStream = this.getClass().getResourceAsStream("./matrixLogp.csv");
        this.matrixParser.setLogpMapStream(matrixStream);
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
        assertEquals(505, this.matrixParser.getHeatMap().size());
        assertNotNull(this.matrixParser.getLogpMap());
        assertTrue(this.matrixParser.getLogpMap().size() > 0);
        assertEquals(505, this.matrixParser.getLogpMap().size());
    }

    @Test
    public void testGetProteinReferenceAllele() {
        // local variables
        int position = 119;
        String referenceLetter = "K";
        PositionMatrixBean resultHeat = null;

        // get heat amount and test
        try {
            resultHeat = this.matrixParser.getPositionMatrixAtPositionAndType(position, MatrixParser.MATRIX_TYPE_POSITION_HEAT);

        } catch (GradeException exception) {
            fail("got heat exception: " + exception.getMessage());
        }

        // test
        assertNotNull(resultHeat);
        assertEquals(referenceLetter, resultHeat.getReferenceLetter());
    }

    @Test
    public void testGetHeatAmount() {
        // local variables
        int position = 245;
        String referenceLetter = "G";
        Double result = null;

        // get heat amount and test
        try {
            result = this.matrixParser.getMatrixValueAtPositionAndLetterAndType(position, referenceLetter, MatrixParser.MATRIX_TYPE_POSITION_HEAT);

        } catch (GradeException exception) {
            fail("got heat exception: " + exception.getMessage());
        }

        // test
        assertNotNull(result);
        assertEquals(new Double(1.26523134366304), result);
    }

    @Test
    public void testGetLogpAmount() {
        // local variables
        int position = 245;
        String referenceLetter = "G";
        Double result = null;

        // get heat amount and test
        try {
            result = this.matrixParser.getMatrixValueAtPositionAndLetterAndType(position, referenceLetter, MatrixParser.MATRIX_TYPE_POSITION_LOGP);

        } catch (GradeException exception) {
            fail("got heat exception: " + exception.getMessage());
        }

        // test
        assertNotNull(result);
        assertEquals(new Double(18.5031135354918), result);
    }

    @Test
    public void testGetReferenceLetterList() {
        // local variables
        List<String> referenceLetterList = this.matrixParser.getProteinReferenceLetterList();

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
            splitArray = this.matrixParser.getGeneRegionStringArrasFromString(testLine);

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
            splitArray = this.matrixParser.getGeneRegionStringArrasFromString(testLine2);

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

    @Test
    public void testparseGeneRegions() {
        // local variables
        List<GeneRegion> regionList = null;
        InputStream geneStream = null;
        int geneRegionLength = 60;

        // get the input stream
        try {
            // get the stream
            geneStream = this.getClass().getResourceAsStream("./geneRegion.txt");

            // parse the stream regions
            regionList = this.matrixParser.parseGeneRegions(geneStream, geneRegionLength);

        } catch (GradeException exception) {
            fail("got error initializing parser: " + exception.getMessage());
        }

        // test length and content
        assertNotNull(regionList);
        assertTrue(regionList.size() > 0);
        assertEquals(2559, regionList.size());
        for (int i = 0; i < regionList.size(); i++) {
            GeneRegion region = regionList.get(i);
            assertNotNull(region);
            assertNotNull(region.getRegionSequence());
            assertTrue(region.getRegionSequence().length() > 0);
            if (i == regionList.size() - 1) {
                assertEquals(27, region.getRegionSequence().length());
            } else {
                assertEquals(60, region.getRegionSequence().length());
            }
            assertTrue(region.getRegionStart() > 0);
            assertTrue(region.getRegionEnd() > 0);
        }
    }

    @Test
    public void testGetGene() {
        // local variables
        List<GeneRegion> regionList = null;
        InputStream geneStream = null;
        Gene gene = null;

        // get the input stream
        try {
            // get the stream
            geneStream = this.getClass().getResourceAsStream("./geneRegion.txt");
            this.matrixParser.setGeneRegionStream(geneStream);

            // get the gene
            gene = this.matrixParser.getGene();

        } catch (GradeException exception) {
            fail("got error initializing parser: " + exception.getMessage());
        }

        // test
        assertNotNull(gene);
        regionList = gene.getGeneRegionList();
        assertNotNull(regionList);
        assertTrue(regionList.size() > 0);
        assertEquals(2559, regionList.size());
        for (int i = 0; i < regionList.size(); i++) {
            GeneRegion region = regionList.get(i);
            assertNotNull(region);
            assertNotNull(region.getRegionSequence());
            assertTrue(region.getRegionSequence().length() > 0);
            if (i == regionList.size() - 1) {
                assertEquals(27, region.getRegionSequence().length());
            } else {
                assertEquals(60, region.getRegionSequence().length());
            }
            assertTrue(region.getRegionStart() > 0);
            assertTrue(region.getRegionEnd() > 0);
        }
    }

    @Test
    public void testCompareMatrixReferenceToCodingStringTranslation() {
        // local variables
        String proteinMatrixReference = null;
        String genotypeProteinReference = null;
        String codon = null;

        // compare starting from the protein reference string
        for (int i = 1; i <= this.matrixParser.getProteinReferenceLetterList().size(); i++) {
            try {
                // get the codon and translated protein
                codon = this.matrixParser.getGene().getCodonAtProteinPosition(i);
                genotypeProteinReference = this.matrixParser.getCodonToAminoAcidMap().get(codon);

                // get the matrix reference
                proteinMatrixReference = this.matrixParser.getProteinReferenceLetterAtPosition(i);

            } catch (GradeException exception) {
                fail("got error comparing protein sequence at protein position: " + i + " with codon: " + codon + ": " + exception.getMessage());
            }

            // test
            assertNotNull(genotypeProteinReference);
            assertNotNull(proteinMatrixReference);
            if (!proteinMatrixReference.equals(genotypeProteinReference)) {
                fail("at protein position: " + i + " got protein reference: " + genotypeProteinReference + " for expected protein reference: " + proteinMatrixReference + " for codon: " + codon);
            }
        }
    }

    @Test
    public void testGetLogPValue() {
        // local variables
        int position = 245;
        String referenceLetter = "G";
        Double result = null;
        Double probablity = 0.01;

        try {
            // get the logp
            result = this.matrixParser.getLogPForPositionLetterAndProbability(position, referenceLetter, probablity);

        } catch (GradeException exception) {
            fail("got error calculating logp value");
        }

        // test
        assertNotNull(result);
        assertEquals(18.493063199638296, result);
    }

    @Test
    public void testGetPValue() {
        // local variables
        int position = 245;
        String referenceLetter = "G";
        Double result = null;
        Double probablity = 0.01;

        try {
            // get the logp
            result = this.matrixParser.getResultPValueForPositionLetterAndProbability(position, referenceLetter, probablity);

        } catch (GradeException exception) {
            fail("got error calculating pValue");
        }

        // test
        assertNotNull(result);
        assertEquals(0.9486996994900958, result);
    }
}
