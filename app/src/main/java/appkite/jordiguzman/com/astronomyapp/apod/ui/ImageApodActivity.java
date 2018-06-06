package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;

public class ImageApodActivity extends YouTubeBaseActivity {

    public static final String API_KEY = "AIzaSyBil6N0vhX3-uJRzwPZD2D7Pn3H5Ee-MuI";
    //private Toolbar toolbar;
    int position;
    private ConstraintLayout constraintLayout;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_apod);
        ImageView iv_apod_image = findViewById(R.id.iv_image_apod);
        constraintLayout = findViewById(R.id.layout_image_apod);
        //toolbar = findViewById(R.id.toolbar);
        YouTubePlayerView videoView_apod = findViewById(R.id.video_view_apod);
        //setSupportActionBar(toolbar);


        hideNavigation();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
        }


        String url_base_embed = "https://www.youtube.com/embed/";

        String url = mApodData.get(position).getUrl();
        int length = url.length();
        String result = url.substring(length - 3, length);
        if (!result.equals("jpg")) {
            final String key = url.substring(url_base_embed.length(), url.length() - 6);
            iv_apod_image.setVisibility(View.INVISIBLE);
            videoView_apod.setVisibility(View.VISIBLE);
            videoView_apod.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(key);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        snackBarVideo();
                }
            });


        } else {
            Glide.with(this)
                    .load(mApodData.get(position).getHdurl())
                    .into(iv_apod_image);
        }


    }

    private void snackBarVideo() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "Error on video!", 3000)
                .setActionTextColor(Color.RED);

        snackbar.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideNavigation() {
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
