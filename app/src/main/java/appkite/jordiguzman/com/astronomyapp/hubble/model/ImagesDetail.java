package appkite.jordiguzman.com.astronomyapp.hubble.model;


public class ImagesDetail  {

    private String name;
    private String description;
    private String credits;
    private String image;


    public ImagesDetail(String name, String description, String credits,
                        String image) {
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.image = image;
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





