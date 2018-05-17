package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;

public class ApodActivity extends AppCompatActivity {

    public static ArrayList<Apod> mApodData = new ArrayList<>();
    private  RecyclerView mRecyclerView;
    private AdapterApod mAdapterApod;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        setTitle(getString(R.string.apod_title));



        mRecyclerView= findViewById(R.id.rv_apod);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapterApod = new AdapterApod(mApodData,  this);
        new AsynTask().execute("");

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
