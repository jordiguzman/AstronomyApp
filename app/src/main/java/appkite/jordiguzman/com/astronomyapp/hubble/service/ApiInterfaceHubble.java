package appkite.jordiguzman.com.astronomyapp.hubble.service;


import java.util.List;

import appkite.jordiguzman.com.astronomyapp.hubble.model.Hubble;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaceHubble {

    @GET(".")
    Call<List<Hubble>> getDataHubble();
}
