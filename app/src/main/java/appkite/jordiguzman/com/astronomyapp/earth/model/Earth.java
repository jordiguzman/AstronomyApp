package appkite.jordiguzman.com.astronomyapp.earth.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Earth implements Parcelable{

    private String caption;
    private String image;
    private String date;

    private Earth(Parcel in) {
        caption = in.readString();
        image = in.readString();
        date = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(image);
        dest.writeString(date);
    }
}
