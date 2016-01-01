package org.broadinstitute.variantgrade.bean;

import org.broadinstitute.variantgrade.util.GradeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mduby on 12/25/15.
 */
public class Gene {
    // instance variables
    private String name;
    private List<GeneRegion> geneRegionList = new ArrayList<GeneRegion>();
    private List<CodingRegion> codingRegionList = new ArrayList<CodingRegion>();
    private int geneRegionLength;
    private String codingSequence;

    /**
     * default constructor
     *
      */
    public Gene (String name) {
        this.name = name;
    }

    /**
     * add a gene region
     *
     * @param region
     */
    public void addGeneRegion(GeneRegion region) {
        this.geneRegionList.add(region);
    }

    /**
     * add to a gene coding region
     *
     * @param region
     */
    public void addCodingRegion(CodingRegion region) {
        this.codingRegionList.add(region);
    }

    /**
     * add all gene regions
     *
     * @param regionList
     */
    public void addAllGeneRegions(List<GeneRegion> regionList) {
        this.geneRegionList.addAll(regionList);
    }

    /**
     * checks to see if a position is in one of the gene's coding regions
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public boolean isPositionInCodingRegion(int position) throws GradeException {
        // local variables
        boolean isInCodingRegion = false;

        // loop through regions
        for (CodingRegion region : this.codingRegionList) {
            if (region.isPositionInCodingRegion(position)) {
                isInCodingRegion = true;
                break;
            }
        }

        // return
        return isInCodingRegion;
    }

    /**
     * return the codon at the given position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getCodonAtPosition(int position) throws GradeException {
        // local variables
        String codon;
        int arrayIndex = -1;
        GeneRegion region;

        // find the array index for the region needed
        arrayIndex = (position - 1)/ this.geneRegionLength;
        region = this.geneRegionList.get(arrayIndex);

        // get the codon
        codon = region.getCodonAtPosition(position);

        // return
        return codon;
    }

    /**
     * return the codon at the given position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getNewCodonForAlleleAtPosition(int position, String allele) throws GradeException {
        // local variables
        String codon;
        int arrayIndex = -1;
        GeneRegion region;

        // find the array index for the region needed
        arrayIndex = (position - 1)/ this.geneRegionLength;
        region = this.geneRegionList.get(arrayIndex);

        // get the codon
        codon = region.getNewCodonForAlleleAtPosition(position, allele.toLowerCase());

        // return
        return codon;
    }

    /**
     * returns the converted protein position for the coding region allele
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public int getProteinPositionForCodingRegionAllele(int position) throws GradeException {
        // local variables
        int proteinPosition = -1;

        // loop through coding regions and get the converted protein position
        for (int i = 0; i < this.getCodingRegionList().size(); i++) {
            CodingRegion region = this.getCodingRegionList().get(i);

            if (region.isPositionInCodingRegion(position)) {
                proteinPosition = region.getProteinPositionForCodingRegionAllele(position);
                break;
            }
        }

        // if not found, error
        if (proteinPosition < 0) {
            throw new GradeException("could not find protein position for any of the coding regions for position: " + position);
        }

        // return
        return proteinPosition;
    }

    /**
     * get the codon at the expected protein position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getCodonAtProteinPosition(int position) throws GradeException {
        // local variables
        String codon = null;
        int codonStart = -1;
        int codonEnd = -1;

        // get the codon start and end positions
        codonEnd = position * 3 - 1;
        codonStart = codonEnd - 2;

        // get the codon at that position
        codon = this.getCodingSequence().substring(codonStart, codonEnd + 1);

        // return
        return codon;
    }

    /**
     * returns the coding sequence
     *
     * @return
     */
    public String getCodingSequence() throws GradeException {
        // build the coding sequence if not built already
        if (this.codingSequence == null) {
            StringBuffer buffer = new StringBuffer();
            // build the coding string
            // TODO - (brute force, but whatever, clean up later)
            for (CodingRegion region : this.getCodingRegionList()) {
                for (CodingSegment segment: region.getSegmentList()) {
                    for (int i = segment.getStartPosition(); i<= segment.getEndPosition(); i++) {
                        buffer.append(this.getReferenceAtGenePosition(i));
                    }
                }
            }

            // set the reference coding string
            this.codingSequence = buffer.toString();
        }

        // return
        return this.codingSequence;
    }

    /**
     * returns the reference allele at the gene position given
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getReferenceAtGenePosition(int position) throws GradeException {
        // local variables
        String reference = null;
        int regionIndex = -1;
        GeneRegion region = null;

        // get the coding region
        regionIndex = (position - 1)/ this.getGeneRegionLength();

        // get the gene region
        region = this.getGeneRegionList().get(regionIndex);
        if (region == null) {
            throw new GradeException("position: " + position + " is beyond the gene region");
        }

        // get the reference
        reference = region.getReferenceAtGenePosition(position);

        // return
        return reference;
    }

    public String getName() {
        return name;
    }

    public List<GeneRegion> getGeneRegionList() {
        return geneRegionList;
    }

    public List<CodingRegion> getCodingRegionList() {
        return codingRegionList;
    }

    public int getGeneRegionLength() {
        return geneRegionLength;
    }

    public void setGeneRegionLength(int geneRegionLength) {
        this.geneRegionLength = geneRegionLength;
    }
}
