package com.orion.ext.location.region.block;

/**
 * @author li
 */
public class DataBlock {

    /**
     * city id
     */
    private int cityId;

    /**
     * region address
     */
    private String region;

    /**
     * region ptr in the db file
     */
    private int dataPtr;

    public DataBlock(int cityId, String region, int dataPtr) {
        this.cityId = cityId;
        this.region = region;
        this.dataPtr = dataPtr;
    }

    public DataBlock(int cityId, String region) {
        this(cityId, region, 0);
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDataPtr(int dataPtr) {
        this.dataPtr = dataPtr;
    }

    public int getCityId() {
        return cityId;
    }

    public String getRegion() {
        return region;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    @Override
    public String toString() {
        return String.valueOf(cityId) + '|' + region + '|' + dataPtr;
    }

}
