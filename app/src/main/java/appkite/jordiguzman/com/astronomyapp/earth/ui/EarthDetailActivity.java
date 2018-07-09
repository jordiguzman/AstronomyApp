package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;

import appkite.jordiguzman.com.astronomyapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.itemPositionEarth;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.ImageActivityEarth.indexEarth;

public class EarthDetailActivity extends AppCompatActivity  {


    @BindView(R.id.btn_step_forward)
    ImageButton btn_step_forward;
    @BindView(R.id.btn_animation)
    ImageButton btn_animation;
    @BindView(R.id.tv_distance_earth)
    TextView tv_distance_earth;
    @BindView(R.id.tv_distance_sun)
    TextView tv_distance_sun;
    @BindView(R.id.tv_caption)
    TextView tv_caption;
    ImageView iv_detail_earth;
    private int index =0;
    private boolean isStop;
    private Thread mThread;
    public static String dateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_detail);
        ButterKnife.bind(this);



        iv_detail_earth = findViewById(R.id.iv_detail_earth);
        index = itemPositionEarth;
        if (savedInstanceState!=null){
            index = savedInstanceState.getInt("index");
        }
        tv_distance_earth.setText(convertDoubleEarth(index));
        tv_distance_sun.setText(convertDoubleSun(index));
        tv_caption.setText(earthArrayList.get(index).getCaption());


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext())
                        .setMemoryCategory(MemoryCategory.HIGH);
                Glide.with(getApplicationContext())
                        .load(setPicture(index))
                        .into(iv_detail_earth);
            }
        });

    }


    public String convertDoubleEarth(int index){
        double distanceToEarthDouble = earthArrayList.get(index).getDistanceToEarth().getY();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(distanceToEarthDouble).concat(" km");
    }
    public String convertDoubleSun(int index){
        double distanceToEarthDouble = earthArrayList.get(index).getDistanceToSun().getY();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(distanceToEarthDouble).concat(" km");
    }

    public String setPicture(int index){
        String url = "https://epic.gsfc.nasa.gov/archive/natural/";
        return url + dateApi + "png/" + earthArrayList.get(index).getImage() +
                ".png";
    }
    public void stepForward(View view){
        index++;
        if (index> earthArrayList.size()-1)index=0;
        tv_distance_earth.setText(convertDoubleEarth(index));
        tv_distance_sun.setText(convertDoubleSun(index));


        Glide.with(this)
                .load(setPicture(index))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_detail_earth);
    }
    public void fastForward(View view){
        if (isStop){
            btn_animation.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fast_forward_black_24dp));
            isStop = false;
            mThread.interrupt();
            btn_step_forward.setClickable(true);

        }else {
            btn_animation.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_black_24dp));
            isStop = true;
            animateEarth();
        }
    }
    public void animateEarth(){
        btn_step_forward.setClickable(false);
        mThread = new Thread(){
            @Override
            public void run() {
                try {
                    while (index++ < earthArrayList.size()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (index > earthArrayList.size() - 1) index = 0;
                                tv_distance_earth.setText(convertDoubleEarth(index));
                                tv_distance_sun.setText(convertDoubleSun(index));

                                Glide.with(getApplicationContext())
                                        .load(setPicture(index))
                                        .apply(new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                                        .into(iv_detail_earth);

                            }
                        });
                    }
                }catch (InterruptedException e){
                    e.getMessage();
                }
            }
        };
        mThread.start();

    }
    public void clickPictureEarth(View view){
        indexEarth = index;
        Intent intent = new Intent(this, ImageActivityEarth.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                iv_detail_earth,
                "image");
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (iv_detail_earth != null){
            outState.putInt("index", index);
        }
    }
}
