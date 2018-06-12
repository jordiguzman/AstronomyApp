package appkite.jordiguzman.com.astronomyapp.hubble.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Hubble implements Parcelable {

    private String name;
    private String url;
    private String abstractString;
    private String credits;
    private String urlThubNail;
    private String urlImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAbstractString() {
        return abstractString;
    }

    public void setAbstractString(String abstractString) {
        this.abstractString = abstractString;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getUrlThubNail() {
        return urlThubNail;
    }

    public void setUrlThubNail(String urlThubNail) {
        this.urlThubNail = urlThubNail;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    protected Hubble(Parcel in) {
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