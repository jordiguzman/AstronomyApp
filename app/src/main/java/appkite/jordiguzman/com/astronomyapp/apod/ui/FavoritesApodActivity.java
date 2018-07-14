package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodDbHelper;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesApodActivity extends AppCompatActivity implements AdapterApodFavorites.ItemClickListenerApodFavorites,
        LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.tv_title_apod)
    TextView title_apod;
    @BindView(R.id.iv_item_apod)
    ImageView iv_apod;
    @BindView(R.id.ib_menu_acticity_apod)
    ImageButton ib_menu;
    @BindView(R.id.pb_apod_activity)
    ProgressBar pb_apod_activity;
    @BindView(R.id.collapsing_apod)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.tv_nodata_favorites)
    TextView tv_nodata_favorites;
    private RecyclerView mRecyclerView;
    public static ArrayList<String[]> apodArrayList = new ArrayList<>();
    public static String [][] dataLoadedApod;
    public static int itemPositionFavorites;
    private ApodDbHelper db;

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

        getSupportLoaderManager().initLoader(1, null,  this);
        populateRecyclerView();
    }


    private void populateRecyclerView() {
        checkApodArrayIsEmpty();
        AdapterApodFavorites adapterApodFavorites = new AdapterApodFavorites(apodArrayList, this, FavoritesApodActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterApodFavorites);
        mRecyclerView.swapAdapter(adapterApodFavorites, true);
        adapterApodFavorites.notifyDataSetChanged();

    }

    private void checkApodArrayIsEmpty() {
        if (apodArrayList.isEmpty()){
            tv_nodata_favorites.setVisibility(View.VISIBLE);
        }else {
            tv_nodata_favorites.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        apodArrayList.clear();
        Uri uri = ApodContract.ApodEntry.CONTENT_URI;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                apodArrayList.add(new String[]{
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_EXPLANATION)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_COPYRIGHT)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_HURL)),
                        cursor.getString(cursor.getColumnIndex(ApodContract.ApodEntry._ID))});
            }
            Collections.reverse(apodArrayList);
            dataLoadedApod = apodArrayList.toArray(new String[apodArrayList.size()][5]);
            cursor.close();

        }
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        AdapterApodFavorites adapterApodFavorites = new AdapterApodFavorites(apodArrayList, this, FavoritesApodActivity.this);

        mRecyclerView.swapAdapter(adapterApodFavorites, true);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    @Override
    public void onClickItem(int position) {
        itemPositionFavorites = position;
        Intent intent = new Intent(this, FavoritesApodDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getSupportLoaderManager().initLoader(1, null,  this);
        populateRecyclerView();
    }



}
