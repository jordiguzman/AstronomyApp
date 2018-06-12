package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HubbleActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://hubblesite.org/api/v3/news_release/2018-";
    public static ArrayList<String> dataHubble = new ArrayList<>();
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> urlArray= new ArrayList<>();
    private static int index =0;
    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);


        try {
            populateArray();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setupRecyclerView();


    }

    public  void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_hubble);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AdapterHubble adapterHubble = new AdapterHubble(this, nameArray);
        mRecyclerView.setAdapter(adapterHubble);
        mRecyclerView.setHasFixedSize(true);

    }


    public void populateArray() throws ExecutionException, InterruptedException {
        for (int i= 1; i< 27; i++){
            new HttpAsyncTaskDataHubble().execute(BASE_URL + i).get();
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
                    String name = jsonObject.getString("name");
                    String urlThumb = jsonObject.getString("thumbnail");
                    nameArray.add(name);
                    urlArray.add(urlThumb);
                }








            }catch (JSONException e){
                e.getMessage();
            }

        }
    }

    public class AdapterHubble extends RecyclerView.Adapter<AdapterHubble.AdapterHubbleViewHolder>{

        private Context mContext;
        private ArrayList<String> nameAdapter;

        AdapterHubble(Context context, ArrayList<String> name){
            mContext = context;
            nameAdapter = name;
        }

        @NonNull
        @Override
        public AdapterHubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View rootView = inflater.inflate(R.layout.item_hubble, parent, false);

            return new AdapterHubbleViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterHubbleViewHolder holder, int position) {

            Glide.with(mContext)
                .load(urlArray.get(position))
                .into(holder.iv_hubble);
        holder.tv_hubble.setText(nameAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return nameAdapter.size();
        }

        class AdapterHubbleViewHolder extends RecyclerView.ViewHolder{

            ImageView iv_hubble;
            TextView tv_hubble;

            AdapterHubbleViewHolder(View itemView) {
                super(itemView);
                iv_hubble = itemView.findViewById(R.id.iv_hubble);
                tv_hubble = itemView.findViewById(R.id.tv_title_huble);
            }
        }
    }



}
