package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.earth.adapter.AdapterEarth;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiClientEarth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiInterfaceEarth;
import appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity;
import appkite.jordiguzman.com.astronomyapp.iss.ui.MapsActivity;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthDetailActivity.dateApi;
import static appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp.isTablet;

public class EarthActivity extends AppCompatActivity implements AdapterEarth.ItemClickListenerEarth {


    public static ArrayList<Earth> earthArrayList = new ArrayList<>();
    public static int itemPositionEarth;
    @BindView(R.id.iv_earth)
    ImageView iv_earth;
    @BindView(R.id.collapsing_earth)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @SuppressLint("StaticFieldLeak")
    private static RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);
        ButterKnife.bind(this);

        /*if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(getApplication());*/


        mRecyclerView= findViewById(R.id.rv_earth);
        imageCollapsingToolBar();

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[1])
                .into(iv_earth);

        if (earthArrayList.isEmpty()){
            getDataEarth();
        }else {
            populateData();
            convertDate();
        }

    }
    public void convertDate(){
        String dateTemp = earthArrayList.get(0).getDate().substring(0, 10);
        int a = 4;
        int b = 7;
        dateApi = new StringBuilder(dateTemp).replace(a, a+1, "/").replace(b, b+1, "/").toString().concat("/");
    }

    public  void getDataEarth() {
        final ApiInterfaceEarth mApiInterfaceEarth = ApiClientEarth.getClientEarth().create(ApiInterfaceEarth.class);
        Call<List<Earth>> call = mApiInterfaceEarth.getDataEarth();
        call.enqueue(new Callback<List<Earth>>() {
            @Override
            public void onResponse(@NonNull Call<List<Earth>> call, @NonNull retrofit2.Response<List<Earth>> response) {
                switch (response.code()) {
                    case 200:
                        earthArrayList = (ArrayList<Earth>) response.body();
                         if (earthArrayList != null){
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     //preloadPictures();
                                 }
                             });
                             populateData();
                             convertDate();

                         }

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

    private  void populateData() {
        if (isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        AdapterEarth adapterEarth = new AdapterEarth(this, earthArrayList, this);
        mRecyclerView.setAdapter(adapterEarth);
        mRecyclerView.setHasFixedSize(true);
        adapterEarth.notifyDataSetChanged();
    }

    public void clickMenuEarth(View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_earth, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.from_earth_to_apod:
                        gotoActivity(ApodActivity.class);
                        break;
                    case R.id.from_earth_to_solar:
                        gotoActivity(SolarSystemActivity.class);
                        break;
                    case R.id.from_earth_to_iss:
                        gotoActivity(MapsActivity.class);
                        break;
                    case R.id.from_earth_to_hubble:
                        gotoActivity(HubbleActivity.class);
                        break;
                }
                return true;
            }

        });
    }
    public void gotoActivity(Class toClass){
        Intent intent = new Intent(this, toClass);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }
    @Override
    public void onClickItem(int position) {
        itemPositionEarth = position;
        Intent intent = new Intent(this, EarthDetailActivity.class);
        startActivity(intent);
    }
}
