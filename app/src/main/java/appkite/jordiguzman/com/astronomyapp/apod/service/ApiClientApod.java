package appkite.jordiguzman.com.astronomyapp.apod.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ApiClientApod {
    private static final String BASE_URL = "https://api.nasa.gov/planetary/";
    private static Retrofit retrofit = null;
    public static final String API_KEY = "TuB6OluEaUTVzK0LcUNIBJEYyAgGjRAi2JgsbT0o";


    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
