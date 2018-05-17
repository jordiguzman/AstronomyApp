package appkite.jordiguzman.com.astronomyapp.apod.service;

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ApiIntefaceApod {

    @GET("apod")
    Call<List<Apod>> getData(
            @Query("api_key") String apiKey,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate);

}
