package org.broadinstitute.org.variantgrade

import grails.test.spock.IntegrationSpec
import org.broadinstitute.variantgrade.HeatMapService
import org.junit.After
import org.junit.Before

/**
 * Created by mduby on 12/24/15.
 */
class HeatMapServiceIntegrationTest extends IntegrationSpec {
    // instance variables
    HeatMapService heatMapService;

    @Before
    void setup() {
        metaDataService.getCommonPropertiesAsJson(false)
    }

    @After
    void tearDown() {

    }

    void "test protein heat map value"() {
        when:
        int position = 245;
        String referenceAllele = "G";
        Double resultDouble = null;

        then:
        assert resultDouble != null;
        assert resultDouble == (new Double(0.130984994891909))
    }
}
