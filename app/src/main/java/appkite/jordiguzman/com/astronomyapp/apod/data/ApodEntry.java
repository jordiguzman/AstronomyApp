package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "apod")
public class ApodEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String copyrigth;
    private String title;
    private String date;
    private String explanation;
    private String url;


    @Ignore
    public ApodEntry(String copyrigth, String title, String date, String explanation, String url) {
        this.copyrigth = copyrigth;
        this.title = title;
        this.date = date;
        this.explanation = explanation;
        this.url = url;

    }

    ApodEntry(int id, String copyrigth, String title, String date, String explanation, String url) {
        this.id = id;
        this.copyrigth = copyrigth;
        this.title = title;
        this.date = date;
        this.explanation = explanation;
        this.url = url;

    }

    public int getId() {
        return id;
    }

    public String getCopyrigth() {
        return copyrigth;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getUrl() {
        return url;
    }


}
