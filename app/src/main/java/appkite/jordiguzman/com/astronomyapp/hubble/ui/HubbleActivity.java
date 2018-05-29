package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.model.Hubble;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HubbleActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://hubblesite.org/api/v3/news_release/";
    private String newsId = "2018-12";
    public static ArrayList<Hubble> hubbleArrayList = new ArrayList<>();
    private String name;
    private String url;
    private String abstractString;
    private String credits;
    private String urlThubNail;
    private String urlImage;
    private RequestQueue mRequestQueue;
    @BindView(R.id.iv_hubble)
    ImageView iv_hubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubble);
        ButterKnife.bind(this);

        mRequestQueue = Volley.newRequestQueue(this);
        getDataHubble();

    }



    public void getDataHubble(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + newsId,
                null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name = response.getString("name");
                   urlThubNail = response.getString("thumbnail_retina");
                    Glide.with(getApplicationContext())
                            .load(urlThubNail)
                            .into(iv_hubble);



                } catch (Exception e) {
                    Log.e("ERROR: ", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERROR VOLLEY: ", volleyError.getMessage());
            }
        });
        mRequestQueue.add(jsonObjectRequest);

    }

}
