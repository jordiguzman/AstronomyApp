package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import appkite.jordiguzman.com.astronomyapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.itemPositionEarth;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class EarthDetailActivity extends AppCompatActivity  {

    @BindView(R.id.iv_detail_earth)
    ImageView iv_detail_earth;
    @BindView(R.id.btn_adelante)
    ImageButton btn_adelante;
    @BindView(R.id.btn_animacion)
    ImageButton btn_animacion;
    @BindView(R.id.tv_distance_earth)
    TextView tv_distance_earth;
    @BindView(R.id.tv_distance_sun)
    TextView tv_disyance_sun;
    private String URL = "https://epic.gsfc.nasa.gov/epic-archive/jpg/";
    private int index =0;
    private String url;
    private boolean isStop;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_detail);
        ButterKnife.bind(this);


        url = URL + earthArrayList.get(itemPositionEarth).getImage() + ".jpg";
        index = itemPositionEarth;
        //tv_distance_earth.setText(String.valueOf(earthArrayList.get(index).getDistanceEarth()).substring(0,7).concat(" km"));
        //Log.i("Distancia", String.valueOf(earthArrayList.get(index).getDistanceToEarth().get(index).toString()));
        Glide.with(this)
                .load(url)
                .into(iv_detail_earth);

        btn_adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url= URL + earthArrayList.get(index).getImage() + ".jpg";
                Glide.with(getApplicationContext())
                            .load(url)
                            .into(iv_detail_earth);
                index++;
                if (index> earthArrayList.size()-1)index=0;
            }
        });
        btn_animacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStop){
                    btn_animacion.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fast_forward_black_24dp));
                    isStop = false;
                    mThread.interrupt();

                }else {
                    btn_animacion.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_black_24dp));
                    isStop = true;
                    animateEarth();
                }
            }
        });

    }

    public void animateEarth(){
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
                                url = URL + earthArrayList.get(index).getImage() + ".jpg";
                                Glide.with(getApplicationContext())
                                        .load(url)
                                        .transition(withCrossFade(400))
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

}
