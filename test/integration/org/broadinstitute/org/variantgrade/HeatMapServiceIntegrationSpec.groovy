package org.broadinstitute.org.variantgrade

import grails.test.spock.IntegrationSpec
import org.broadinstitute.variantgrade.HeatMapService
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
        Double resultDouble = null;
        resultDouble = this.heatMapService.getHeatMapReadingFromProtein(position, referenceAllele);
        
        then:
        assert resultDouble != null;
        assert resultDouble == (new Double(0.130984994891909))
    }
}
