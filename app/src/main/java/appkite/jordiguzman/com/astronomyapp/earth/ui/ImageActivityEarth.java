package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import appkite.jordiguzman.com.astronomyapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthDetailActivity.dateApi;

public class ImageActivityEarth extends AppCompatActivity {

    public static int indexEarth;
    @BindView(R.id.iv_image_earth)
    PhotoView iv_image_earth;
    @BindView(R.id.ib_image_earth)
    ImageButton ib_image_earth;
    private boolean hideButtonNavigation;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_earth);
        ButterKnife.bind(this);
        showNavigation();
        Glide.with(this)
                .load(setPicture(indexEarth))
                .error(Glide.with(this)
                        .load(setPicture(indexEarth)))
                .preload(600,600);
        Glide.get(this)
                .setMemoryCategory(MemoryCategory.HIGH);
        Glide.with(this)
                .load(setPicture(indexEarth))
                .apply(new RequestOptions().override(900,900)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_image_earth);
        ViewCompat.setTransitionName(iv_image_earth, "image");

        ib_image_earth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                shareImageEarth(getApplicationContext());

            }
        });
        iv_image_earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hideButtonNavigation){
                    Animation up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
                    ib_image_earth.startAnimation(up);
                    ib_image_earth.setVisibility(View.INVISIBLE);
                    hideNavigation();
                    hideButtonNavigation = true;
                }else {
                    Animation down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down);
                    ib_image_earth.startAnimation(down);
                    ib_image_earth.setVisibility(View.VISIBLE);
                    showNavigation();
                    hideButtonNavigation = false;
                }
            }
        });

    }

    public static String setPicture(int index){
        String url = "https://epic.gsfc.nasa.gov/archive/natural/";
        return url + dateApi + "png/" + earthArrayList.get(index).getImage() +
                ".png";
    }



    static public void shareImageEarth(final Context context){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Picasso.get()
                .load(setPicture(indexEarth))
                .priority(Picasso.Priority.HIGH)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                        Intent chooserIntent = Intent.createChooser(i, "Share Image");
                        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(chooserIntent);

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        e.getMessage();
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
    }
     public static Uri getLocalBitmapUri(Bitmap bmp, Context context) {
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
    public void hideNavigation() {
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | SYSTEM_UI_FLAG_IMMERSIVE);
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
                        | SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
