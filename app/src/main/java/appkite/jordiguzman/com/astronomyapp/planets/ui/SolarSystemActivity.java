package appkite.jordiguzman.com.astronomyapp.planets.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.planets.adapter.AdapterSolarSystem;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp.isTablet;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.BASE_URL_EXTRACT;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.PLANETS_API;

public class SolarSystemActivity extends AppCompatActivity implements AdapterSolarSystem.ItemClickListenerSystem {

    public static ArrayList<String> wikiPlanetsText = new ArrayList<>();

    @BindView(R.id.iv_system)
    ImageView iv_system;
    @BindView(R.id.rv_system)
    RecyclerView mRecyclerView;
    @BindView(R.id.collapsing_system)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    public static int itemPositionSolar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_planets);
        ButterKnife.bind(this);
        imageCollapsingToolBar();

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[2])
                .into(iv_system);

        setupRecyclerView();
        SolarSystemActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wikiApiText();
            }
        });







    }
    public void setupRecyclerView(){
        if (isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        AdapterSolarSystem adapterSolarSystem = new AdapterSolarSystem(this, this);
        mRecyclerView.setAdapter(adapterSolarSystem);
        mRecyclerView.setHasFixedSize(true);
    }


    private void wikiApiText(){
        for (String aTITLE : PLANETS_API) {
            new HttpAsyncTaskText().execute(BASE_URL_EXTRACT + aTITLE);

        }

    }


    private static String converInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)result.append(line);

        inputStream.close();
        return result.toString();
    }

    @Override
    public void onClickItem(int position) {
        itemPositionSolar = position;
        Intent intent = new Intent(this, SolarSystemDetailActivity.class);
        startActivity(intent);

    }
    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }



    @SuppressLint("StaticFieldLeak")
    static class HttpAsyncTaskText extends AsyncTask<String, Void, String>{

        String serverResponse;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode= urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    serverResponse = converInputStreamToString(urlConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            StringBuilder textWiki;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject query = jsonObject.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");

                String[] strings = pages.toString().substring(0,20).split(":");

                String string = strings[0];

                String pageid = string.substring(2, string.length()-1);

                JSONObject page = pages.getJSONObject(pageid);
                textWiki = new StringBuilder((String) page.get("extract"));

                String[] strings1 = textWiki.toString().split("=");

                textWiki = new StringBuilder();

                for (String temp:strings1){

                    textWiki.append("\n").append(temp.trim());
                }

                if (textWiki.length() > 0){
                    String textHtml = String.valueOf(Html.fromHtml(textWiki.toString()));
                    wikiPlanetsText.add(textHtml);

                }else {
                    Log.e("Error", "");
                }

            }catch (JSONException e){
                e.getMessage();
            }
        }
    }


}