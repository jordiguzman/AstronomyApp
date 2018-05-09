package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiClient;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiInteface;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.planets.MainActivityPlanets;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;
public class MainActivityApp extends AppCompatActivity {

    private final String LOG_TAG = MainActivityApp.class.getSimpleName();
    private LocalDate today, dateOld;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);


        today = LocalDate.now();
        datesToShow();
        getData(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void datesToShow(){

        for (int i = 0; i < 15; i++) {
            dateOld = today.minusDays(i);

        }
    }

    public void clickButtons(View view){
        switch (view.getId()){
            case R.id.btn_apod:
                Intent intent = new Intent(this, ApodActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_planets:
                Intent intent1 = new Intent(this, MainActivityPlanets.class);
                startActivity(intent1);
                break;
        }

    }

    public void getData(final Context context){
        final ApiInteface mApiInteface = ApiClient.getClient().create(ApiInteface.class);
        Call<List<Apod>> call = mApiInteface.getData(ApiClient.API_KEY, String.valueOf(dateOld), String.valueOf(today));
        call.enqueue(new Callback<List<Apod>>() {
            @Override
            public void onResponse(@NonNull Call<List<Apod>> call, @NonNull Response<List<Apod>> response) {
                switch (response.code()){
                    case 200:
                        mApodData = (ArrayList<Apod>) response.body();
                        assert mApodData != null;
                        Collections.reverse(mApodData);
                        break;
                    default:
                        Toast.makeText(context, "Error api", Toast.LENGTH_LONG).show();
                        Log.e("Error API", response.toString());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Apod>> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
