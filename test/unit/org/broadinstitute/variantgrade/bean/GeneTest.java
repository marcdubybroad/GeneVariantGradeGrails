package org.broadinstitute.variantgrade.bean;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mduby on 12/28/15.
 */
public class GeneTest extends TestCase {
    // instance variables
    private Gene gene;

    @Before
    public void setUp() throws Exception {
        // set the gene
        this.gene = new Gene("test gene");
        this.gene.setGeneRegionLength(12);
        this.gene.addGeneRegion(new GeneRegion(1, 12, "aaabbbcccddd"));
        this.gene.addGeneRegion(new GeneRegion(13, 24, "eeefffggghhh"));
        this.gene.addGeneRegion(new GeneRegion(25, 27, "iii"));
        CodingRegion codingRegion = new CodingRegion("region 1");
        codingRegion.addCodingSegment(new CodingSegment(4, 9));             // coding region sequence is bbbccceee
        codingRegion.addCodingSegment(new CodingSegment(13, 15));
        this.gene.addCodingRegion(codingRegion);
    }

    @Test
    public void testgetProteinPositionForCodingRegionAllele() {
        // local variables
        int proteinPosition = -1;
        int allelePosition = -1;

        // first part of codon
        allelePosition = 4;
        try {
            // get the protein position
            proteinPosition = this.gene.getProteinPositionForCodingRegionAllele(allelePosition);

        } catch (GradeException exception) {
            fail("got error getting protein position for allele position: " + allelePosition + ": " + exception.getMessage());
        }

        // test
        assertTrue(proteinPosition > 0);
        assertEquals(1, proteinPosition);

        // last part of codon
        allelePosition = 9;
        try {
            // get the protein position
            proteinPosition = this.gene.getProteinPositionForCodingRegionAllele(allelePosition);

        } catch (GradeException exception) {
            fail("got error getting protein position for allele position: " + allelePosition + ": " + exception.getMessage());
        }

        // test
        assertTrue(proteinPosition > 0);
        assertEquals(2, proteinPosition);

        // first part of second region codon
        allelePosition = 14;
        try {
            // get the protein position
            proteinPosition = this.gene.getProteinPositionForCodingRegionAllele(allelePosition);

        } catch (GradeException exception) {
            fail("got error getting protein position for allele position: " + allelePosition + ": " + exception.getMessage());
        }


        // test
        assertTrue(proteinPosition > 0);
        assertEquals(3, proteinPosition);
    }

    @Test
    public void testIsPositionInCodingRegion() {
        // local variables
        boolean isCodingRegion = false;
        int positionTrue = 5;
        int positionFalse = 16;

        try {
            // get the region check
            isCodingRegion = this.gene.isPositionInCodingRegion(positionTrue);

        } catch (GradeException exception) {
            fail("got coding postion check error: " + exception.getMessage());
        }

        // test
        assertTrue(isCodingRegion);

        try {
            // get the region check
            isCodingRegion = this.gene.isPositionInCodingRegion(positionFalse);

        } catch (GradeException exception) {
            fail("got coding postion check error: " + exception.getMessage());
        }

        // test
        assertTrue(!isCodingRegion);
    }

    @Test
    public void testGetCodonAtPosition() {
        // local variables
        String codon = null;
        String codonStart = "bbb";
        int positionStart = 4;
        String codonMiddle = "ggg";
        int positionMiddle = 20;
        String codonEnd = "hhh";
        int positionEnd = 24;

        try {
            // get the codon
            codon = this.gene.getCodonAtPosition(positionStart);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonStart, codon);

        try {
            // get the codon
            codon = this.gene.getCodonAtPosition(positionMiddle);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonMiddle, codon);

        try {
            // get the codon
            codon = this.gene.getCodonAtPosition(positionEnd);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonEnd, codon);

    }

    @Test
    public void testGetNewCodonForAlleleAtPosition() {
        // local variables
        String codon = null;
        String codonStart = "cbb";
        int positionStart = 4;
        String codonMiddle = "gcg";
        int positionMiddle = 20;
        String codonEnd = "hhc";
        int positionEnd = 24;
        String allele = "c";

        try {
            // get the codon
            codon = this.gene.getNewCodonForAlleleAtPosition(positionStart, allele);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonStart, codon);

        try {
            // get the codon
            codon = this.gene.getNewCodonForAlleleAtPosition(positionMiddle, allele);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonMiddle, codon);

        try {
            // get the codon
            codon = this.gene.getNewCodonForAlleleAtPosition(positionEnd, allele);

        } catch (GradeException exception) {
            fail("got codon retrieval exception: " + exception.getMessage());
        }

        // test
        assertNotNull(codon);
        assertEquals(codonEnd, codon);

    }
}
