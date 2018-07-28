package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.adapter.AdapterHubbleFavorites;
import appkite.jordiguzman.com.astronomyapp.hubble.data.AppDatabaseHubble;
import appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleEntry;
import appkite.jordiguzman.com.astronomyapp.hubble.data.MainViewModelHubble;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleDetailFragment.names;

public class FavoritesHubbleActivity extends AppCompatActivity implements AdapterHubbleFavorites.ItemClickListenerHubbleFavorites {

    @BindView(R.id.tv_title_hubble)
    TextView tv_title_hubble;
    @BindView(R.id.ib_menu_activity_hubble)
    ImageButton ib_menu;
    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;
    @BindView(R.id.tv_nodata_favorites_hubble)
    TextView tv_nodata;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_hubble)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    public static int itemPositionFavoritesHubble;
    public static List<HubbleEntry> mHubbleDataList;
    private AdapterHubbleFavorites adapterHubbleFavorites;
    private AppDatabaseHubble mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        mRecyclerView = findViewById(R.id.rv_hubble);
        ib_menu.setVisibility(View.INVISIBLE);
        tv_title_hubble.setText(R.string.favorites);
        imageCollapsingToolBar();
        mDb = AppDatabaseHubble.getInstance(this);
        setupViewModelHubble();
        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);
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
                        List<HubbleEntry> hubbleEntries = adapterHubbleFavorites.getHubbleData();
                        mDb.hubbleDao().deleteHubble(hubbleEntries.get(position));
                        if (!names.isEmpty())names.remove(position);
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setupViewModelHubble(){
        MainViewModelHubble viewModelHubble = ViewModelProviders.of(this).get(MainViewModelHubble.class);
        viewModelHubble.getHubbleData().observe(this, new Observer<List<HubbleEntry>>() {
            @Override
            public void onChanged(@Nullable List<HubbleEntry> hubbleEntries) {
                mHubbleDataList = hubbleEntries;
                adapterHubbleFavorites.setHubbleData(hubbleEntries);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }

    private void populateRecyclerView() {
        adapterHubbleFavorites = new AdapterHubbleFavorites(mHubbleDataList, this, FavoritesHubbleActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterHubbleFavorites);
        mRecyclerView.swapAdapter(adapterHubbleFavorites, true);
        adapterHubbleFavorites.notifyDataSetChanged();
    }

    public void checkApodArrayIsEmpty() {
        if (names.isEmpty()){
            tv_nodata.setVisibility(View.VISIBLE);
        }else {
            tv_nodata.setVisibility(View.INVISIBLE);
        }
    }
    public void reloadAfterDelete(){

        populateRecyclerView();
    }

    @Override
    public void onClickItem(int position) {
        itemPositionFavoritesHubble = position;
        Intent intent = new Intent(this, FavoritesHubbleDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkApodArrayIsEmpty();
        reloadAfterDelete();
    }
}
