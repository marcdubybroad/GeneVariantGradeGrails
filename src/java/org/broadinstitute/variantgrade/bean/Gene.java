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
        codon = region.getNewCodonForAlleleAtPosition(position, allele);

        // return
        return codon;
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
