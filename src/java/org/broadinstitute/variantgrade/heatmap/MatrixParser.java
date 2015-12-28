package org.broadinstitute.variantgrade.heatmap;

import org.broadinstitute.variantgrade.bean.CodingRegion;
import org.broadinstitute.variantgrade.bean.CodingSegment;
import org.broadinstitute.variantgrade.bean.Gene;
import org.broadinstitute.variantgrade.bean.GeneRegion;
import org.broadinstitute.variantgrade.bean.PositionHeat;
import org.broadinstitute.variantgrade.util.GradeException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mduby on 12/22/15.
 */
public class MatrixParser {
    // instance variables
    private Map<Integer, PositionHeat> heatMap = new HashMap<Integer, PositionHeat>();
    private InputStream heatMapStream;
    private InputStream geneRegionStream;
    private List<String> referenceLetterList = new ArrayList<String>();
    private boolean isInitialized;
    private Map<String, String> codonToAminoAcidMap = null;
    private Gene gene = null;

    // constants to build maps
    private final String[] codonArray = new String[]{"t", "c", "a", "g"};
    private final String proteinString = "FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG";

    // singleton variable
    private static MatrixParser matrixParser;

    /**
     * singleton method to return parser
     *
     * @return
     */
    public static MatrixParser getMatrixParser() {
        if (matrixParser == null) {
            matrixParser = new MatrixParser();
        }

        return matrixParser;
    }

    public void setHeatMapStream(InputStream heatMapStream) {
        this.heatMapStream = heatMapStream;
    }

    public void setGeneRegionStream(InputStream geneRegionStream) {
        this.geneRegionStream = geneRegionStream;
    }

    /**
     * populates the instance heat map from the given file
     * @throws GradeException
     */
    public void populate() throws GradeException {
        // local variables
        BufferedReader reader = null;
        String[] headerLine = null;
        String[] tempLine = null;
        int count = 0;
        Integer position = null;
        String referenceLetter = null;
        PositionHeat positionHeat;

        // load and parse the file
        String line = "";
        String cvsSplitBy = ",";

        try {
            // read file and parse
            reader = new BufferedReader(new InputStreamReader(this.heatMapStream));

            while ((line = reader.readLine()) != null) {
                // first line is header line
                if (count == 0) {
                    headerLine = line.split(cvsSplitBy);

                    // add in the reference letters to the reference list
                    for (int i = 3; i < headerLine.length; i++) {
                        this.referenceLetterList.add(headerLine[i].substring(1, 2));
                    }

                } else {
                    tempLine = line.split(cvsSplitBy);

                    // no go through line array and create position heat object
                    // index 1 is position number
                    try {
                        position = Integer.parseInt(tempLine[1]);

                    } catch (NumberFormatException exception) {
                        throw new GradeException("Got bad position at line: " + count + " :" + tempLine + " : )" + exception.getMessage());
                    }

                    // index 2 is reference letter
                    referenceLetter = tempLine[2].substring(1, 2);

                    // check data issues
                    if (position == null) {
                        throw new GradeException("Got null position at line: " + count + " : " + tempLine);
                    }

                    if (referenceLetter == null) {
                        throw new GradeException("Got null reference letter at line: " + count + " : " + tempLine);
                    }

                    // loop through rest of array and create heat object
                    positionHeat = new PositionHeat(position, referenceLetter);
                    for (int i = 3; i < headerLine.length; i++) {
                        positionHeat.addHeatEntry(headerLine[i].substring(1, 2), new Double(tempLine[i]));
                    }

                    // if all went well, add to map
                    this.heatMap.put(position, positionHeat);
                }

                // add to count
                count++;
            }

        } catch (FileNotFoundException exception) {
            throw new GradeException("Got file exception reading heat map file: " + exception.getMessage());

        } catch (IOException exception) {
            throw new GradeException("Got IO exception reading heat map file: " + exception.getMessage());

        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException exception) {
                    throw new GradeException("Got reader close exception reading heat map file: " + exception.getMessage());
                }
            }
        }

        // set initialization
        this.isInitialized = true;
    }

    /**
     * get the heat map number for the given position and reference letter
     *
     * @param position
     * @param letter
     * @return
     * @throws GradeException
     */
    public Double getHeatAtPositionAndLetter(int position, String letter) throws GradeException {
        // local variables
        PositionHeat positionHeat;
        Double heatNumber = null;

        // get the position heat
        positionHeat = this.getPositionHeatAtPosition(position);

        // get the heat number
        heatNumber = positionHeat.getHeatNumber(letter);

        // if null, error
        if (heatNumber == null) {
            throw new GradeException("Got null heat number heat for position: " + position + " and letter: " + letter);
        }

        // return
        return heatNumber;
    }

    /**
     * get the postion heat at the position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public PositionHeat getPositionHeatAtPosition(int position) throws GradeException {
        // local variables
        PositionHeat positionHeat;

        // get the position heat
        positionHeat = this.heatMap.get(new Integer(position));

        // make sure exists
        if (positionHeat == null) {
            throw new GradeException("Got null position heat for position: " + position, GradeException.MESSAGE_NOT_PROTEIN_POSITION);
        }

        // return
        return positionHeat;
    }

    /**
     * get the codon to amino acid coding map
     *
     * @return
     * @throws GradeException
     */
    public Map<String, String> getCodonToAminoAcidMap() throws GradeException {
        // if map not built yet, build it
        if (this.codonToAminoAcidMap == null) {
            // create map
            this.codonToAminoAcidMap = new HashMap<String, String>();
            int count = 0;

            // populate the map
            for (int i = 0; i < this.codonArray.length; i++) {
                for (int j = 0; j < this.codonArray.length; j++) {
                    for (int k = 0; k < this.codonArray.length; k++) {
                        this.codonToAminoAcidMap.put((this.codonArray[i] + this.codonArray[j] + this.codonArray[k]), this.proteinString.substring(count, count + 1));
                        count++;
                    }
                }
            }
        }

        // return the map
        return this.codonToAminoAcidMap;
    }

    /**
     * returns the split gene strings
     *
     * @param inputString
     * @return
     * @throws GradeException
     */
    protected String[] getGeneRegionStringArrasFromString(String inputString) throws GradeException {
        // local variables
        String[] splitString;
        String pattern = "\\s+";
        String[] finalArray;

        // make sure correct string
        if (inputString == null) {
            throw new GradeException("Got null string to split for gene");
        }

        // split the string
        splitString = inputString.split(pattern);

        // create final array; cut first 2 elements out
        finalArray = Arrays.copyOfRange(splitString, 2, splitString.length);

        // return
        return finalArray;
    }

    /**
     * parse the gene
     *
     * @return
     * @throws GradeException
     */
    public Gene getGene() throws GradeException {
        // local variables
        List<GeneRegion> regionList = null;

        // initialize list if null
        if (this.gene == null) {
            // build the gene
            this.gene = new Gene("PPARG");

            // if gene region stream is null, throw exception
            if (this.geneRegionStream == null) {
                throw new GradeException("the gene region for the gene: " + this.gene.getName() + " has not been set");
            }

            // get the region list
            regionList = this.parseGeneRegions(this.geneRegionStream);

            // add to the gene
            gene.addAllGeneRegions(regionList);

            // build first coding region by hand
            CodingRegion region = new CodingRegion("Coding region 1");
            region.addCodingSegment(new CodingSegment(68744, 68825));
            region.addCodingSegment(new CodingSegment(96855, 97082));
            region.addCodingSegment(new CodingSegment(98473, 98642));
            region.addCodingSegment(new CodingSegment(109765, 109903));
            region.addCodingSegment(new CodingSegment(123033, 123232));
            region.addCodingSegment(new CodingSegment(133855, 134305));
            region.addCodingSegment(new CodingSegment(151049, 151296));
            this.gene.addCodingRegion(region);

            // build second coding region by hand
            region = new CodingRegion("Coding region 2");
            region.addCodingSegment(new CodingSegment(96857, 97082));
            region.addCodingSegment(new CodingSegment(98473, 98642));
            region.addCodingSegment(new CodingSegment(109765, 109903));
            region.addCodingSegment(new CodingSegment(123033, 123232));
            region.addCodingSegment(new CodingSegment(133855, 134305));
            region.addCodingSegment(new CodingSegment(151049, 151296));
            this.gene.addCodingRegion(region);
        }

        // return
        return this.gene;
    }

    protected List<GeneRegion> parseGeneRegions(InputStream regionStream) throws GradeException {
        // instance variables
        List<GeneRegion> regionList = new ArrayList<GeneRegion>();
        BufferedReader reader = null;
        String line = null;
        GeneRegion region = null;
        StringBuffer buffer = null;
        String[] regionStringArray = null;
        int position = 0;

        // read the input stream
        // read file and parse
        try {
            reader = new BufferedReader(new InputStreamReader(regionStream));

            while ((line = reader.readLine()) != null) {
                // for each line, get string array
                regionStringArray = this.getGeneRegionStringArrasFromString(line);

                // add all the arrays together
                buffer = new StringBuffer();
                for (int i = 0; i < regionStringArray.length; i++) {
                    buffer.append(regionStringArray[i]);
                }

                // for each line, parse the regions
                region = new GeneRegion(position + 1, position + 60, buffer.toString());

                // add to list
                regionList.add(region);
            }

        } catch (IOException exception) {
            throw new GradeException("got exception reading gene regions for gene: " + this.gene.getName() + ": " + exception.getMessage());
        }

        // return
        return regionList;

    }

    /**
     * get the protein reference letter at a given position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public String getProteinReferenceLetterAtPosition(int position) throws GradeException {
        // local variables
        PositionHeat positionHeat = null;

        // get the position heat
        positionHeat = this.getPositionHeatAtPosition(position);

        // if null throw error
        if (positionHeat == null) {
            throw new GradeException("got incorrect position: " + position);
        }

        // get the reference letter
        return positionHeat.getReferenceLetter();
    }

    public Map<Integer, PositionHeat> getHeatMap() {
        return heatMap;
    }

    public List<String> getProteinReferenceLetterList() {
        return referenceLetterList;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public String getProteinString() {
        return proteinString;
    }
}
