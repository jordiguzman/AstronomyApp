package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.earth.adapter.AdapterEarth;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthActivity extends AppCompatActivity implements AdapterEarth.ItemClickListenerEarth {


    public static ArrayList<Earth> earthArrayList = new ArrayList<>();
    public static int itemPositionEarth;
    @BindView(R.id.iv_earth)
    ImageView iv_earth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[1])
                .into(iv_earth);

        RecyclerView mRecyclerView = findViewById(R.id.rv_earth);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        AdapterEarth adapterEarth = new AdapterEarth(this, earthArrayList, this);
        mRecyclerView.setAdapter(adapterEarth);
        mRecyclerView.setHasFixedSize(true);

        preloadPictures();
    }

    private void preloadPictures() {
        for (int i=0; i< earthArrayList.size(); i++){
            Glide.with(this)
                    .load(earthArrayList.get(i).getImage())
                    .preload();
        }
    }


    @Override
    public void onClickItem(int position) {
        itemPositionEarth = position;
        Intent intent = new Intent(this, EarthDetailActivity.class);
        startActivity(intent);
    }
}
