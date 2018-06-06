package appkite.jordiguzman.com.astronomyapp.planets.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivitySolarSystem extends AppCompatActivity {



    public static final String[] PLANETS = {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};
    public static ArrayList<String> wikiPlanets = new ArrayList<>();
    private static final String BASE_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query" +
            "&prop=extracts&explaintext=&titles=";
    private static final String BASE_URL_EXTRACT = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=";
    private static final String BASE_URL_IMAGES = "https://en.wikipedia.org/w/api.php?action=query&titles=Earth&prop=pageimages&format=json&pithumbsize=400";
    @BindView(R.id.iv_system)
    ImageView iv_system;
    @BindView(R.id.rv_system)
    RecyclerView mRecyclerView;
    private StringBuilder title;
    private AdapterSolarSystem adapterSolarSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_planets);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[2])
                .into(iv_system);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterSolarSystem = new AdapterSolarSystem(wikiPlanets, this);
        mRecyclerView.setAdapter(adapterSolarSystem);
        mRecyclerView.setHasFixedSize(true);


        wikiApiText();
        wikiApiImages();
        setTitle("");

    }

    private void wikiApiText(){
        for (String aTITLE : PLANETS) {
            new HttpAsyncTaskText().execute(BASE_URL + aTITLE);
        }
    }
    private void wikiApiImages(){
            new HttpAsyntaskImages().execute(BASE_URL_IMAGES);
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

    public static String GETImages(String url){
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
            Log.i("ERROR GET", e.getMessage());
        }
        return result;
    }




    @SuppressLint("StaticFieldLeak")
    private class HttpAsyntaskImages extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return GETImages(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //String source, title;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject query = jsonObject.getJSONObject("query");
                JSONObject title = query.getJSONObject("title");
                String [] strings = title.toString().substring(0,20).split(":");
                String string = strings[0];
                Log.i("Data", string);

            }catch (JSONException e){
                Log.i("ERROR", e.getMessage());

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class HttpAsyncTaskText extends AsyncTask<String, Void, String>{


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
                title = new StringBuilder((String)page.get("title"));
                setTitle(title);
                String[] strings1 = textWiki.toString().split("=");

                textWiki = new StringBuilder();

                for (String temp:strings1){

                    textWiki.append("\n").append(temp.trim());
                }

                if (textWiki.length() > 0){
                    //textView.setText(Html.fromHtml(textWiki.toString()));
                    wikiPlanets.add(textWiki.toString());
                }else {
                    Log.e("Error", "");
                }

            }catch (JSONException e){
                e.getMessage();
            }
        }
    }

}