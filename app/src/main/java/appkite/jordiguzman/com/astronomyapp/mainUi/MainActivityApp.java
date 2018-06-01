package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiClientApod;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiIntefaceApod;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiClientEarth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiInterfaceEarth;
import appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;

public class MainActivityApp extends AppCompatActivity  implements AdapterMain.ItemClickListener{


    private static LocalDate today;
    private static LocalDate dateOld;
    public static int datesToShow;
    @BindView(R.id.iv_main)
    ImageView iv_main;
    @BindView(R.id.collapsing_main)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.pb_main)
    ProgressBar pb_main;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        ButterKnife.bind(this);

        progresBar();
        snackBar();
        datesToShow= 15;


        RecyclerView mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        AdapterMain mAdapterMain = new AdapterMain(this, MainActivityApp.this);
        mRecyclerView.setAdapter(mAdapterMain);
        mRecyclerView.setHasFixedSize(true);
        mAdapterMain.notifyDataSetChanged();


        Glide.with(this)
                .load(Splash.URL)
                .into(iv_main);

        imageCollapsingToolBar();
        today = LocalDate.now(ZoneId.of("US/Eastern"));
        datesToShow();
        MainActivityApp.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getDataApod(getApplicationContext());
                getDataEarth();


            }
        });

    }

    public void progresBar(){
        pb_main.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        CountDownTimer countDownTimer = new CountDownTimer(6000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                pb_main.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        };
        countDownTimer.start();

    }
    public void snackBar(){

        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, "Loading...", 5000)
                .setActionTextColor(Color.RED);

        snackbar.show();


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void datesToShow(){

        for (int i = 0; i < datesToShow; i++) {
            dateOld = today.minusDays(i);

        }
    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }



    public static void getDataApod(final Context context){
        final ApiIntefaceApod mApiInteface = ApiClientApod.getClient().create(ApiIntefaceApod.class);
        Call<List<Apod>> call = mApiInteface.getData(ApiClientApod.API_KEY, String.valueOf(dateOld), String.valueOf(today));
        call.enqueue(new Callback<List<Apod>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)

            @Override
            public void onResponse(@NonNull Call<List<Apod>> call, @NonNull Response<List<Apod>> response) {
                switch (response.code()){
                    case 200:
                        mApodData = (ArrayList<Apod>) response.body();
                        if (mApodData != null){
                            Collections.reverse(mApodData);
                            Log.i("Copyright", " is: " + mApodData.get(1).getCopyright());
                        }
                        break;

                    default:
                        Toast.makeText(context, "Error api", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Apod>> call, @NonNull Throwable t) {
                Log.e("OnFailure", t.getMessage());
            }


        });
    }

    public static void getDataEarth() {
        final ApiInterfaceEarth mApiInterfaceEarth = ApiClientEarth.getClientEarth().create(ApiInterfaceEarth.class);
        Call<List<Earth>> call = mApiInterfaceEarth.getDataEarth();
        call.enqueue(new Callback<List<Earth>>() {
            @Override
            public void onResponse(@NonNull Call<List<Earth>> call, @NonNull retrofit2.Response<List<Earth>> response) {
                switch (response.code()) {
                    case 200:
                        earthArrayList = (ArrayList<Earth>) response.body();

                        break;
                    default:
                        Log.e("Error API", response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Earth>> call, @NonNull Throwable t) {
                Log.e("On Failure", t.getMessage());
            }
        });
    }


    @Override
    public void onClickItem(int position) {
        switch (position){
            case 0:
                if (mApodData==null){
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                Intent intent = new Intent(this, ApodActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, EarthActivity.class);
                startActivity(intent1);
                break;
        }
    }

}
