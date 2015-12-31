package org.broadinstitute.variantgrade.bean;

/**
 * Created by mduby on 12/31/15.
 */
public class ProteinBean {
    private String codeThreeLetter;
    private String codeOneLetter;
    private String name;

    public ProteinBean(String oneLetterCode, String threeLetterCode, String name) {
        this.codeOneLetter = oneLetterCode;
        this.codeThreeLetter = threeLetterCode;
        this.name = name;
    }

    public String getCodeThreeLetter() {
        return codeThreeLetter;
    }

    public void setCodeThreeLetter(String codeThreeLetter) {
        this.codeThreeLetter = codeThreeLetter;
    }

    public String getCodeOneLetter() {
        return codeOneLetter;
    }

    public void setCodeOneLetter(String codeOneLetter) {
        this.codeOneLetter = codeOneLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
