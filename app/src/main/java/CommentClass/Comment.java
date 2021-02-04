package CommentClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    String username;
    String locationLocation;
    String comment;
    double rate;

    public Comment(String username, String locationLocation, String comment, double rate) {
        this.username = username;
        this.locationLocation = locationLocation;
        this.comment = comment;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocationLocation() {
        return locationLocation;
    }

    public void setLocationLocation(String locationLocation) {
        this.locationLocation = locationLocation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
