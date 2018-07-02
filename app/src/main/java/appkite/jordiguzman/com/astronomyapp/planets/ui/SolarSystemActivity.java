package appkite.jordiguzman.com.astronomyapp.planets.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;

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

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import appkite.jordiguzman.com.astronomyapp.planets.adapter.AdapterSolarSystem;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.BASE_URL_EXTRACT;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.PLANETS_API;

public class SolarSystemActivity extends AppCompatActivity implements AdapterSolarSystem.ItemClickListenerSystem {

    public static ArrayList<String> wikiPlanetsText = new ArrayList<>();

    @BindView(R.id.iv_system)
    ImageView iv_system;
    @BindView(R.id.rv_system)
    RecyclerView mRecyclerView;
    public static int itemPositionSolar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_planets);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[2])
                .into(iv_system);

        setupRecyclerView();
        wikiApiText(BASE_URL_EXTRACT);






    }
    public void setupRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AdapterSolarSystem adapterSolarSystem = new AdapterSolarSystem(this, this);
        mRecyclerView.setAdapter(adapterSolarSystem);
        mRecyclerView.setHasFixedSize(true);
    }


    private void wikiApiText(String type){
        for (String aTITLE : PLANETS_API) {
            new HttpAsyncTaskText().execute(type + aTITLE);

        }

    }


    public static String GETText(String url){
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse  httpResponse = httpClient.execute(new HttpGet(url));
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




    @SuppressLint("StaticFieldLeak")
    static class HttpAsyncTaskText extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            return GETText(strings[0]);
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