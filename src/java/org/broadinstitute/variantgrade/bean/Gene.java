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
        arrayIndex = position / 60;
        region = this.geneRegionList.get(arrayIndex);

        // get the codon
        codon = region.getCodonAtPosition(position);

        // return
        return codon;
    }

    public String getName() {
        return name;
    }

    public List<GeneRegion> getGeneRegionList() {
        return geneRegionList;
    }
}
