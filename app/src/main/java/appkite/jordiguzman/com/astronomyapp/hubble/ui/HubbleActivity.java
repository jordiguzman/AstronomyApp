package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
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

import static appkite.jordiguzman.com.astronomyapp.mainUi.MainActivityApp.isTablet;


public class HubbleActivity extends AppCompatActivity implements AdapterHubble.ItemClickListenerHubble{

    public static ArrayList<Images> dataImages = new ArrayList<>();
    public static ArrayList<ImagesDetail> dataImagesDetail = new ArrayList<>();
    private String url = "http://hubblesite.org/api/v3/image/";
    public static int itemPositionHubble;
    @BindView(R.id.ib_menu_activity_hubble)
    ImageButton ib_menu_hubble;
    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;
    @BindView(R.id.pb_hubble_activity)
    ProgressBar progressBar;
    @BindView(R.id.rv_hubble)
    RecyclerView recyclerView;
    @BindView(R.id.collapsing_hubble)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        imageCollapsingToolBar();
        Glide.with(this)
                .load(AdapterMain.URL_MAIN[4])
                .into(iv_hubble);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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
                            new HttpAsyctaskDataHubbleImagesDetail().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url + dataImages.get(i).getId());
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


    private static String converInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)result.append(line);

        inputStream.close();
        return result.toString();
    }

    @SuppressLint("ResourceAsColor")
    public void imageCollapsingToolBar(){

        mCollapsingToolbarLayout.setContentScrimColor(R.color.primary_text);
        mCollapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryLight);
    }
    @SuppressLint("StaticFieldLeak")
    class HttpAsyctaskDataHubbleImagesDetail extends AsyncTask<String, Void, String>{

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
                    oneImage = imageFiles.getJSONObject(3);
                    image = oneImage.getString("file_url");
                }
                imagesDetail = new ImagesDetail(name, description, credits, image);

                dataImagesDetail.add(imagesDetail);
                /*for (int i=0; i<dataImagesDetail.size(); i++){
                    Glide.with(getApplicationContext())
                    .load(dataImagesDetail.get(i).getImage())
                            .preload();
                }*/

                CountDownTimer countDownTimer = new CountDownTimer(100,1500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        setupRecyclerView();
                    }
                };
                countDownTimer.start();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void clickMenuHubble(View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_hubble, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_favorites:
                        goToFavoritesHubble();
                        break;
                }
                return true;
            }

        });
    }

    private void goToFavoritesHubble() {
        Intent intent = new Intent(this, FavoritesHubbleActivity.class);
        startActivity(intent);
    }


    public  void setupRecyclerView() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = findViewById(R.id.rv_hubble);
        if (isTablet(this)){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        AdapterHubble adapterHubble = new AdapterHubble(this, dataImagesDetail, this);
        mRecyclerView.setAdapter(adapterHubble);
        mRecyclerView.setHasFixedSize(true);

    }


    @Override
    public void onClickItem(int position) {
        itemPositionHubble = position;
        Intent intent = new Intent(this, HubbleDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }
}
