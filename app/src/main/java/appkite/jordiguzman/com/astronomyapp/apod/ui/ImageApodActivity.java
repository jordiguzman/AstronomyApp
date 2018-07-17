package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.utils.ShareImageApod;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodDataMain;

public class ImageApodActivity extends YouTubeBaseActivity  {

    public static final String API_KEY = "AIzaSyBil6N0vhX3-uJRzwPZD2D7Pn3H5Ee-MuI";

    private int position;

    @BindView(R.id.iv_image_apod)
    ImageView iv_apod_image;
    @BindView(R.id.video_view_apod)
    YouTubePlayerView videoView_apod;
    @BindView(R.id.ib_image_apod)
    ImageButton ib_image_apod;

    private LinearLayout linearLayout;
    ConstraintLayout constraintLayout;
    private int currentPositionVideo, mMutedColor;
    private YouTubePlayer player;
    private boolean isFavorited, noVideo, hideButtonNavigation;
    private ShareImageApod shareImageApod = new ShareImageApod();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_apod);
        ButterKnife.bind(this);
        constraintLayout = findViewById(R.id.layout_image_apod);
        linearLayout = findViewById(R.id.linearLayout_activity_image);
        showNavigation();

        if (savedInstanceState!=null){
            currentPositionVideo =savedInstanceState.getInt("current");
            }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            isFavorited = bundle.getBoolean("isFavorited");

        }
        preloadPicture();
        populatePictureVideo();
        ViewCompat.setTransitionName(iv_apod_image, "Image");
        ib_image_apod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareImageApod.shareImage(getApplicationContext(), mApodDataMain.get(position).getUrl());
                    }
                });



            }
        });

        iv_apod_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hideButtonNavigation){
                    Animation up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
                    ib_image_apod.startAnimation(up);
                    ib_image_apod.setVisibility(View.INVISIBLE);
                    hideNavigation();
                    hideButtonNavigation = true;
                }else {
                    Animation down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down);
                    ib_image_apod.startAnimation(down);
                    ib_image_apod.setVisibility(View.VISIBLE);
                    showNavigation();
                    hideButtonNavigation = false;
                }
            }
        });
    }

    private void preloadPicture() {
        Picasso.get()
                .load(mApodDataMain.get(position).getHdurl())
                .fetch();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void populatePictureVideo(){
        String url_base_embed = "https://www.youtube.com/embed/";
        String url;
        if (isFavorited){
            url = mApodDataMain.get(position).getUrl();
        }else {
            url = mApodDataMain.get(position).getUrl();
        }

        int length = url.length();
        String result = url.substring(length - 3, length);
        if (result.equals("jpg") || result.equals("peg")
                || result.equals("gif") || result.equals("png")){
            noVideo=true;
            if (isFavorited){
                setBackground(FavoritesApodActivity.mApodDataList.get(position).getUrl());
                Glide.with(this)
                        .load(FavoritesApodActivity.mApodDataList.get(position).getUrl())
                        .into(iv_apod_image);


            }else {
                setBackground(mApodDataMain.get(position).getUrl());
                Glide.with(this)
                        .load(mApodDataMain.get(position).getHdurl())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(iv_apod_image);

            }
        }else {
            noVideo = false;
            hideNavigation();
            ib_image_apod.setVisibility(View.INVISIBLE);
            iv_apod_image.setVisibility(View.INVISIBLE);
            final String key = url.substring(url_base_embed.length(), url.length() - 6);
            iv_apod_image.setVisibility(View.INVISIBLE);
            videoView_apod.setVisibility(View.VISIBLE);

            videoView_apod.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    player = youTubePlayer;
                    if (key != null && !b){
                        youTubePlayer.loadVideo(key);
                    }else {
                        youTubePlayer.seekToMillis(currentPositionVideo);
                        youTubePlayer.play();
                    }
                    if (isLandscape()){
                        youTubePlayer.seekToMillis(currentPositionVideo);
                        youTubePlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    snackBarVideo();
                }
            });
        }

    }
    private void setBackground(final String url){
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap= Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(url)
                                .submit(500,500)
                                .get();
                        if (bitmap !=null){
                            Palette p = Palette.from(bitmap).generate();
                            mMutedColor = p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
                            linearLayout.setBackgroundColor(mMutedColor);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }


    private void snackBarVideo() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "Error on video!", 3000)
                .setActionTextColor(Color.RED);

        snackbar.show();
    }
    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideNavigation() {
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showNavigation() {
        View decorView = getWindow().getDecorView();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (videoView_apod != null && !noVideo){
            bundle.putInt("current", player.getCurrentTimeMillis());
        }
    }





}
