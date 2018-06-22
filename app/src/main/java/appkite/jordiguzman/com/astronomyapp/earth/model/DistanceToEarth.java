package appkite.jordiguzman.com.astronomyapp.earth.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DistanceToEarth implements Parcelable{


    @SerializedName("y")private double Y;

    public double getY() {
        return Y;
    }

    private DistanceToEarth(Parcel in) {
        Y = in.readDouble();

    }

    public static final Creator<DistanceToEarth> CREATOR = new Creator<DistanceToEarth>() {
        @Override
        public DistanceToEarth createFromParcel(Parcel in) {
            return new DistanceToEarth(in);
        }

        @Override
        public DistanceToEarth[] newArray(int size) {
            return new DistanceToEarth[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(Y);
    }
}
