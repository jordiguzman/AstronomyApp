package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.adapter.AdapterHubble;
import appkite.jordiguzman.com.astronomyapp.hubble.model.Images;
import appkite.jordiguzman.com.astronomyapp.hubble.model.ImagesDetail;
import appkite.jordiguzman.com.astronomyapp.hubble.service.ApiClientHubbleImages;
import appkite.jordiguzman.com.astronomyapp.hubble.service.ApiInterfaceHubbleImages;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HubbleActivity extends AppCompatActivity implements AdapterHubble.ItemClickListenerHubble{

    public ArrayList<Images> dataImages = new ArrayList<>();
    public static ArrayList<ImagesDetail> dataImagesDetail = new ArrayList<>();
    private String url = "http://hubblesite.org/api/v3/image/";


    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;
    public static int itemPositionHubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);

        getDataHubbleImages();

    }

    public void getDataHubbleImages(){
        final ApiInterfaceHubbleImages apiInterfaceHubbleImages = ApiClientHubbleImages.getClient()
                .create(ApiInterfaceHubbleImages.class);
        Call<List<Images>> call = apiInterfaceHubbleImages.getDataImages();
        call.enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(@NonNull Call<List<Images>> call, @NonNull Response<List<Images>> response) {
                switch (response.code()){
                    case 200:
                        dataImages = (ArrayList<Images>) response.body();
                        for (int i=0; i< dataImages.size();i++){
                            new HttpAsyctaskDataHubbleImagesDetail().execute(url + dataImages.get(i).getId());
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Error api", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Images>> call, @NonNull Throwable t) {
                Log.e("OnFailure", t.getMessage());
            }

        });

    }

    public String getDataHubbleImagesDetail(String url){
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

    @SuppressLint("StaticFieldLeak")
    class HttpAsyctaskDataHubbleImagesDetail extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return getDataHubbleImagesDetail(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            ImagesDetail imagesDetail;
            try {
                JSONObject jsonObject = new JSONObject(result);
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String credits = jsonObject.getString("credits");
                JSONArray imageFiles = jsonObject.getJSONArray("image_files");

                JSONObject oneImage = imageFiles.getJSONObject(0);
                String image = oneImage.getString("file_url");
                String imageTemp = image.substring(image.length()-3);
                if (imageTemp.equals("pdf") || imageTemp.equals("tif")){
                    oneImage = imageFiles.getJSONObject(2);
                    image = oneImage.getString("file_url");
                }
                imagesDetail = new ImagesDetail(name, description, credits, image);

                dataImagesDetail.add(imagesDetail);
                for (int i=0; i<dataImagesDetail.size(); i++){
                    Glide.with(getApplicationContext())
                    .load(dataImagesDetail.get(i).getImage())
                            .preload();
                }

                setupRecyclerView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public  void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_hubble);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AdapterHubble adapterHubble = new AdapterHubble(this, dataImagesDetail, this);
        mRecyclerView.setAdapter(adapterHubble);
        mRecyclerView.setHasFixedSize(true);

    }


    @Override
    public void onClickItem(int position) {
        itemPositionHubble = position;
        Intent intent = new Intent(this, HubbleDetailActivity.class);
        startActivity(intent);

    }
}
