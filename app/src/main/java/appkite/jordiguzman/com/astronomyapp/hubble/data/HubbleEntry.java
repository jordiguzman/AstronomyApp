package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "hubble")
public class HubbleEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String credits;
    private String image;

    @Ignore
    public HubbleEntry(String name, String description, String credits, String image) {
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.image = image;
    }

    public HubbleEntry(int id, String name, String description, String credits, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCredits() {
        return credits;
    }

    public String getImage() {
        return image;
    }
}
