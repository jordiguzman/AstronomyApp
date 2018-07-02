package appkite.jordiguzman.com.astronomyapp.earth.service;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientEarth {

    private static final String BASE_URL =
            "https://epic.gsfc.nasa.gov/api/natural/";
    private static Retrofit retrofit = null;

    public static Retrofit getClientEarth(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
