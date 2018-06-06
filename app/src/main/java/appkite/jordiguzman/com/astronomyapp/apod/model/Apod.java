package appkite.jordiguzman.com.astronomyapp.apod.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Apod  implements Parcelable{


    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String mediaType;
    private String serviceVersion;
    private String title;
    private String url;
    private String dateToShow;




    public String getCopyright() {
        return copyright;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDateToShow() {
        return dateToShow;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    private Apod(Parcel in) {
        copyright = in.readString();
        date = in.readString();
        explanation = in.readString();
        hdurl = in.readString();
        mediaType = in.readString();
        serviceVersion = in.readString();
        title = in.readString();
        url = in.readString();
        dateToShow = in.readString();
    }

    public static final Creator<Apod> CREATOR = new Creator<Apod>() {
        @Override
        public Apod createFromParcel(Parcel in) {
            return new Apod(in);
        }

        @Override
        public Apod[] newArray(int size) {
            return new Apod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(copyright);
        dest.writeString(date);
        dest.writeString(explanation);
        dest.writeString(hdurl);
        dest.writeString(mediaType);
        dest.writeString(serviceVersion);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(dateToShow);
    }

}
