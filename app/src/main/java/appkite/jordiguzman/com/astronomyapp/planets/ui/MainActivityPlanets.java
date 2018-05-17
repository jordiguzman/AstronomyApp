package appkite.jordiguzman.com.astronomyapp.planets.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.planets.model.WikiPlanets;

public class MainActivityPlanets extends AppCompatActivity {



    private static final String TITLE = "Mars";
    private WikiPlanets wikiPlanets = new WikiPlanets();
    private static final String BASE_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query" +
            "&prop=extracts&explaintext=&titles=";
    private static final String BASE_URL_EXTRACT = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exintro=&titles=";
    private ArrayList<JSONArray> arrayList = new ArrayList<>();
    private TextView textView;
    private StringBuilder title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_planets);

        /*FragmentPlanets fragmentPlanets = new FragmentPlanets();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_planets, fragmentPlanets)
                .commit();*/
        textView = findViewById(R.id.tv_planet_description);

        wikiApi();
        setTitle("");

    }

    private void wikiApi(){
        new HttpAsyncTask().execute(BASE_URL + TITLE);

    }
    public static String GET(String url){
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




    @SuppressLint("StaticFieldLeak")
    private class HttpAsyncTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            return GET(strings[0]);
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
                    textView.setText(Html.fromHtml(textWiki.toString()));
                }else {
                    textView.setText(R.string.no_results);
                }

            }catch (JSONException e){
                e.getMessage();
            }
        }
    }
}