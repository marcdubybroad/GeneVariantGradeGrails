package org.broadinstitute.variantgrade.heatmap;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.log4j.Logger;
import org.broadinstitute.variantgrade.bean.AminoAcidBean;
import org.broadinstitute.variantgrade.bean.CodingRegion;
import org.broadinstitute.variantgrade.bean.CodingSegment;
import org.broadinstitute.variantgrade.bean.DiseaseOddsRatio;
import org.broadinstitute.variantgrade.bean.Gene;
import org.broadinstitute.variantgrade.bean.GeneRegion;
import org.broadinstitute.variantgrade.bean.HeatMapBean;
import org.broadinstitute.variantgrade.bean.OddsRatioBean;
import org.broadinstitute.variantgrade.bean.PositionMatrixBean;
import org.broadinstitute.variantgrade.translator.SearchInputTranslator;
import org.broadinstitute.variantgrade.util.GradeException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mduby on 12/22/15.
 */
public class MatrixParser {
    // instance variables
    Logger matrixLogger = Logger.getLogger(this.getClass().getName());
    private Map<Integer, HeatMapBean> heatMapBeanMap = new HashMap<Integer, HeatMapBean>();
    private Map<Integer, PositionMatrixBean> heatMap = new HashMap<Integer, PositionMatrixBean>();
    private Map<Integer, PositionMatrixBean> logpMap = new HashMap<Integer, PositionMatrixBean>();
    private InputStream geneRegionStream;
    private List<String> referenceLetterList = new ArrayList<String>();
    private boolean isInitialized;
    private Map<String, String> codonToAminoAcidMap = null;
    private Gene gene = null;
    private List<AminoAcidBean> proteinList = null;
    private Map<String, AminoAcidBean> proteinMapKeyedOnOneLetterCode = null;
    private Map<String, AminoAcidBean> proteinMapKeyedOnThreeLetterCode = null;
    private Map<Integer, OddsRatioBean> oddsRationOtionMap = null;
    // constants to build maps
    private final String[] codonArray = new String[]{"t", "c", "a", "g"};
    private final String proteinString = "FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG";

    // singleton variable
    private static MatrixParser matrixParser;

    // constants
    public static final int MATRIX_TYPE_POSITION_HEAT_A         = 1;
    public static final int MATRIX_TYPE_POSITION_HEAT_B         = 2;
    public static final int MATRIX_TYPE_POSITION_HEAT_C         = 3;
    public static final int MATRIX_TYPE_POSITION_LOGP           = 4;
    public static final int MATRIX_TYPE_IARC_SOMATIC_COUNT      = 5;
    public static final int MATRIX_TYPE_IARC_GERMLINE_COUNT     = 6;
    public static final int MATRIX_TYPE_EXAC_GERMLINE_COUNT     = 7;
    public static final int MATRIX_TYPE_TRANSC_ACTIVITY_YEAST   = 8;
    public static final int MATRIX_TYPE_MUTATION_PROBABILITY    = 9;

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

    public void setHeatMapStream(Integer type, InputStream heatMapStream) {
        if (this.heatMapBeanMap.get(type) == null) {
            this.heatMapBeanMap.put(type, new HeatMapBean());
        }

        this.heatMapBeanMap.get(type).setHeatMapStream(heatMapStream);
    }

    public void setGeneRegionStream(InputStream geneRegionStream) {
        this.geneRegionStream = geneRegionStream;
    }

    /**
     * returns the protein map with one letter code as lookup key
     *
     * @return
     */
    protected Map<String, AminoAcidBean> getProteinMapKeyedOnOneLetterCode() {
        // if map not built, build it
        if (this.proteinMapKeyedOnOneLetterCode == null) {
            this.proteinMapKeyedOnOneLetterCode = new HashMap<String, AminoAcidBean>();

            // loop through protein list and build
            for (AminoAcidBean bean: this.getProteinList()) {
                this.proteinMapKeyedOnOneLetterCode.put(bean.getCodeOneLetter(), bean);
                this.proteinMapKeyedOnOneLetterCode.put(bean.getCodeOneLetter().toLowerCase(), bean);
            }
        }

        // return
        return this.proteinMapKeyedOnOneLetterCode;
    }

    /**
     * returns the protein map with three letter code as lookup key
     *
     * @return
     */
    protected Map<String, AminoAcidBean> getProteinMapKeyedOnThreeLetterCode() {
        // if map not built, build it
        if (this.proteinMapKeyedOnThreeLetterCode == null) {
            this.proteinMapKeyedOnThreeLetterCode = new HashMap<String, AminoAcidBean>();

            // loop through protein list and build
            for (AminoAcidBean bean: this.getProteinList()) {
                this.proteinMapKeyedOnThreeLetterCode.put(bean.getCodeThreeLetter(), bean);
                this.proteinMapKeyedOnThreeLetterCode.put(bean.getCodeThreeLetter().toLowerCase(), bean);
            }
        }

        // return
        return this.proteinMapKeyedOnThreeLetterCode;
    }

    /**
     * returns the protein bean list
     *
     * @return
     */
    protected List<AminoAcidBean> getProteinList() {
        // look to see if list built already
        if (this.proteinList == null) {
            this.proteinList = new ArrayList<AminoAcidBean>();

            // add in the 20 proteins
            this.proteinList.add(new AminoAcidBean("A", "Ala", "Alanine"));
            this.proteinList.add(new AminoAcidBean("C", "Cys", "Cysteine"));
            this.proteinList.add(new AminoAcidBean("D", "Asp", "Aspartic acid"));
            this.proteinList.add(new AminoAcidBean("E", "Glu", "Glutamic acid"));
            this.proteinList.add(new AminoAcidBean("F", "Phe", "Phenylalanine"));
            this.proteinList.add(new AminoAcidBean("G", "Gly", "Glycine"));
            this.proteinList.add(new AminoAcidBean("H", "His", "Histidine"));
            this.proteinList.add(new AminoAcidBean("K", "Lys", "Lysine"));
            this.proteinList.add(new AminoAcidBean("L", "Leu", "Leucine"));
            this.proteinList.add(new AminoAcidBean("I", "Ile", "Isoleucine"));
            this.proteinList.add(new AminoAcidBean("M", "Met", "Methionine"));
            this.proteinList.add(new AminoAcidBean("N", "Asn", "Asparagine"));
            this.proteinList.add(new AminoAcidBean("P", "Pro", "Proline"));
            this.proteinList.add(new AminoAcidBean("Q", "Gln", "Glutamine"));
            this.proteinList.add(new AminoAcidBean("R", "Arg", "Arginine"));
//            this.proteinList.add(new ProteinBean("R", "Arg", "Arginine"));
            this.proteinList.add(new AminoAcidBean("S", "Ser", "Serine"));
//            this.proteinList.add(new ProteinBean("S", "Ser", "Serine"));
            this.proteinList.add(new AminoAcidBean("T", "Thr", "Threonine"));
            this.proteinList.add(new AminoAcidBean("V", "Val", "Valine"));
            this.proteinList.add(new AminoAcidBean("Y", "Tyr", "Tyrosine"));
            this.proteinList.add(new AminoAcidBean("W", "Trp", "Tryptophan"));

            // add stop codon for cmiter
            this.proteinList.add(new AminoAcidBean("Z", "Ter", "StopCodon"));
        }

        // return
        return this.proteinList;
    }

    /**
     * populates the instance heat map from the given file
     * @throws GradeException
     */
    public synchronized void populate() throws GradeException {
        if (!this.isInitialized) {
            // populate the heat matrix
            Iterator<Integer> matrixTypeIterator = this.heatMapBeanMap.keySet().iterator();
            while (matrixTypeIterator.hasNext()) {
                Integer matrixType = matrixTypeIterator.next();

                this.matrixLogger.info("populating matrix of type: " + matrixType);

                this.populateMatrix(matrixType);
            }
//            this.populateMatrix(MATRIX_TYPE_POSITION_HEAT_A);
//            this.populateMatrix(MATRIX_TYPE_POSITION_HEAT_B);
//            this.populateMatrix(MATRIX_TYPE_POSITION_HEAT_C);

            // populate the logp matrix
//            this.populateMatrix(MATRIX_TYPE_POSITION_LOGP);

            // set initialization
            this.isInitialized = true;
        }
    }

    /**
     * returns trie if the new codon is a stop codon
     *
     * @return
     */
    public boolean isResultStopCodon(String codon) {
        // local variables
        boolean isStop = false;

        if (codon != null) {
            if ("taa".equals(codon) || "tag".equals(codon) || "tga".equals(codon)) {
                isStop = true;
            }
        }

        // return
        return isStop;
    }

    /**
     * populate the matrix given
     *
     * @param matrixType
     * @throws GradeException
     */
    protected void populateMatrix(int matrixType) throws GradeException {
        // local variables
        BufferedReader reader = null;
        String[] headerLine = null;
        String[] tempLine = null;
        int count = 0;
        Integer position = null;
        String referenceLetter = null;
        PositionMatrixBean positionMatrixBean;
        Map<Integer, PositionMatrixBean> mapToPopulate = null;
        InputStream inputStream = null;
        HeatMapBean heatMapBean = null;

        // load and parse the file
        String line = "";
        String cvsSplitBy = ",";

        try {
            // read file and parse
            heatMapBean = this.heatMapBeanMap.get(matrixType);
            if (heatMapBean == null) {
                throw new GradeException("Heat map bean is null for map type: " + matrixType);
            }

            inputStream = heatMapBean.getHeatMapStream();
            if (inputStream == null) {
                throw new GradeException("Heat map stream is null for map type: " + matrixType);
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            mapToPopulate = heatMapBean.getHeatMap();

            while ((line = reader.readLine()) != null) {
                // first line is header line
                if (count == 0) {
                    headerLine = line.split(cvsSplitBy);

                    // add in the reference letters to the reference list
                    // only use the heat map for the reference letters
                    if (matrixType == MATRIX_TYPE_POSITION_HEAT_A) {
                        for (int i = 2; i < headerLine.length; i++) {
                            // cmiter change (no quotes)
//                            String tempString = headerLine[i].substring(1, 2);
                            String tempString = headerLine[i];
                            this.referenceLetterList.add(tempString);
                        }
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
                    // cmiter change (no quotes)
//                    referenceLetter = tempLine[0].substring(1, 2);
                    referenceLetter = tempLine[0];

                    // check data issues
                    if (position == null) {
                        throw new GradeException("Got null position at line: " + count + " : " + tempLine);
                    }

                    if (referenceLetter == null) {
                        throw new GradeException("Got null reference letter at line: " + count + " : " + tempLine);
                    }

                    // loop through rest of array and create heat object
                    positionMatrixBean = new PositionMatrixBean(position, referenceLetter);
                    for (int i = 2; i < headerLine.length; i++) {
                        // cmiter change (no quotes)
//                        positionMatrixBean.addHeatEntry(headerLine[i].substring(1, 2), new Double(tempLine[i]));
                        if (headerLine[i] == null) {
                            throw new GradeException("Got error in header line for type: " + matrixType + " and position: " + i);

                        } else if ((tempLine[i] == null) || (tempLine[i].trim().length() < 1)) {
                            positionMatrixBean.addHeatEntry(headerLine[i], new Double(0));

                        } else {
                            try {
                                positionMatrixBean.addHeatEntry(headerLine[i], new Double(tempLine[i]));

                            } catch (NumberFormatException exception) {
                                positionMatrixBean.addHeatEntry(headerLine[i], null);
                            }
                        }
                    }

                    // if all went well, add to map
                    mapToPopulate.put(position, positionMatrixBean);
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

            // null out heatmap stream for space
            if (heatMapBean != null) {
                heatMapBean.setHeatMapStream(null);
            }
        }
    }

    /**
     * get the logp value for the given position and letter
     *
     * @param position
     * @param letter
     * @param prevalance
     * @return
     * @throws GradeException
     */
    public Double getLogPForPositionLetterAndProbability(int position, String letter, Double prevalance) throws GradeException {
        // local variables
        Double returnLog = null;
        Double logP = null;
        Double oddsRatio;

        // get the logp value
        logP = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MATRIX_TYPE_POSITION_LOGP, false);

        // calculate the odds ration
        oddsRatio = (1 - prevalance) / prevalance;

        // calculate the logP value
        returnLog = logP + Math.log(oddsRatio);

        // this is log (B : p)
        // if > 0,,
        // return
        return returnLog;
    }

    public Double getDiseaseOddsForPositionLetterAndPrevalence(int position, String letter, Double prevalance) throws GradeException {
        // local variables
        Double logp = null;
        Double odds = null;
        Double exponentinalTemp = null;

        // get the logp with the prevalence included
        logp = this.getLogPForPositionLetterAndProbability(position, letter, prevalance);

        // exponentiate and subtract from 1
        exponentinalTemp = Math.exp(logp);
        odds = exponentinalTemp / (exponentinalTemp + 1);
        odds = 1 - odds;

        //return
        return odds;
    }

    /**
     * get the odds of disease risk given a position, letter and prevalence
     *
     * @param position
     * @param letter
     * @param prevalence
     * @return
     * @throws GradeException
     */
    public DiseaseOddsRatio getOddOfDiseaseNumber(int position, String letter, Double prevalence) throws GradeException {
        // local variables
        DiseaseOddsRatio oddsRatio = new DiseaseOddsRatio();
        Double logP;
        Double expP;

        // get the logp value
        logP = this.getLogPForPositionLetterAndProbability(position, letter, prevalence);

        // get the odds ratio
        oddsRatio = this.getOddOfDiseaseNumber(logP);

        // return
        return oddsRatio;
    }

    /**
     * get the odds of disease based on a log value
     *
     * @param logP
     * @return
     * @throws GradeException
     */
    public DiseaseOddsRatio getOddOfDiseaseNumber(double logP) throws GradeException {
        // local variables
        DiseaseOddsRatio oddsRatio = new DiseaseOddsRatio();
        Double expP;

        // if positive, use exp() as benign odds value
        if (logP > 0) {
            expP = Math.exp(logP);
            oddsRatio.setBenignOdds(expP.intValue());

            // if not, use number as deleterious odds value
        } else {
            expP = Math.exp(logP);
            oddsRatio.setDeleteriousOdds(expP.intValue());
        }

        // return
        return oddsRatio;
    }

    /**
     * returns the pValue of a result
     *
     * @param position
     * @param letter
     * @param probability
     * @return
     * @throws GradeException
     */
    public Double getResultPValueForPositionLetterAndProbability(int position, String letter, Double probability) throws GradeException {
        // local variables
        Double pValue;
        Double logp;

        // get the logp
        logp = this.getLogPForPositionLetterAndProbability(position, letter, probability);

        // calculate the pValue
        pValue = Math.exp(logp) / (Math.exp(logp) + 1);

        // return
        return pValue;
    }

    /**
     * return the odds ratio option map
     *
     * @return
     */
    public Map<Integer, OddsRatioBean> getOddsRatioOptionsMap() {
        // check to see if map already built
        if (this.oddsRationOtionMap == null) {
            this.oddsRationOtionMap = new HashMap<Integer, OddsRatioBean>();
            this.oddsRationOtionMap.put(1, new OddsRatioBean(1, "1:10", 0.1));
            this.oddsRationOtionMap.put(2, new OddsRatioBean(2, "1:100", 0.01));
            this.oddsRationOtionMap.put(3, new OddsRatioBean(3, "1:1000", 0.001));
        }

        // return
        return this.oddsRationOtionMap;
    }

    /**
     * get the heat map number for the given position and reference letter
     *
     * @param position
     * @param letter
     * @return
     * @throws GradeException
     */
    public Double getMatrixValueAtPositionAndLetterAndType(int position, String letter, int matrixType, boolean isNullOk) throws GradeException {
        // local variables
        PositionMatrixBean positionMatrixBean;
        Double heatNumber = null;

        // get the position heat
        positionMatrixBean = this.getPositionMatrixAtPositionAndType(position, matrixType);

        // get the heat number
        heatNumber = positionMatrixBean.getHeatNumber(letter);

        // if null, error
        if (!isNullOk && (heatNumber == null)) {
            throw new GradeException("Got null heat number heat for position: " + position + " and letter: " + letter);
        }

        // return
        return heatNumber;
    }

    /**
     * returns the type 2 diabetes risk string
     *
     * @param position
     * @param letter
     * @return
     * @throws GradeException
     */
    public String getType2DiabetesRiskAtPositionAndLetterAndType(int position, String letter) throws GradeException {
        // local variables
        Double heatNumber = null;
        String diabetesRiskString = "";

        // get the position heat
        heatNumber = this.getFunctionalScoreAtPositionAndLetter(position, letter);

        // if null, error
        if (heatNumber == null) {
            throw new GradeException("Got null heat number heat for position: " + position + " and letter: " + letter);
        }

        // get the diabetes risk string
        if (heatNumber < -0.252) {
            diabetesRiskString = "6.5-fold increased risk";
        } else if (heatNumber > 0.998) {
            diabetesRiskString = "no increased risk";
        } else {
            diabetesRiskString = "2-fold increased risk";
        }

        // return
        return diabetesRiskString;
    }

    /**
     * central method to return the functional score
     * <br/>
     * modify this method if changes to score calculation are made
     *
     * @param position
     * @param letter
     * @return
     * @throws GradeException
     */
    public Double getFunctionalScoreAtPositionAndLetter(Integer position, String letter) throws GradeException {
        // local variables
        Double heatNumber = null;
        String diabetesRiskString = "";
        Double firstDouble = null;
        Double secondDouble = null;
        Double thirdDouble = null;

        // get the numbers
        firstDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_A, false);
        secondDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_B, false);
        thirdDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_C, false);

        // get the position heat
//        heatNumber = firstDouble + secondDouble - thirdDouble;
        heatNumber = this.getCancerGradeFunctionScore(firstDouble, secondDouble, thirdDouble);

        // if null, error
        if (heatNumber == null) {
            throw new GradeException("Got null heat number heat for position: " + position + " and letter: " + letter);
        }

        // return
        return heatNumber;
    }

    /**
     * central method to return the functional score standard deviation
     * <br/>
     * modify this method if changes to score calculation are made
     *
     * @param position
     * @param letter
     * @return
     * @throws GradeException
     */
    public Double getFunctionalScoreStandardDeviationAtPositionAndLetter(Integer position, String letter) throws GradeException {
        // local variables
        Double heatNumber = null;
        String diabetesRiskString = "";
        Double firstDouble = null;
        Double secondDouble = null;
        Double thirdDouble = null;

        // get the numbers
        firstDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_A, false);
        secondDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_B, false);
        thirdDouble = this.getMatrixValueAtPositionAndLetterAndType(position, letter, MatrixParser.MATRIX_TYPE_POSITION_HEAT_C, false);

        // get the position heat
//        heatNumber = firstDouble + secondDouble - thirdDouble;
        heatNumber = this.getCancerGradeFunctionScoreStandardDev(firstDouble, secondDouble, thirdDouble);

        // if null, error
        if (heatNumber == null) {
            throw new GradeException("Got null heat number heat standard deviation for position: " + position + " and letter: " + letter);
        }

        // return
        return heatNumber;
    }

    /**
     * returns mutant p53 application experimental function score
     *
     * @param zScore1
     * @param zScore2
     * @param zScore3
     * @return
     */
    protected Double getCancerGradeFunctionScore(Double zScore1, Double zScore2, Double zScore3) {
        Double result = null;

        // get the result
        result = (zScore1 + zScore2 - zScore3)/new Double(3);

        // return
        return result;
    }

    /**
     * returns mutant p53 application experimental function score standard deviation
     *
     * @param zScore1
     * @param zScore2
     * @param zScore3
     * @return
     */
    protected Double getCancerGradeFunctionScoreStandardDev(Double zScore1, Double zScore2, Double zScore3) {
        Double result = null;
        double[] standard = {zScore1.doubleValue(), zScore2.doubleValue(), -1.0 * zScore3.doubleValue()};
        StandardDeviation standardDeviation = new StandardDeviation();

        // get the result
        result = standardDeviation.evaluate(standard) / Math.sqrt(3.0);

        // return
        return result;
    }



    /**
     * get the position heat at the position
     *
     * @param position
     * @return
     * @throws GradeException
     */
    public PositionMatrixBean getPositionMatrixAtPositionAndType(int position, int matrixType) throws GradeException {
        // local variables
        PositionMatrixBean positionMatrixBean;
        Map<Integer, PositionMatrixBean> heatMap = null;
        HeatMapBean heatMapBean = null;

        // get the heat map bean
        heatMapBean = this.heatMapBeanMap.get(matrixType);
        if (heatMapBean == null) {
            throw new GradeException("Got null heat map bean for get position matrix for type: " + matrixType);
        }

        // get the heat map
        heatMap = heatMapBean.getHeatMap();
        if (heatMapBean == null) {
            throw new GradeException("Got null heat map for get position matrix for type: " + matrixType);
        }

        // get the position heat
        positionMatrixBean = heatMap.get(new Integer(position));

        // make sure exists
        if (positionMatrixBean == null) {
            throw new GradeException("Got null position heat for position: " + position, GradeException.MESSAGE_NOT_PROTEIN_POSITION);
        }

        // return
        return positionMatrixBean;
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
            this.gene = new Gene("PPARG", SearchInputTranslator.GENE_CHROMOSOME_OFFSET);
            this.gene.setGeneRegionLength(60);

            // if gene region stream is null, throw exception
            if (this.geneRegionStream == null) {
                throw new GradeException("the gene region for the gene: " + this.gene.getName() + " has not been set");
            }

            // get the region list
            regionList = this.parseGeneRegions(this.geneRegionStream, this.gene.getGeneRegionLength());

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

            /* skip this coding region for now
            // build second coding region by hand
            region = new CodingRegion("Coding region 2");
            region.addCodingSegment(new CodingSegment(96857, 97082));
            region.addCodingSegment(new CodingSegment(98473, 98642));
            region.addCodingSegment(new CodingSegment(109765, 109903));
            region.addCodingSegment(new CodingSegment(123033, 123232));
            region.addCodingSegment(new CodingSegment(133855, 134305));
            region.addCodingSegment(new CodingSegment(151049, 151296));
            this.gene.addCodingRegion(region);
            */
        }

        // return
        return this.gene;
    }

    protected List<GeneRegion> parseGeneRegions(InputStream regionStream, int geneRegionLength) throws GradeException {
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
                region = new GeneRegion(position + 1, position + geneRegionLength, buffer.toString());

                // add to list
                regionList.add(region);

                // increment position
                position = position + geneRegionLength;
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
        PositionMatrixBean positionMatrixBean = null;

        // get the position heat
        positionMatrixBean = this.getPositionMatrixAtPositionAndType(position, MATRIX_TYPE_POSITION_HEAT_A);

        // if null throw error
        if (positionMatrixBean == null) {
            throw new GradeException("got incorrect position: " + position);
        }

        // get the reference letter
        return positionMatrixBean.getReferenceLetter();
    }

    /**
     * returns the protein one letter code given the three letter code
     *
     * @param code
     * @return
     * @throws GradeException
     */
    public String getOneLetterProteinCodeFromThreeLetterCode(String code) throws GradeException {
        // local variables
        AminoAcidBean protein = null;

        // get the code
        protein = this.getProteinMapKeyedOnThreeLetterCode().get(code);

        // test if null
        if (protein == null) {
            throw new GradeException("Got incorrect three letter protein code for lookup: " + code);
        }

        // return
        return protein.getCodeOneLetter();
    }

    /**
     * returns the protein one letter code given either the one or three letter code
     *
     * @param code
     * @return
     * @throws GradeException
     */
    public String getOneLetterProteinCodeFromOneOrThreeLetterCode(String code) throws GradeException {
        // local variables
        AminoAcidBean protein = null;

        // look for the code in the one letter map
        protein = this.getProteinFromOneOrThreeLetterCode(code);

        // return
        return protein.getCodeOneLetter();
    }

    /**
     * return the protein name from the given 1 or 3 letter code
     *
     * @param code
     * @return
     * @throws GradeException
     */
    public String getProteinNameFromOneOrThreeLetterCode(String code) throws GradeException {
        // local variables
        AminoAcidBean protein = null;

        // look for the code in the one letter map
        protein = this.getProteinFromOneOrThreeLetterCode(code);

        // return
        return protein.getName();
    }

    /**
     * get the protein for the given 1 or 3 letter code
     *
     * @param code
     * @return
     * @throws GradeException
     */
    protected AminoAcidBean getProteinFromOneOrThreeLetterCode(String code) throws GradeException {
        // local variables
        AminoAcidBean protein = null;

        // look for the code in the one letter map
        protein = this.getProteinMapKeyedOnOneLetterCode().get(code);

        // if null, look in the 3 letter map
        if (protein == null) {
            protein = this.getProteinMapKeyedOnThreeLetterCode().get(code);
        }

        // test if null
        if (protein == null) {
            throw new GradeException("Got incorrect one or three letter protein code for lookup: " + code);
        }

        // return
        return protein;
    }

    /**
     * returns the protein three letter code given the one letter code
     *
     * @param code
     * @return
     * @throws GradeException
     */
    public String getThreeLetterProteinCodeFromOneLetterCode(String code) throws GradeException {
        // local variables
        AminoAcidBean protein = null;

        // get the code
        protein = this.getProteinMapKeyedOnOneLetterCode().get(code);

        // test if null
        if (protein == null) {
            throw new GradeException("Got incorrect one letter protein code for lookup: " + code);
        }

        // return
        return protein.getCodeThreeLetter();
    }

    public Map<Integer, PositionMatrixBean> getHeatMap() {
        return heatMap;
    }

    public Map<Integer, PositionMatrixBean> getLogpMap() {
        return logpMap;
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
