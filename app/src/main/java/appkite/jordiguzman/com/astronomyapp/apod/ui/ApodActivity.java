package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApodActivity extends AppCompatActivity implements AdapterApod.ItemClickListenerApod{

    public static ArrayList<Apod> mApodData = new ArrayList<>();

    private  RecyclerView mRecyclerView;
    private AdapterApod mAdapterApod;
    public static int itemPosition;
    private static ImageView iv_apod;

    @BindView(R.id.collapsing_apod)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        ButterKnife.bind(this);
        iv_apod = findViewById(R.id.iv_apod);
        imageCollapsingToolBar();

        mRecyclerView= findViewById(R.id.rv_apod);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapterApod = new AdapterApod(mApodData,  this, this);
        mRecyclerView.setAdapter(mAdapterApod);
        mRecyclerView.setHasFixedSize(true);
        populateImage(this);

        ApodActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preLoadHdurl();
            }
        });

    }

    private void preLoadHdurl() {
        for (int i=0; i< mApodData.size();i++){
            Glide.with(this)
                    .load(mApodData.get(i).getHdurl())
                    .preload();
        }
    }

    public  void populateImage(Context context){
        String url_base_youtube_video= "http://img.youtube.com/vi/";
        String url_base_embed = "https://www.youtube.com/embed/";

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


    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }

    @Override
    public void onClickItem(int position) {
        itemPosition = position;
        Intent intent = new Intent(this, ApodDetailActivity.class);
        startActivity(intent);

    }

}
