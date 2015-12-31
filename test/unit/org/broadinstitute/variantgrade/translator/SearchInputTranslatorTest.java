package org.broadinstitute.variantgrade.translator;

import junit.framework.TestCase;
import org.broadinstitute.variantgrade.input.SearchInputBean;
import org.broadinstitute.variantgrade.util.GradeException;
import org.junit.Test;

/**
 * Created by mduby on 12/31/15.
 */
public class SearchInputTranslatorTest extends TestCase {


    @Test
    public void testTranslateGoodInput() {
        // local variables
        SearchInputBean bean = null;
        SearchInputTranslator translator = new SearchInputTranslator("p.Leu299Ile");

        // get the bean
        try {
            bean = translator.translate();

        } catch (GradeException exception) {
            fail("got protein translation error: " + exception.getMessage());
        }

        // test
        assertNotNull(bean);
        assertEquals("L", bean.getProteinReferenceAllele());
        assertEquals(299, bean.getProteinPosition());
        assertEquals("I", bean.getProteinInputAllele());
    }
}
