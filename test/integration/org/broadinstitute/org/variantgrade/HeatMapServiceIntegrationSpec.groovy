package org.broadinstitute.org.variantgrade

import grails.test.spock.IntegrationSpec
import org.broadinstitute.variantgrade.HeatMapService
import org.broadinstitute.variantgrade.result.GeneResult
import org.broadinstitute.variantgrade.result.ProteinResult
import org.junit.After
import org.junit.Before

/**
 * Created by mduby on 12/24/15.
 */
class HeatMapServiceIntegrationSpec extends IntegrationSpec {
    // instance variables
    HeatMapService heatMapService;

    @Before
    void setup() {
    }

    @After
    void tearDown() {

    }

    void "test protein heat map value"() {
        when:
        int position = 245;
        String newAllele = "G";
        ProteinResult result = null;
        Double resultDouble = null;
        result = this.heatMapService.getHeatMapReadingFromProtein(position, newAllele);

        then:
        assert result.getHeatAmount() != null;
        assert result.getHeatAmount() == (new Double(0.130984994891909))
    }

    void "test gene heat map value for non coding"() {
        when:
        int position = 1805;
        String newAllele = "a";
        GeneResult geneResult = this.heatMapService.getGeneResultForAllele(position, newAllele)        ;

        then:
        assert geneResult != null;
        assert "agc" == geneResult.getReferenceCodon()
        assert "aac" == geneResult.getNewCodon()
        assert !geneResult.isProteinCodingPosition()
        assert null == geneResult.getReferenceProteinAllele()
        assert null == geneResult.getNewProteinAllele()
        assert !geneResult.isResultStopCodon()
        assert null == geneResult.getHeatAmount()
    }

    void "test gene heat map value for coding"() {
        when:
        int position = 98477;
        String newAllele = "g";
        GeneResult geneResult = this.heatMapService.getGeneResultForAllele(position, newAllele)        ;

        then:
        assert geneResult != null;
        assert "caa" == geneResult.getReferenceCodon()
        assert "cga" == geneResult.getNewCodon()
        assert geneResult.isProteinCodingPosition()
        assert 77 == geneResult.getProteinPosition()
        assert "A" == geneResult.getReferenceProteinAllele()
        assert "R" == geneResult.getNewProteinAllele()
        assert !geneResult.isResultStopCodon()
        assert 1.0 == geneResult.getHeatAmount()
    }

    void "test gene heat map value stop codon"() {
        when:
        int position = 98476;
        String newAllele = "t";
        GeneResult geneResult = this.heatMapService.getGeneResultForAllele(position, newAllele)        ;

        then:
        assert geneResult != null;
        assert "caa" == geneResult.getReferenceCodon()
        assert "taa" == geneResult.getNewCodon()
        assert geneResult.isProteinCodingPosition()
        assert 77 == geneResult.getProteinPosition()
        assert geneResult.isResultStopCodon()
        assert null == geneResult.getReferenceProteinAllele()
        assert null == geneResult.getNewProteinAllele()
        assert null == geneResult.getHeatAmount()
    }
}
