package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.widget.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.FavoritesApodActivity.dataLoadedApod;

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




        preloadPicture();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
            isFavorited = bundle.getBoolean("isFavorited");

        }

        populatePictureVideo();
        ib_image_apod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                shareImage(getApplicationContext());
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
                .load(mApodData.get(position).getHdurl())
                .fetch();
    }


    public void shareImage(final Context context){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Picasso.get()
                .load(mApodData.get(position).getUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                        context.startActivity(Intent.createChooser(i, "Share Image"));

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });


    }
    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void populatePictureVideo(){
        String url_base_embed = "https://www.youtube.com/embed/";
        String url;
        if (isFavorited){
            url = dataLoadedApod[position][4];
        }else {
            url = mApodData.get(position).getUrl();
        }

        int length = url.length();
        String result = url.substring(length - 3, length);
        if (!result.equals("jpg")) {
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


        } else {
            noVideo=true;
            if (isFavorited){
                setBackground(dataLoadedApod[position][5]);
                Glide.with(this)
                        .load(dataLoadedApod[position][5])
                        .into(iv_apod_image);


            }else {
                setBackground(mApodData.get(position).getUrl());
                Glide.with(this)
                        .load(mApodData.get(position).getUrl())
                        .into(iv_apod_image);

            }

        }
    }
    private void setBackground(final String url){
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap= GlideApp.with(getApplicationContext())
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostResume() {
        super.onPostResume();

    }



}
