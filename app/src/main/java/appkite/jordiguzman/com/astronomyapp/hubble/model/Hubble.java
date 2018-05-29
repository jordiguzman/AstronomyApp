package appkite.jordiguzman.com.astronomyapp.hubble.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Hubble implements Parcelable{

    private String name;
    private String url;
    private String abstractString;
    private String credits;
    private String urlThubNail;
    private String urlImage;

    private Hubble(Parcel in) {
        name = in.readString();
        url = in.readString();
        abstractString = in.readString();
        credits = in.readString();
        urlThubNail = in.readString();
        urlImage = in.readString();
    }

    public static final Creator<Hubble> CREATOR = new Creator<Hubble>() {
        @Override
        public Hubble createFromParcel(Parcel in) {
            return new Hubble(in);
        }

        @Override
        public Hubble[] newArray(int size) {
            return new Hubble[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getAbstractString() {
        return abstractString;
    }

    public String getCredits() {
        return credits;
    }

    public String getUrlThubNail() {
        return urlThubNail;
    }

    public String getUrlImage() {
        return urlImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(abstractString);
        dest.writeString(credits);
        dest.writeString(urlThubNail);
        dest.writeString(urlImage);
    }
}
