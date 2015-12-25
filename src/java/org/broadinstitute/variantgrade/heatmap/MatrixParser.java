package org.broadinstitute.variantgrade.heatmap;

import org.broadinstitute.variantgrade.bean.PositionHeat;
import org.broadinstitute.variantgrade.util.GradeException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private List<String> referenceLetterList = new ArrayList<String>();
    private boolean isInitialized;

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
                    referenceLetter = tempLine[2];

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
        positionHeat = this.heatMap.get(new Integer(position));

        // make sure exists
        if (positionHeat == null) {
            throw new GradeException("Got null position heat for position: " + position, GradeException.MESSAGE_NOT_PROTEIN_POSITION);
        }

        // get the heat number
        heatNumber = positionHeat.getHeatNumber(letter);

        // if null, error
        if (heatNumber == null) {
            throw new GradeException("Got null heat number heat for position: " + position + " and letter: " + letter);
        }

        // return
        return heatNumber;
    }

    public Map<Integer, PositionHeat> getHeatMap() {
        return heatMap;
    }

    public List<String> getReferenceLetterList() {
        return referenceLetterList;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
