package org.broadinstitute.variantgrade.translator;

import org.broadinstitute.variantgrade.heatmap.MatrixParser;
import org.broadinstitute.variantgrade.input.SearchInputBean;
import org.broadinstitute.variantgrade.util.GradeException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (input != null) {
            this.inputString = input.trim();
        } else {
            this.inputString = input;
        }
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
        String chromosome = null;

        try {
            // parse the string
            splitString = this.inputString.split("-");

            // make sure correct number of arguments
            if (splitString.length < 4) {
                throw new GradeException("got improperly formatted search input string: " + this.inputString);
            }

            // check the chromosome
            chromosome = splitString[0];
            if (!"chr3".equalsIgnoreCase(chromosome)) {
                throw new GradeException("got incorrect chromosome: " + chromosome, "got incorrect chromosome: " + chromosome);
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
        List<String> parsedStringList = null;

        try {
            // lowercase the string
            tempString = this.inputString.toLowerCase();

            // cut out the p.
            tempString = tempString.substring(2);

            // split the string
            parsedStringList = this.parseProteinChangeString(tempString);

            // make sure the parsed string has only 3 segments
            if (parsedStringList.size() != 3) {
                throw new GradeException("got incorrect protein change string");
            }

            // get the reference allele
//            referenceAllele = tempString.substring(0, 3);
            referenceAllele = parsedStringList.get(0);
            bean.setProteinReferenceAllele(this.matrixParser.getOneLetterProteinCodeFromOneOrThreeLetterCode(referenceAllele));

            // get the alternate allele
//            alternateAllele = tempString.substring(tempString.length() - 3);
            alternateAllele = parsedStringList.get(2);
            bean.setProteinInputAllele(this.matrixParser.getOneLetterProteinCodeFromOneOrThreeLetterCode(alternateAllele));

            // get the position
            try {
//                position = Integer.valueOf(tempString.substring(3, tempString.length() - 3));
                String positionString = parsedStringList.get(1);
                position = Integer.valueOf(positionString);
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

    private List<String> parseProteinChangeString(String str) {
        List<String> output = new ArrayList<String>();
        Matcher match = Pattern.compile("[0-9]+|[a-z]+|[A-Z]+").matcher(str);
        while (match.find()) {
            output.add(match.group());
        }
        return output;
    }

}
