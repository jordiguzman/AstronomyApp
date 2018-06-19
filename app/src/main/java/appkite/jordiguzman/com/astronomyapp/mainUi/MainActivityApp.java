package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiClientEarth;
import appkite.jordiguzman.com.astronomyapp.earth.service.ApiInterfaceEarth;
import appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity;
import appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity;
import appkite.jordiguzman.com.astronomyapp.iss.ui.MapsActivity;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.abstractData;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.credits;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.date;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.name;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.urlImage;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.urlThumbnail;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.URL_PLANETS;

public class MainActivityApp extends AppCompatActivity  implements AdapterMain.ItemClickListener{




    @BindView(R.id.iv_main)
    ImageView iv_main;
    @BindView(R.id.collapsing_main)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar pb_main;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        ButterKnife.bind(this);

        RecyclerView mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        AdapterMain mAdapterMain = new AdapterMain(this, MainActivityApp.this);
        mRecyclerView.setAdapter(mAdapterMain);
        mRecyclerView.setHasFixedSize(true);
        mAdapterMain.notifyDataSetChanged();



        Glide.with(this)
                .load(Splash.URL)
                .into(iv_main);

        imageCollapsingToolBar();


        MainActivityApp.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /*getDataEarth();
                try {
                    populateArray();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

            }

        });
        new Thread(new Runnable() {
            @Override
            public void run() {

                preLoadImagesSystem();
            }
        });



    }


    private void preLoadImagesSystem() {
        for (String URL_PLANET : URL_PLANETS) {
            Glide.with(this)
                    .load(URL_PLANET)
                    .preload();
        }

    }








    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }

    /**
     * *******************************APOD *************************************
     *
     */
    /*public static void getDataApod(final Context context){
        final ApiIntefaceApod mApiInteface = ApiClientApod.getClient().create(ApiIntefaceApod.class);
        Call<List<Apod>> call = mApiInteface.getData(ApiClientApod.API_KEY, String.valueOf(dateOld), String.valueOf(today));
        call.enqueue(new Callback<List<Apod>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)

            @Override
            public void onResponse(@NonNull Call<List<Apod>> call, @NonNull Response<List<Apod>> response) {
                switch (response.code()){
                    case 200:
                        mApodData = (ArrayList<Apod>) response.body();
                        if (mApodData != null){
                            Collections.reverse(mApodData);

                        }
                        break;

                    default:
                        Toast.makeText(context, "Error api", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Apod>> call, @NonNull Throwable t) {
                Log.e("OnFailure", t.getMessage());
            }


        });
    }*/


    /**
     * *******************************EARTH *************************************
     *
     */
    public static void getDataEarth() {
        final ApiInterfaceEarth mApiInterfaceEarth = ApiClientEarth.getClientEarth().create(ApiInterfaceEarth.class);
        Call<List<Earth>> call = mApiInterfaceEarth.getDataEarth();
        call.enqueue(new Callback<List<Earth>>() {
            @Override
            public void onResponse(@NonNull Call<List<Earth>> call, @NonNull retrofit2.Response<List<Earth>> response) {
                switch (response.code()) {
                    case 200:
                        earthArrayList = (ArrayList<Earth>) response.body();


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

    /**
     * *******************************HUBBLE *************************************
     *
     */
    public void populateArray() throws ExecutionException, InterruptedException {
        for (int i= 1; i< 27; i++){
            new HttpAsyncTaskDataHubble().execute(HubbleActivity.BASE_URL + i).get();
        }

    }

    public static String GETDataHubble(String url){
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null){
                result = converInputStreamToString(inputStream);
            }else {
                result = "Error";
            }
        }catch (Exception e){
            e.getMessage();
        }
        return  result;
    }
    private static String converInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)result.append(line);

        inputStream.close();
        return result.toString();
    }

    static class HttpAsyncTaskDataHubble extends AsyncTask<String, Void, String > {



        @Override
        protected String doInBackground(String... strings) {
            return GETDataHubble(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null){
                    JSONObject jsonObject = new JSONObject(result);
                    String nameResult = jsonObject.getString("name");
                    String urlThumb = jsonObject.getString("thumbnail");
                    String urlImageResult = jsonObject.getString("keystone_image_2x");
                    String dateResult = jsonObject.getString("publication");
                    String abstractDate = jsonObject.getString("abstract");
                    String creditsResult = jsonObject.getString("credits");
                    name.add(nameResult);
                    urlThumbnail.add(urlThumb);
                    urlImage.add(urlImageResult);
                    date.add(dateResult);
                    abstractData.add(abstractDate);
                    credits.add(creditsResult);
                }
            }catch (JSONException e){
                e.getMessage();
            }

        }
    }


    @Override
    public void onClickItem(int position) {
        switch (position){
            case 0:
                Intent intent = new Intent(this, ApodActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, EarthActivity.class);
                startActivity(intent1);
                break;

            case 2:
                Intent intent2 = new Intent(this, SolarSystemActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, MapsActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, HubbleActivity.class);
                startActivity(intent4);
                break;
        }
    }

}
