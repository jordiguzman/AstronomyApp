package appkite.jordiguzman.com.astronomyapp.earth.service;


import java.util.List;

import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaceEarth {

    @GET(".")
    Call<List<Earth>> getDataEarth();


}
