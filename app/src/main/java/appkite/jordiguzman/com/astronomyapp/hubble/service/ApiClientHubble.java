package appkite.jordiguzman.com.astronomyapp.hubble.service;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientHubble {

    private static final String BASE_URL = "http://hubblesite.org/api/v3/news_release/2018-12/";
    private static Retrofit retrofit = null;

    public static Retrofit getClientHubble(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
