package appkite.jordiguzman.com.astronomyapp.earth.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PositionEarth  implements Parcelable{

    @SerializedName("x") private long x;
    @SerializedName("y")private long Y;
    @SerializedName("z") private long z;

    protected PositionEarth(Parcel in) {
        x = in.readLong();
        Y = in.readLong();
        z = in.readLong();
    }

    public static final Creator<PositionEarth> CREATOR = new Creator<PositionEarth>() {
        @Override
        public PositionEarth createFromParcel(Parcel in) {
            return new PositionEarth(in);
        }

        @Override
        public PositionEarth[] newArray(int size) {
            return new PositionEarth[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(x);
        dest.writeLong(Y);
        dest.writeLong(z);
    }
}
