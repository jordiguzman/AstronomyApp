package appkite.jordiguzman.com.astronomyapp.earth.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DistanceToSun implements Parcelable{
    @SerializedName("y") private double Y;

    public double getY() {
        return Y;
    }

    private DistanceToSun(Parcel in) {
        Y = in.readDouble();
    }

    public static final Creator<DistanceToSun> CREATOR = new Creator<DistanceToSun>() {
        @Override
        public DistanceToSun createFromParcel(Parcel in) {
            return new DistanceToSun(in);
        }

        @Override
        public DistanceToSun[] newArray(int size) {
            return new DistanceToSun[size];
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
