package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApodActivity extends AppCompatActivity implements AdapterApod.ItemClickListenerApod{


    public static ArrayList<Apod> mApodData = new ArrayList<>();

    public static int itemPosition;
    @SuppressLint("StaticFieldLeak")
    private static ImageView iv_apod;
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



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        ButterKnife.bind(this);
        iv_apod = findViewById(R.id.iv_apod);
        mRecyclerView = findViewById(R.id.rv_apod);

        imageCollapsingToolBar();

        datesToShow= 50;
        today = LocalDate.now(ZoneId.of("US/Eastern"));
        datesToShow();

        preLoadHdurl();


        if (!mApodData.isEmpty()){
            populateImage(this);
        }else {
            pb_apod_activity.setVisibility(View.VISIBLE);
            new AsynctTaskApod().execute();
        }



    }

    @SuppressLint("StaticFieldLeak")
    public class AsynctTaskApod extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            getDataApod(getApplicationContext());

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



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void datesToShow(){

        for (int i = 0; i < datesToShow; i++) {
            dateOld = today.minusDays(i);

        }
    }
    private void preLoadHdurl() {
        for (int i=0; i< mApodData.size();i++){
            Glide.with(this)
                    .load(mApodData.get(i).getHdurl())
                    .preload();
        }
    }


    public void populateImage(Context context){
        String url_base_youtube_video= "http://img.youtube.com/vi/";
        String url_base_embed = "https://www.youtube.com/embed/";

        if (mApodData != null){
            AdapterApod mAdapterApod = new AdapterApod(mApodData, this, ApodActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapterApod);
            String url = mApodData.get(0).getUrl();
            int length = url.length();
            String result = url.substring(length-3, length);
            if (!result.equals("jpg")){
                String key = url.substring(url_base_embed.length(), url.length()-6);
                String urlResult = url_base_youtube_video + key +"/0.jpg";
                Glide.with(context)
                        .load(urlResult)
                        .into(iv_apod);
            }else {
                Glide.with(context)
                        .load(mApodData.get(0).getUrl())
                        .into(iv_apod);
            }
        }
        pb_apod_activity.setVisibility(View.INVISIBLE);

    }

    public void clickMenu(View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_apod, popupMenu.getMenu());

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_favorites:
                        goToFavoritesApod();
                        break;
                }
                return true;
            }

        });
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
        /*String trans = getString(R.string.trans_photo_apod);
        View transView = findViewById(R.id.iv_apod);
        ViewCompat.setTransitionName(transView, trans);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(ApodActivity.this, transView, trans);*/
        Intent intent = new Intent(this, ApodDetailActivity.class);

        startActivity(intent);

    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //deleteCache(this);
    }


}
