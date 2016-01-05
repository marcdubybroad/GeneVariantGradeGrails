package org.broadinstitute.variantgrade.translator;

import org.broadinstitute.variantgrade.heatmap.MatrixParser;
import org.broadinstitute.variantgrade.input.SearchInputBean;
import org.broadinstitute.variantgrade.util.GradeException;

/**
 * Created by mduby on 12/31/15.
 */
public class SearchInputTranslator {
    // instance variables
    private String inputString;
    MatrixParser matrixParser = MatrixParser.getMatrixParser();

    // constants
    // TODO - move this to gene based data location; this should be passed in to the translator, or at least the SearchInputBean
    public static final int GENE_CHROMOSOME_OFFSET           = 12324348;

    /**
     * default constructor
     *
     * @param input
     */
    public SearchInputTranslator(String input) {
        this.inputString = input;
    }

    /**
     * translate the search input
     *
     * @return
     * @throws GradeException
     */
    public SearchInputBean translate() throws GradeException {
        // local variables
        SearchInputBean bean = null;

        // translate
        if (this.inputString == null) {
            throw new GradeException("got incorrect null input string");

        } else if (this.inputString.length() == 0) {
            throw new GradeException("got incorrect empty input string");

        } else if (this.inputString.substring(0, 1).equals("p")) {
            // got protein input
            bean = this.parseProteinInput();

        } else if (this.inputString.substring(0, 3).equals("chr")) {
            // got protein input
            bean = this.parseGenotypeInput();

        } else {
            throw new GradeException("got incorrect input string: " + this.inputString, "Incorrect input string: " + this.inputString);
        }

        // return
        return bean;
    }

    /**
     * parse the genotype string
     *
     * @return
     * @throws GradeException
     */
    protected SearchInputBean parseGenotypeInput() throws GradeException {
        // local variables
        SearchInputBean bean = new SearchInputBean();
        String tempString = null;
        int position;
        String referenceAllele;
        String alternateAllele;
        String[] splitString;

        try {
            // parse the string
            splitString = this.inputString.split("-");

            // make sure correct number of arguments
            if (splitString.length < 4) {
                throw new GradeException("got improperly formatted search input string: " + this.inputString);
            }

            // set the bean parameters
            bean.setGeneReferenceAllele(splitString[2].toLowerCase());
            bean.setGeneInputAllele(splitString[3].toLowerCase());

            // set the position
            try {
                bean.setChromosomePosition(Integer.valueOf(splitString[1]));
                bean.setGenePosition(Integer.valueOf(splitString[1]) - GENE_CHROMOSOME_OFFSET);
                bean.setIsProteinInput(false);

            } catch (NumberFormatException exception) {
                throw new GradeException("got input gene position error for input: " + exception.getMessage());
            }

        } catch (GradeException exception) {
            throw new GradeException("got error with input string: " + this.inputString + ": " + exception.getMessage(), "Incorrect variant input string: " + this.inputString);
        }

        // return
        return bean;
    }

    /**
     * return the parsed protein search input
     *
     * @return
     * @throws GradeException
     */
    protected SearchInputBean parseProteinInput() throws GradeException {
        // local variables
        SearchInputBean bean = new SearchInputBean();
        String tempString = null;
        int position;
        String referenceAllele;
        String alternateAllele;

        try {
            // cut out the p.
            tempString = this.inputString.substring(2);

            // get the reference allele
            referenceAllele = tempString.substring(0, 3);
            bean.setProteinReferenceAllele(this.matrixParser.getOneLetterProteinCodeFromThreeLetterCode(referenceAllele));

            // get the alternate allele
            alternateAllele = tempString.substring(tempString.length() - 3);
            bean.setProteinInputAllele(this.matrixParser.getOneLetterProteinCodeFromThreeLetterCode(alternateAllele));

            // get the position
            try {
                position = Integer.valueOf(tempString.substring(3, tempString.length() - 3));
                bean.setProteinPosition(position);
                bean.setIsProteinInput(true);

            } catch (NumberFormatException exception) {
                throw new GradeException("got input protein position error for input: " + exception.getMessage());
            }

        } catch (GradeException exception) {
            throw new GradeException("got error with input string: " + this.inputString + ": " + exception.getMessage(), "Incorrect protein change input string: " + this.inputString);
        }

        // return
        return bean;
    }

}
