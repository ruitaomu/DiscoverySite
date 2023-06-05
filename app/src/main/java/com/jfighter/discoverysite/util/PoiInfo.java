package com.jfighter.discoverysite.util;

public class PoiInfo {
    private final String mDescription;
    private final Coordinate mCoordinate;
    private final String mImageName;
    private final String mSiteName;
    private final String mType;

    public PoiInfo(Coordinate coordinate, 
                   String imageName, 
                   String siteName, 
                   String type, String description) {
        mCoordinate = coordinate;
        mImageName = imageName;
        mSiteName = siteName;
        mType = type;
        mDescription = description;
    }

    public Coordinate getmCoordinate() {
        return mCoordinate;
    }

    public String getmImageName() {
        return mImageName;
    }

    public String getmSiteName() {
        return mSiteName;
    }

    public String getmType() {
        return mType;
    }

    public String getmDescription() { return mDescription; }
}
