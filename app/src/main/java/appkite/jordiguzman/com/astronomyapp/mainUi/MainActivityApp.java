package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity;
import appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity;
import appkite.jordiguzman.com.astronomyapp.iss.ui.MapsActivity;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.CheckOnLine;
import appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.URL_PLANETS;

public class MainActivityApp extends AppCompatActivity  implements AdapterMain.ItemClickListener{




    @BindView(R.id.iv_main)
    ImageView iv_main;
    @BindView(R.id.collapsing_main)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    private CheckOnLine checkOnLIne;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        ButterKnife.bind(this);

         checkOnLIne = new CheckOnLine(this);
        if (!checkOnLIne.isOnline()){
            showSnackbar();
            return;
        }

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



        new Thread(new Runnable() {
            @Override
            public void run() {
                preLoadImagesSystem();
            }
        });

    }

    private void showSnackbar() {
        Snackbar mSnackbar = Snackbar
                .make(mCoordinatorLayout, getResources().getString(R.string.no_network), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkOnLIne.isOnline()) {
                            showSnackbar();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Splash.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
        mSnackbar.setActionTextColor(Color.RED);
        View sbView = mSnackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        mSnackbar.show();
    }


    private void preLoadImagesSystem() {
        for (String URL_PLANET : URL_PLANETS) {
            Glide.with(this)
                    .load(URL_PLANET)
                    .preload();
        }

    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }

    @Override
    public void onClickItem(int position) {
        switch (position){
            case 0:
                Intent intent = new Intent(this, ApodActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, EarthActivity.class);
                startActivity(intent1);
                break;

            case 2:
                Intent intent2 = new Intent(this, SolarSystemActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, MapsActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, HubbleActivity.class);
                startActivity(intent4);

                break;
        }
    }

}
