package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApodFavorites;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesApodActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_apod)
    TextView title_apod;
    @BindView(R.id.iv_apod)
    ImageView iv_apod;
    @BindView(R.id.ib_menu_acticity_apod)
    ImageButton ib_menu;
    @BindView(R.id.pb_apod_activity)
    ProgressBar pb_apod_activity;
    @BindView(R.id.collapsing_apod)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;

    private RecyclerView mRecyclerView;
    private ArrayList<String> apodArrayListTemp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.rv_apod);


        ib_menu.setVisibility(View.INVISIBLE);
        title_apod.setText(R.string.favorites);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[0])
                .into(iv_apod);
        loadData();
        populateRecyclerView();
        verData();
    }

    private void verData(){
        for (int i = 0; i < apodArrayListTemp.size(); i++){
            Log.i("Data", apodArrayListTemp.get(i) + "\n");
        }
    }
    private void populateRecyclerView() {
        AdapterApodFavorites adapterApodFavorites = new AdapterApodFavorites(apodArrayListTemp, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterApodFavorites);
    }

    public void loadData(){

        Cursor mCursor = getContentResolver().query(ApodContract.ApodEntry.CONTENT_URI, null
        ,null, null,
                ApodContract.ApodEntry._ID);
        int index = 0;
        if (mCursor != null){
            while (mCursor.moveToNext()) {
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_TITLE)));
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_DATE)));
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_EXPLANATION)));
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_COPYRIGHT)));
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_URL)));
                apodArrayListTemp.add(index, mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_HURL)));
                Collections.reverse(apodArrayListTemp);
                index++;
            }
            mCursor.close();


        }
    }

}
