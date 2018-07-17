package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.utils.ShareImageHubble;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.dataImagesDetail;

public class ImageHubbleActivity extends AppCompatActivity {

    @BindView(R.id.iv_image_hubble)
    ImageView iv_apod_image;
    @BindView(R.id.ib_image_hubble)
    ImageButton ib_image_apod;
    private int position, mMutedColor;
    private boolean isfavorited, hideButtonNavigation;
    private LinearLayout linearLayout;
    private ShareImageHubble shareImageHubble = new ShareImageHubble();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_hubble);
        ButterKnife.bind(this);
        showNavigation();
        linearLayout = findViewById(R.id.linearLayout_activity_image_hubble);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            position = bundle.getInt("position");
            isfavorited = bundle.getBoolean("isFavorited");
        }


        populateImage();
        ViewCompat.setTransitionName(iv_apod_image, "image");
        ib_image_apod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                shareImageHubble.shareImage(getApplicationContext(), dataImagesDetail.get(position).getImage());
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

    public void populateImage(){
        if (isfavorited){
            setBackground(HubbleActivity.dataImagesDetail.get(position).getImage());
            Glide.with(this)
                    .load(HubbleActivity.dataImagesDetail.get(position).getImage())
                    .into(iv_apod_image);
        }else {
            setBackground(dataImagesDetail.get(position).getImage());
            Glide.with(this)
                    .load(dataImagesDetail.get(position).getImage())
                    .into(iv_apod_image);
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

}
