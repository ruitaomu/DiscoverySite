package com.jfighter.discoverysite.util;

public class PoiInfo {
    private Coordinate mCoordinate;
    private String mImageName;
    private String mDescription;
    private String mType;

    public PoiInfo(Coordinate coordinate, String imageName, String description, String type) {
        mCoordinate = coordinate;
        mImageName = imageName;
        mDescription = description;
        mType = type;
    }

    public Coordinate getmCoordinate() {
        return mCoordinate;
    }

    public String getmImageName() {
        return mImageName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmType() {
        return mType;
    }
}
