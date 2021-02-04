package com.cityguide.PostClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable, Comparable<Post>{

    private String objectId;
    private String categoryName;
    private String locationDescription;
    private String locationLocation;
    private Bitmap locationImage;
    private int commentCount;
    private double averageRating;
    private double latitude;
    private double longitude;
    private Bitmap image;
    private String compareString;
    private double distance;


    public Post(String objectId, String categoryName, String locationDescription, String locationLocation, int commentCount, double averageRating, double latitude, double longitude, Bitmap image) {
        this.objectId = objectId;
        this.categoryName = categoryName;
        this.locationDescription = locationDescription;
        this.locationLocation = locationLocation;
        this.image = image;
        this.commentCount = commentCount;
        this.averageRating = averageRating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getLocationLocation() {
        return locationLocation;
    }

    public void setLocationLocation(String locationLocation) {
        this.locationLocation = locationLocation;
    }


    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getCompareString() {
        return compareString;
    }

    public void setCompareString(String compareString) {
        this.compareString = compareString;
    }

    @Override
    public int compareTo(Post o) {
        if (compareString.equals("toprated")){
            if (this.getAverageRating() > o.getAverageRating()){
                return -1;
            }else if (this.getAverageRating() == o.getAverageRating()){
                return 0;
            }else {
                return 1;
            }
        }else if (compareString.equals("mostcomments")){
            if (this.getCommentCount() > o.getCommentCount()){
                return -1;
            }else if (this.getCommentCount() == o.getCommentCount()){
                return 0;
            }else {
                return 1;
            }
        }else if (compareString.equals("closest")){
            if (this.getDistance() < o.getDistance()){
                return -1;
            }else if (this.getDistance() == o.getDistance()){
                return 0;
            }else {
                return 1;
            }
        }
        return 0;
    }
}
