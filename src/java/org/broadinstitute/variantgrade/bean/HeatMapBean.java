package org.broadinstitute.variantgrade.bean;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold the heat maps
 *
 * Created by mduby on 10/11/17.
 */
public class HeatMapBean {
    // instance variables
    private Map<Integer, PositionMatrixBean> heatMap = new HashMap<Integer, PositionMatrixBean>();
    private String name;
    private InputStream heatMapStream;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, PositionMatrixBean> getHeatMap() {

        return heatMap;
    }

    public void setHeatMap(Map<Integer, PositionMatrixBean> heatMap) {
        this.heatMap = heatMap;
    }

    public InputStream getHeatMapStream() {
        return heatMapStream;
    }

    public void setHeatMapStream(InputStream heatMapStream) {
        this.heatMapStream = heatMapStream;
    }
}
