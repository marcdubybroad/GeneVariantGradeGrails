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
    public void testTranslateGoodProteinInput() {
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
        assertTrue(bean.isProteinInput());
    }

    @Test
    public void testTranslateGoodGenotypeInput() {
        // local variables
        SearchInputBean bean = null;
        SearchInputTranslator translator = new SearchInputTranslator("chr3-68746-A-G");

        // get the bean
        try {
            bean = translator.translate();

        } catch (GradeException exception) {
            fail("got protein translation error: " + exception.getMessage());
        }

        // test
        assertNotNull(bean);
        assertEquals("a", bean.getGeneReferenceAllele());
        assertEquals(68746, bean.getGenePosition());
        assertEquals("g", bean.getGeneInputAllele());
        assertTrue(!bean.isProteinInput());
    }

    @Test
    public void testTranslateGoodGenotypeInput2() {
        // local variables
        SearchInputBean bean = null;
        SearchInputTranslator translator = new SearchInputTranslator("chr3-68747-G-A");

        // get the bean
        try {
            bean = translator.translate();

        } catch (GradeException exception) {
            fail("got protein translation error: " + exception.getMessage());
        }

        // test
        assertNotNull(bean);
        assertEquals("g", bean.getGeneReferenceAllele());
        assertEquals(68747, bean.getGenePosition());
        assertEquals("a", bean.getGeneInputAllele());
        assertTrue(!bean.isProteinInput());
    }

    @Test
    public void testTranslateGoodGenotypeInput3() {
        // local variables
        SearchInputBean bean = null;
        SearchInputTranslator translator = new SearchInputTranslator("chr3-1-G-A");

        // get the bean
        try {
            bean = translator.translate();

        } catch (GradeException exception) {
            fail("got protein translation error: " + exception.getMessage());
        }

        // test
        assertNotNull(bean);
        assertEquals("g", bean.getGeneReferenceAllele());
        assertEquals(1, bean.getGenePosition());
        assertEquals("a", bean.getGeneInputAllele());
        assertTrue(!bean.isProteinInput());
    }

}
