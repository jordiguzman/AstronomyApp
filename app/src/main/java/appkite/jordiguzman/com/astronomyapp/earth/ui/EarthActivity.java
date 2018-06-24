package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.earth.adapter.AdapterEarth;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiClientEarth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiInterfaceEarth;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthDetailActivity.dateApi;

public class EarthActivity extends AppCompatActivity implements AdapterEarth.ItemClickListenerEarth {


    public static ArrayList<Earth> earthArrayList = new ArrayList<>();
    public static int itemPositionEarth;
    @BindView(R.id.iv_earth)
    ImageView iv_earth;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);
        ButterKnife.bind(this);



        mRecyclerView= findViewById(R.id.rv_earth);
        Glide.with(this)
                .load(AdapterMain.URL_MAIN[1])
                .into(iv_earth);

        if (earthArrayList.isEmpty()){
            preloadPictures();
            getDataEarth();
        }else {
            populateData();
            convertDate();
        }







    }
    public void convertDate(){
        String dateTemp = earthArrayList.get(0).getDate().substring(0, 10);
        int a = 4;
        int b = 7;
        dateApi = new StringBuilder(dateTemp).replace(a, a+1, "/").replace(b, b+1, "/").toString().concat("/");
    }

    public  void getDataEarth() {
        final ApiInterfaceEarth mApiInterfaceEarth = ApiClientEarth.getClientEarth().create(ApiInterfaceEarth.class);
        Call<List<Earth>> call = mApiInterfaceEarth.getDataEarth();
        call.enqueue(new Callback<List<Earth>>() {
            @Override
            public void onResponse(@NonNull Call<List<Earth>> call, @NonNull retrofit2.Response<List<Earth>> response) {
                switch (response.code()) {
                    case 200:
                        earthArrayList = (ArrayList<Earth>) response.body();
                         if (earthArrayList != null){
                             populateData();
                             convertDate();
                         }

                        break;
                    default:
                        Log.e("Error API", response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Earth>> call, @NonNull Throwable t) {
                Log.e("On Failure", t.getMessage());

            }
        });
    }

    private  void populateData() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        AdapterEarth adapterEarth = new AdapterEarth(this, earthArrayList, this);
        mRecyclerView.setAdapter(adapterEarth);
        mRecyclerView.setHasFixedSize(true);
    }

    private void preloadPictures() {
        for (int i=0; i< earthArrayList.size(); i++){
            Glide.with(this)
                    .load(earthArrayList.get(i).getImage())
                    .preload(300,300);
        }
    }


    @Override
    public void onClickItem(int position) {
        itemPositionEarth = position;
        Intent intent = new Intent(this, EarthDetailActivity.class);
        startActivity(intent);
    }
}
