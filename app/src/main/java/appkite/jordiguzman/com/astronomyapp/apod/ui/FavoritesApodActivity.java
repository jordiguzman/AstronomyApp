package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class FavoritesApodActivity extends AppCompatActivity implements AdapterApodFavorites.ItemClickListenerApodFavorites {

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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteItem(position);
                reloadAfterDelete();
                snackBarDelete();

            }
        }).attachToRecyclerView(mRecyclerView);

    }

    private void snackBarDelete() {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getResources().getString( R.string.data_deleted), Snackbar.LENGTH_LONG );
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(ContextCompat.getColor(this,  R.color.colorAccent));
        snackbar.show();
    }
    private void deleteItem(int position){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = ApodContract.ApodEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(FavoritesApodActivity.dataLoadedApod[position][6]).build();
        contentResolver.delete(uri, null, null);
        populateRecyclerView();
    }


    private void populateRecyclerView() {
        if (apodArrayList.isEmpty()){
            tv_nodata_favorites.setVisibility(View.VISIBLE);
        }else {
            tv_nodata_favorites.setVisibility(View.INVISIBLE);
        }
        AdapterApodFavorites adapterApodFavorites = new AdapterApodFavorites(apodArrayList, this, FavoritesApodActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterApodFavorites);

    }

    public void loadData(){
        apodArrayList.clear();
        dataLoadedApod =null;
        Cursor mCursor = getContentResolver().query(ApodContract.ApodEntry.CONTENT_URI, null
        ,null, null,
                ApodContract.ApodEntry._ID);
        if (mCursor != null){
            while (mCursor.moveToNext()) {
                apodArrayList.add(new String[]{
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_TITLE)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_DATE)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_EXPLANATION)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_COPYRIGHT)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_URL)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry.COLUMN_HURL)),
                        mCursor.getString(mCursor.getColumnIndex(ApodContract.ApodEntry._ID))});
            }
            Collections.reverse(apodArrayList);
            dataLoadedApod = apodArrayList.toArray(new String[apodArrayList.size()][5]);
            mCursor.close();
        }
    }

    public void reloadAfterDelete(){
        loadData();
        populateRecyclerView();
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
        reloadAfterDelete();
    }
}
