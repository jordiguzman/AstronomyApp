package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApodActivity extends AppCompatActivity implements AdapterApod.ItemClickListenerApod{

    public static ArrayList<Apod> mApodData = new ArrayList<>();
    private  RecyclerView mRecyclerView;
    private AdapterApod mAdapterApod;
    @BindView(R.id.iv_apod)
    ImageView iv_apod;
    @BindView(R.id.fab_settings)
    FloatingActionButton mFloatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        ButterKnife.bind(this);

        MainActivityApp.getDataApod(this);

        Glide.with(this)
                .load(mApodData.get(0).getUrl())
                .into(iv_apod);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Has tocado el FloatingButton", Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView= findViewById(R.id.rv_apod);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapterApod = new AdapterApod(mApodData,  this, this);
        new AsynTask().execute("");

    }



    @Override
    public void onClickItem(int position) {
        switch (position){
            case 0:
                Intent intent = new Intent(this, ApodDetailActivity.class);
                startActivity(intent);
                break;

        }

    }

    @SuppressLint("StaticFieldLeak")
    class AsynTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {

            mRecyclerView.setAdapter(mAdapterApod);
            mRecyclerView.setHasFixedSize(true);
            return null;
        }


    }




}
