package org.broadinstitute.variantgrade
import grails.transaction.Transactional
import org.broadinstitute.variantgrade.bean.Gene
import org.broadinstitute.variantgrade.heatmap.MatrixParser
import org.broadinstitute.variantgrade.input.SearchInputBean
import org.broadinstitute.variantgrade.result.GeneResult
import org.broadinstitute.variantgrade.result.ProteinResult
import org.broadinstitute.variantgrade.translator.SearchInputTranslator
import org.broadinstitute.variantgrade.util.GradeException

@Transactional
class HeatMapService {
    // instance variables
    MatrixParser matrixParser = null;

    def serviceMethod() {

    }

    /**
     * return the map result given a search string
     *
     * @param searchString
     * @return
     * @throws GradeException
     */
    public ProteinResult getHeatMapReadingFromSearchString(String searchString, String prevalence) throws GradeException {
        // local variables
        SearchInputBean inputBean = null;
        SearchInputTranslator translator = new SearchInputTranslator(searchString);
        ProteinResult resultBean;
        BigDecimal prevalenceDecimal = null;

        // translate the bean
        inputBean = translator.translate();

        // get the double of prevalence
        try {
            prevalenceDecimal = new BigDecimal(prevalence);

        } catch (NumberFormatException exception) {
            throw new GradeException("got number format exception for prevalence: " + exception.getMessage(), "Incorrect prevalence input: " + prevalence);
        }

        if (inputBean.isProteinInput()) {
            // log
            log.info("calling protein heat map for position: " + inputBean.getProteinPosition() + " and input allele: " + inputBean.getProteinInputAllele());

            // call
            resultBean =  this.getHeatMapReadingFromProtein(inputBean.getProteinPosition(), inputBean.getProteinInputAllele(), prevalenceDecimal.doubleValue(), false);

        } else {
            resultBean =  this.getHeatMapReadingFromVariant(inputBean.getGenePosition(), inputBean.getGeneInputAllele(), prevalenceDecimal.doubleValue());

        }

        // set the prevalence
        resultBean.setInputPrevalence(prevalenceDecimal.doubleValue());

        // return
        return resultBean;
    }

    /**
     * return result from variant allele input
     *
     * @param variantPosition
     * @param alternateAllele
     * @return
     * @throws GradeException
     */
    public ProteinResult getHeatMapReadingFromVariant(int variantPosition, String alternateAllele, Double prevalence) throws GradeException {
        // local variables
        int proteinPosition;
        String proteinAllele;
        String newCodon;
        ProteinResult proteinResult;

        // get the protein position
        try {
            proteinPosition = this.matrixParser.getGene().getProteinPositionForCodingRegionAllele(variantPosition);

        } catch (GradeException exception) {
            throw new GradeException(exception.getMessage(), "The variant position " + (variantPosition + SearchInputTranslator.GENE_CHROMOSOME_OFFSET) + " is not in the protein coding region");
        }

        // get the new protein allele
        newCodon = this.matrixParser.getGene().getNewCodingCodonForAlelleAtCodingPosition(variantPosition, alternateAllele);
        proteinAllele = this.matrixParser.getCodonToAminoAcidMap().get(newCodon);

        // log
        log.info("calling variant heat map for position: " + variantPosition + " and variant allele: " + alternateAllele + " and new codon: " + newCodon);

        // call the protein function
        proteinResult = this.getHeatMapReadingFromProtein(proteinPosition, proteinAllele, prevalence, this.getMatrixParser().isResultStopCodon(newCodon));

        // add in the variant data
        proteinResult.setAlternateCodon(newCodon);
        proteinResult.setGenePosition(variantPosition);
        proteinResult.setGeneInputAllele(alternateAllele.toLowerCase());
        proteinResult.setGeneReferenceAllele(this.matrixParser.getGene().getReferenceAtGenePosition(variantPosition));
        proteinResult.setChromosomePosition(variantPosition + this.matrixParser.getGene().getChromosomePosition());

        // return
        return proteinResult;
    }

    /**
     * get the heat map from protein map
     *
     * @param position
     * @param referenceAllele
     * @return
     * @throws GradeException
     */
    public ProteinResult getHeatMapReadingFromProtein(int position, String allele, Double prevalence, boolean isStopCodon) throws GradeException {
        // local variables
        Double proteinGrade = null;
        ProteinResult result = new ProteinResult();
        String referenceAllele = null;
        String referenceCodon = null;
        Double tempDouble = null;
        Double diseaseOddsRatio;
        String diabetesRiskString = null;

        // log
        log.info("got heat map reading call for protein position: " + position + " and allele: " + allele + "and prevalence: " + prevalence + " and stop codon: " + isStopCodon);

        // first make sure prevalence is between 0 and 1
        if ((prevalence <= 0) || (prevalence >= 1)) {
            throw new GradeException("Got incorrect prevalence number: " + prevalence, "The prevalence inputted should be greater than 0 and less than 1");
        }

        // if not a stop codon
        if (!isStopCodon) {
            // log
            log.info("got non stop codon");

            // get the map amount
            proteinGrade = this.getMatrixParser().getFunctionalScoreAtPositionAndLetter(position, allele);
            result.setHeatAmount(proteinGrade);

            // set the diabetes risk string
            diabetesRiskString = this.getMatrixParser().getType2DiabetesRiskAtPositionAndLetterAndType(position, allele);
            result.setDiabetesRiskString(diabetesRiskString);

            // set the logp values
            tempDouble = this.getMatrixParser().getLogPForPositionLetterAndProbability(position, allele, prevalence);
            result.setLogP(tempDouble);

            // set the pValue
            tempDouble = this.getMatrixParser().getResultPValueForPositionLetterAndProbability(position, allele, prevalence);
            result.setpValue(tempDouble);

            // set the odds ratio
            diseaseOddsRatio = this.getMatrixParser().getDiseaseOddsForPositionLetterAndPrevalence(position, allele, prevalence);
            result.setOddsRatioOfDisease(diseaseOddsRatio)
        }

        // get the reference allele
        referenceAllele = this.getMatrixParser().getProteinReferenceLetterAtPosition(position);

        // make sure modified protein allele is different from reference allele
        if (allele.equalsIgnoreCase(referenceAllele)) {
            String aminoAcidName = this.getMatrixParser().getProteinNameFromOneOrThreeLetterCode(allele);
            throw new GradeException("Protein allele: " + allele + " is the same as the reference allele", "At protein position " + position + ", the reference amino acid " + aminoAcidName + " does not have an experimental function score.");
        }

        // get the reference gene codon
        referenceCodon = this.getMatrixParser().getGene().getCodonAtProteinPosition(position);

        // set the result
        result.setInputAllele(allele);
        result.setPosition(position);
        result.setReferenceAllele(referenceAllele);
        result.setReferenceCodon(referenceCodon);

        // set the scientific code
        result.setAminoAcidReference(this.matrixParser.getThreeLetterProteinCodeFromOneLetterCode(result.getReferenceAllele()));
        result.setAminoAcidAlllele(this.matrixParser.getThreeLetterProteinCodeFromOneLetterCode(result.getInputAllele()));
        result.setScientificAlleleCode("p." + this.matrixParser.getThreeLetterProteinCodeFromOneLetterCode(result.getReferenceAllele()) + result.getPosition() + this.matrixParser.getThreeLetterProteinCodeFromOneLetterCode(result.getInputAllele()));

        // return
        return result;
    }

    /**
     * get and initialize the heat map parser
     *
     * @return
     */
    public MatrixParser getMatrixParser() throws GradeException {
        // initialize if not already
        if (this.matrixParser == null) {
            this.matrixParser = MatrixParser.getMatrixParser();

            // get the file stream and load it to the parser
            InputStream heatMapStreamA = this.class.classLoader.getResourceAsStream('matrixHeatWT_Nutlin.csv');
            this.matrixParser.setHeatMapStream(MatrixParser.MATRIX_TYPE_POSITION_HEAT_A, heatMapStreamA);

            InputStream heatMapStreamB = this.class.classLoader.getResourceAsStream('matrixHeatNULL_Nutlin.csv');
            this.matrixParser.setHeatMapStream(MatrixParser.MATRIX_TYPE_POSITION_HEAT_B, heatMapStreamB);

            InputStream heatMapStreamC = this.class.classLoader.getResourceAsStream('matrixHeatNULL_Etoposide.csv');
            this.matrixParser.setHeatMapStream(MatrixParser.MATRIX_TYPE_POSITION_HEAT_C, heatMapStreamC);

            // set the logp stream
            InputStream logpStream = this.class.classLoader.getResourceAsStream('matrixHeatNULL_Etoposide.csv');
            this.matrixParser.setHeatMapStream(MatrixParser.MATRIX_TYPE_POSITION_LOGP, logpStream);
            this.matrixParser.populate();

            // set the gene file stream
            InputStream geneStream = this.class.classLoader.getResourceAsStream('geneRegion.txt');
            this.matrixParser.setGeneRegionStream(geneStream);
        }

       // return
        return this.matrixParser;
    }

    /**
     * get the protein reference letter list
     *
     * @return
     */
    public List<String> getProteinReferenceLetterList() {
        // local variables
        List<String> letterList;

        // get the list from the matrix parser
        letterList = this.getMatrixParser().getProteinReferenceLetterList();

        // return
        return letterList;
    }

    /**
     * get the gene allele effect
     *
     * @param position
     * @param allele
     * @return
     * @throws GradeException
     */
    public GeneResult getGeneResultForAllele(int position, String allele) throws GradeException {
        // local variables
        GeneResult geneResult = new GeneResult();
        Gene gene;

        // get the gene
        gene = this.matrixParser.getGene();

        // get the 2 codons at the position
        geneResult.setReferenceCodon(gene.getCodonAtPosition(position));
        geneResult.setNewCodon(gene.getNewCodonForAlleleAtPosition(position, allele));

        // look to see if coding position
        if (gene.isPositionInCodingRegion(position)) {
            geneResult.setProteinCodingPosition(true);

            // get protein reference and allele
            // get the protein position and reference allele
            int positionTemp = gene.getProteinPositionForCodingRegionAllele(position);
            geneResult.setProteinPosition(positionTemp);
            geneResult.setReferenceProteinAllele(this.matrixParser.getProteinReferenceLetterAtPosition(geneResult.getProteinPosition()));

            // get the new protein allele based on the new codon
            geneResult.setNewProteinAllele(this.matrixParser.getCodonToAminoAcidMap().get(geneResult.getNewCodon()));

            // get the heat amount based on the new protein allele
            if (!geneResult.isResultStopCodon()) {
                geneResult.setHeatAmount(this.matrixParser.getMatrixValueAtPositionAndLetterAndType(geneResult.getProteinPosition(), geneResult.getNewProteinAllele()), MatrixParser.MATRIX_TYPE_POSITION_HEAT);
            }

        } else {
            geneResult.setProteinCodingPosition(false);
        }

        // return
        return geneResult;
    }
}
