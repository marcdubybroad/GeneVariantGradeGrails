package org.broadinstitute.variantgrade.bean;

/**
 * Created by mduby on 1/2/16.
 */
public class OddsRatioBean {
    // instance variables
    private String webDisplayString;
    private Double value;
    private Integer id;

    public OddsRatioBean(int id, String name, Double value) {
        this.id = new Integer(id);
        this.webDisplayString = name;
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public String getWebDisplayString() {
        return webDisplayString;
    }

    public Integer getId() {
        return id;
    }
}
