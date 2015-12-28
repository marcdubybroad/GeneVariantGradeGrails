package org.broadinstitute.org.variantgrade

import grails.test.spock.IntegrationSpec
import org.broadinstitute.variantgrade.HeatMapService
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
        String referenceAllele = "G";
        ProteinResult result = null;
        Double resultDouble = null;
        result = this.heatMapService.getHeatMapReadingFromProtein(position, referenceAllele);

        then:
        assert result.getHeatAmount() != null;
        assert result.getHeatAmount() == (new Double(0.130984994891909))
    }
}
