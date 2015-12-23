package org.broadinstitute.variantgrade.heatmap;

import org.broadinstitute.variantgrade.bean.PositionHeat;
import org.broadinstitute.variantgrade.util.GradeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mduby on 12/22/15.
 */
public class MatrixParser {
    // instance variables
    Map<Integer, PositionHeat> heatMap = new HashMap<Integer, PositionHeat>();
    File heatMapFile;

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

    public void setHeatMapFile(File heatMapFile) {
        this.heatMapFile = heatMapFile;
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
            reader = new BufferedReader(new FileReader(this.heatMapFile));

            while ((line = reader.readLine()) != null) {
                // first line is header line
                if (count == 0) {
                    headerLine = line.split(cvsSplitBy);
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
                        positionHeat.addHeatEntry(headerLine[i], new Double(tempLine[i]));
                    }

                    // if all went well, add to map
                    this.heatMap.put(position, positionHeat);
                }
                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                System.out.println("Country [code= " + country[4]
                        + " , name=" + country[5] + "]");

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
            throw new GradeException("Got null position heat for position: " + position);
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
}
