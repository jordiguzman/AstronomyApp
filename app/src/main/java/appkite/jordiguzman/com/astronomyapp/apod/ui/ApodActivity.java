package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;

public class ApodActivity extends AppCompatActivity {

    public static ArrayList<Apod> mApodData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        setTitle(getString(R.string.apod_title));



        RecyclerView mRecyclerView = findViewById(R.id.rv_apod);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        AdapterApod mAdapterApod = new AdapterApod(mApodData,  this);
        mRecyclerView.setAdapter(mAdapterApod);
        mRecyclerView.setHasFixedSize(true);

    }


}
