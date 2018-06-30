package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesHubbleActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_hubble)
    TextView tv_title_hubble;
    @BindView(R.id.ib_menu_activity_hubble)
    ImageButton ib_menu;
    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;
    @BindView(R.id.tv_nodata_favorites_hubble)
    TextView tv_nodata;
    private RecyclerView mRecyclerView;

    public static ArrayList<String[]> hubbleArrayList = new ArrayList<>();
    public static String [][] dataLoadedHubble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        mRecyclerView = findViewById(R.id.rv_hubble);
        ib_menu.setVisibility(View.INVISIBLE);
        tv_title_hubble.setText(R.string.favorites);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);
        loadData();
        populateRecyclerView();
    }

    private void populateRecyclerView() {
        if (hubbleArrayList.isEmpty()){
            tv_nodata.setVisibility(View.VISIBLE);
        }else{
            tv_nodata.setVisibility(View.INVISIBLE);
        }
    }

    private void loadData() {
        hubbleArrayList.clear();
        dataLoadedHubble = null;
        Cursor cursor = getContentResolver().query(HubbleContract.HubbleEntry.CONTENT_URI, null,
                null,  null,
                HubbleContract.HubbleEntry._ID);
        if (cursor != null){
            while (cursor.moveToNext()){
                hubbleArrayList.add(new String[]{

                        cursor.getString(cursor.getColumnIndex(HubbleContract.HubbleEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(HubbleContract.HubbleEntry.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(HubbleContract.HubbleEntry.COLUMN_CREDITS)),
                        cursor.getString(cursor.getColumnIndex(HubbleContract.HubbleEntry.COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(HubbleContract.HubbleEntry.COLUMN_ID))});
            }
            dataLoadedHubble = hubbleArrayList.toArray(new String[hubbleArrayList.size()][4]);
            cursor.close();
        }

    }
}
