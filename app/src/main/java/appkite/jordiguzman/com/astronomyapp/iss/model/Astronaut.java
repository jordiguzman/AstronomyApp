package appkite.jordiguzman.com.astronomyapp.iss.model;


public class Astronaut   {


    private String name;
    private String bioPhoto;
    private String flag;
    private String launchDate;
    private String role;
    private String location;
    private String bio;
    private String bioWiki;
    private String twitter;

    public Astronaut(String name, String bioPhoto, String flag, String launchDate, String role, String location, String bio, String bioWiki, String twitter) {
        this.name = name;
        this.bioPhoto = bioPhoto;
        this.flag = flag;
        this.launchDate = launchDate;
        this.role = role;
        this.location = location;
        this.bio = bio;
        this.bioWiki = bioWiki;
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public String getBioPhoto() {
        return bioPhoto;
    }

    public String getFlag() {
        return flag;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public String getBio() {
        return bio;
    }

    public String getBioWiki() {
        return bioWiki;
    }

    public String getTwitter() {
        return twitter;
    }




}
