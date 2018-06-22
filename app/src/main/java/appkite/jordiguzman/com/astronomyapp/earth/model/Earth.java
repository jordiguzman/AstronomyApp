package appkite.jordiguzman.com.astronomyapp.earth.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Earth implements Parcelable{

    @SerializedName ("caption") private String caption;
    @SerializedName("image") private String image;
    @SerializedName("date") private String date;
    @SerializedName("dscovr_j2000_position") private DistanceToEarth distanceToEarth;
    @SerializedName("sun_j2000_position") private DistanceToSun distanceToSun;

    private Earth(Parcel in) {
        caption = in.readString();
        image = in.readString();
        date = in.readString();
        distanceToEarth = in.readParcelable(DistanceToEarth.class.getClassLoader());
        distanceToSun = in.readParcelable(DistanceToSun.class.getClassLoader());
    }

    public static final Creator<Earth> CREATOR = new Creator<Earth>() {
        @Override
        public Earth createFromParcel(Parcel in) {
            return new Earth(in);
        }

        @Override
        public Earth[] newArray(int size) {
            return new Earth[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public DistanceToEarth getDistanceToEarth() {
        return distanceToEarth;
    }

    public DistanceToSun getDistanceToSun() {
        return distanceToSun;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(image);
        dest.writeString(date);
        dest.writeParcelable(distanceToEarth, flags);
        dest.writeParcelable(distanceToSun, flags);
    }
}
