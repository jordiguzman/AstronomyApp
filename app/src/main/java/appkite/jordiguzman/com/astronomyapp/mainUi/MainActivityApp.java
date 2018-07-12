package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;
import appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity;
import appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity;
import appkite.jordiguzman.com.astronomyapp.iss.ui.MapsActivity;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.CheckOnLine;
import appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.apod.service.ApiClientApod.API_KEY;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.URL_PLANETS;
import static appkite.jordiguzman.com.astronomyapp.widget.WidgetAstronomyApp.name;
import static appkite.jordiguzman.com.astronomyapp.widget.WidgetAstronomyApp.url;

public class MainActivityApp extends AppCompatActivity  implements AdapterMain.ItemClickListener{

    @BindView(R.id.iv_main)
    ImageView iv_main;
    @BindView(R.id.collapsing_main)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.coordinator_list_activity)
    CoordinatorLayout mCoordinatorLayout;
    private CheckOnLine checkOnLIne;
    public static String urlToWidget;
    @SuppressLint("StaticFieldLeak")
    public static TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        ButterKnife.bind(this);


         checkOnLIne = new CheckOnLine(this);
        if (!checkOnLIne.isOnline()){
            showSnackbar();
            return;
        }

        RecyclerView mRecyclerView = findViewById(R.id.rv_main);
        if (isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2 ));
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        }

        AdapterMain mAdapterMain = new AdapterMain(this, MainActivityApp.this);
        mRecyclerView.setAdapter(mAdapterMain);
        mRecyclerView.setHasFixedSize(true);
        mAdapterMain.notifyDataSetChanged();


        Glide.with(this)
                .load(Splash.URL)
                .into(iv_main);

        imageCollapsingToolBar();


         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 preLoadImagesSystem();
             }
         });

        String urlApi = "https://api.nasa.gov/planetary/apod?api_key=";
        new GetHttpAsyncTaskApodForWidget(this).execute(urlApi + API_KEY);

    }

    public static boolean isTablet(Context context){

        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    static class GetHttpAsyncTaskApodForWidget extends AsyncTask<String, Void, String>{

        String serverResponse;
        @SuppressLint("StaticFieldLeak")
        private Context mContext;

        GetHttpAsyncTaskApodForWidget(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    serverResponse = converInputStreamToString(urlConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return serverResponse;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                name = jsonObject.getString("title");
                url = jsonObject.getString("url");
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("url", url);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String converInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)result.append(line);

        inputStream.close();
        return result.toString();
    }


    private void showSnackbar() {
        Snackbar mSnackbar = Snackbar
                .make(mCoordinatorLayout, getResources().getString(R.string.no_network), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkOnLIne.isOnline()) {
                            showSnackbar();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Splash.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
        mSnackbar.setActionTextColor(Color.RED);
        View sbView = mSnackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        mSnackbar.show();
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
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    private void showSnackbarFinish() {
        Snackbar mSnackbar = Snackbar
                .make(mCoordinatorLayout, getResources().getString(R.string.finish), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.exit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCache(getApplicationContext());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        }else {
                            finish();
                        }

                    }
                });
        mSnackbar.setActionTextColor(Color.RED);
        View sbView = mSnackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        mSnackbar.show();
    }
    @Override
    public void onBackPressed() {
        showSnackbarFinish();
    }
}
