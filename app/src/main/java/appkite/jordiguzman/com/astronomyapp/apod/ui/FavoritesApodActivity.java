package appkite.jordiguzman.com.astronomyapp.apod.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApodFavorites;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodEntry;
import appkite.jordiguzman.com.astronomyapp.apod.data.AppDatabase;
import appkite.jordiguzman.com.astronomyapp.apod.data.MainViewModel;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodDetailFragment.dates;

public class FavoritesApodActivity extends AppCompatActivity implements AdapterApodFavorites.ItemClickListenerApodFavorites{

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
    public static int itemPositionFavorites;
    public static List<ApodEntry> mApodDataList;
    private AdapterApodFavorites adapterApodFavorites;
    private AppDatabase mDb;


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
        mDb = AppDatabase.getInstance(this);
        setupViewModel();
        checkApodArrayIsEmpty();
        populateRecyclerView();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<ApodEntry> apodEntries = adapterApodFavorites.getApodData();
                        mDb.apodDao().deleteApod(apodEntries.get(position));
                        if (!dates.isEmpty()) dates.remove(position);
                        snackBarDelete();

                    }
                });



            }
        }).attachToRecyclerView(mRecyclerView);

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getApodData().observe(this, new Observer<List<ApodEntry>>() {
            @Override
            public void onChanged(@Nullable List<ApodEntry> apodEntries) {
                mApodDataList = apodEntries;
                adapterApodFavorites.setApodData(apodEntries);

            }
        });

    }

    private void snackBarDelete() {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getResources().getString( R.string.data_deleted), Snackbar.LENGTH_LONG );
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(ContextCompat.getColor(this,  R.color.colorAccent));
        snackbar.show();
    }
    private void populateRecyclerView() {
        adapterApodFavorites= new AdapterApodFavorites(mApodDataList, this, FavoritesApodActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterApodFavorites);
        mRecyclerView.swapAdapter(adapterApodFavorites, true);
        adapterApodFavorites.notifyDataSetChanged();

    }

    public void checkApodArrayIsEmpty() {
        if (dates.isEmpty()){
            tv_nodata_favorites.setVisibility(View.VISIBLE);
        }else {
            tv_nodata_favorites.setVisibility(View.INVISIBLE);
        }
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
        checkApodArrayIsEmpty();
        populateRecyclerView();
    }



}
