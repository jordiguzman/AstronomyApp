package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.adapter.AdapterHubble;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HubbleActivity extends AppCompatActivity implements AdapterHubble.ItemClickListenerHubble{

    public static final String BASE_URL = "http://hubblesite.org/api/v3/news_release/2018-";
    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> urlThumbnail = new ArrayList<>();
    public static ArrayList<String> urlImage = new ArrayList<>();
    public static ArrayList<String> date = new ArrayList<>();
    public static ArrayList<String> abstractData = new ArrayList<>();
    public static ArrayList<String> credits = new ArrayList<>();

    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;
    public static int itemPositionHubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);



        setupRecyclerView();


    }

    public  void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_hubble);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AdapterHubble adapterHubble = new AdapterHubble(this, name, this);
        mRecyclerView.setAdapter(adapterHubble);
        mRecyclerView.setHasFixedSize(true);

    }


    @Override
    public void onClickItem(int position) {
        itemPositionHubble = position;
        Intent intent = new Intent(this, HubbleDetailActivity.class);
        startActivity(intent);

    }
}
