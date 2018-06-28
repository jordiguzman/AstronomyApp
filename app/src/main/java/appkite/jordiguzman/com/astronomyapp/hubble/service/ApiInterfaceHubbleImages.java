package appkite.jordiguzman.com.astronomyapp.hubble.service;


import java.util.List;

import appkite.jordiguzman.com.astronomyapp.hubble.model.Images;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaceHubbleImages {

    @GET(".")
    Call<List<Images>> getDataImages();
}
