package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.leakcanary.LeakCanary;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiClientApod;
import appkite.jordiguzman.com.astronomyapp.apod.service.ApiIntefaceApod;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp.isTablet;
import static appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp.urlToWidget;

public class ApodActivity extends AppCompatActivity implements AdapterApod.ItemClickListenerApod{


    public static ArrayList<Apod> mApodData = new ArrayList<>();

    public static int itemPosition;
    @SuppressLint("StaticFieldLeak")
    private  ImageView iv_apod;
    @BindView(R.id.pb_apod_activity)
    ProgressBar pb_apod_activity;
    @BindView(R.id.collapsing_apod)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.ib_menu_acticity_apod)
    ImageButton ib_menu_apod;
    private static LocalDate today;
    private static LocalDate dateOld;
    public static int datesToShow;
    private RecyclerView mRecyclerView;
    private AsynctTaskApod asynctTaskApod;


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        ButterKnife.bind(this);
        iv_apod = findViewById(R.id.iv_item_apod);
        mRecyclerView = findViewById(R.id.rv_apod);

        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(getApplication());
        imageCollapsingToolBar();

        datesToShow= 30;
        saveNumberItems();
        readSharedPreferences();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now(ZoneId.of("US/Michigan"));
        }else {

            today = LocalDate.now();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datesToShow();
        }


        asynctTaskApod = new AsynctTaskApod();


        if (!mApodData.isEmpty()){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    populateImage(getApplicationContext());
                }
            });

        }else {
            pb_apod_activity.setVisibility(View.VISIBLE);
            asynctTaskApod.execute();
        }



    }

    private void readSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("itemNumber", Context.MODE_PRIVATE);
        datesToShow = sharedPreferences.getInt("number", 0);
    }


    @SuppressLint("StaticFieldLeak")
    public class AsynctTaskApod extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getDataApod(getApplicationContext());
            preLoadHdurl();
            return null;
        }
    }

    public void getDataApod(final Context context){
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
                            populateImage(context);
                            urlToWidget = mApodData.get(0).getUrl();
                        }
                        break;
                    default:
                        Toast.makeText(context, "Error api", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Apod>> call, @NonNull Throwable t) {
                Log.e("OnFailure", t.getMessage());
                snackBar();
                asynctTaskApod.cancel(true);



            }


        });
    }



    private void snackBar() {
            Snackbar mSnackbar = Snackbar
                    .make(mCoordinatorLayout, getResources().getString(R.string.no_api), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asynctTaskApod.execute();
                        }
                    });
            mSnackbar.setActionTextColor(Color.RED);
            View sbView = mSnackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mSnackbar.show();
    }



    public static void datesToShow(){

        for (int i = 0; i < datesToShow; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateOld = today.minusDays(i);
            }

        }
    }
    private void preLoadHdurl() {
        for (int i=0; i< mApodData.size();i++){
            Glide.with(this)
                    .load(mApodData.get(i).getUrl())
                    .apply(RequestOptions.overrideOf(800,800))
                    .preload();
        }
    }


    public void populateImage(Context context){
        String url_base_youtube_video= "http://img.youtube.com/vi/";
        String url_base_embed = "https://www.youtube.com/embed/";

        if (mApodData != null){
            AdapterApod mAdapterApod = new AdapterApod(mApodData, this, ApodActivity.this);

            if (isTablet(context)){
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }else {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapterApod);
            String url = mApodData.get(0).getUrl();
            int length = url.length();
            String result = url.substring(length-3, length);
            if (result.equals("jpg") || result.equals("peg")
                    || result.equals("gif") || result.equals("png")){
                Glide.with(context)
                        .load(mApodData.get(0).getUrl())
                        .into(iv_apod);
            }else {
                String key = url.substring(url_base_embed.length(), url.length()-6);
                String urlResult = url_base_youtube_video + key +"/0.jpg";
                Glide.with(context)
                        .load(urlResult)
                        .into(iv_apod);
            }

        }
        pb_apod_activity.setVisibility(View.INVISIBLE);

    }

    public void clickMenuApod(View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_apod, popupMenu.getMenu());

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_favorites:
                        goToFavoritesApod();
                        break;
                    case R.id.menu_numer_items_30:
                        datesToShow= 30;
                        menuActions();
                        break;
                    case R.id.menu_numer_items_40:
                        datesToShow= 40;
                        menuActions();
                        break;
                    case R.id.menu_numer_items_60:
                        datesToShow = 60;
                        menuActions();
                        break;
                }
                return true;
            }

        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuActions(){
        saveNumberItems();
        datesToShow();
        asynctTaskApod.cancel(true);
        getDataApod(getApplicationContext());
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mCoordinatorLayout.setAnimation(shake);
        showSnackbar();
        populateImage(this);
    }


    private void showSnackbar() {
        switch (datesToShow){
            case 30:
                Snackbar mSnackbar = Snackbar
                        .make(mCoordinatorLayout, getResources().getString(R.string.number_of_days_30),
                                Snackbar.LENGTH_LONG);
                View sbView = mSnackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                mSnackbar.show();
                break;
            case 40:
                Snackbar mSnackbar1 = Snackbar
                        .make(mCoordinatorLayout, getResources().getString(R.string.number_of_days_40),
                                Snackbar.LENGTH_LONG);
                View sbView1 = mSnackbar1.getView();
                TextView textView1 = sbView1.findViewById(android.support.design.R.id.snackbar_text);
                textView1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                mSnackbar1.show();
                break;
            case 60:
                Snackbar mSnackbar2 = Snackbar
                        .make(mCoordinatorLayout, getResources().getString(R.string.number_of_days_60),
                                Snackbar.LENGTH_LONG);
                View sbView2 = mSnackbar2.getView();
                TextView textView2 = sbView2.findViewById(android.support.design.R.id.snackbar_text);
                textView2.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                mSnackbar2.show();
                break;

        }



    }

    public void saveNumberItems(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("itemNumber", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number", datesToShow);
        editor.apply();
    }

    private void goToFavoritesApod() {
        Intent intent = new Intent(this, FavoritesApodActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClickItem(int position) {
        itemPosition = position;
        Intent intent = new Intent(this, ApodDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.move_down_activity, R.anim.move_up_activity);


    }




}
