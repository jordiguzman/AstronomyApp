package appkite.jordiguzman.com.astronomyapp.iss.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import appkite.jordiguzman.com.astronomyapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {



    private GoogleMap mMap;

    private Timer mTimer;
    private Double latitude, longitude;
    public static Marker iss;
    private MarkerOptions mMarkerOptions;
    private RequestQueue mRequestQueue;
    @BindView(R.id.tv_latitude)
    TextView tv_latitude;
    @BindView(R.id.tv_longitude)
    TextView tv_longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRequestQueue = Volley.newRequestQueue(this);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(2);

        if (mTimer == null){
            mTimer = new Timer();
        }

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataISS();

            }
        }, 0, 1000);

        mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_iss));
        mMarkerOptions.anchor(0.5f, 0.5f);
        mMarkerOptions.flat(true);
    }


    public void getDataISS(){

        final String URL = "https://api.wheretheiss.at/v1/satellites/25544";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    latitude = Double.parseDouble(response.getString("latitude"));
                    longitude = Double.parseDouble(response.getString("longitude"));
                    final LatLng ISS = new LatLng(latitude, longitude);

                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(ISS));
                            mMap.getUiSettings().setScrollGesturesEnabled(true);
                            if (iss != null){
                                iss.remove();
                            }
                            mMarkerOptions.position(ISS);
                            iss = mMap.addMarker(mMarkerOptions);
                            tv_latitude.setText(String.valueOf(latitude));
                            tv_longitude.setText(String.valueOf(longitude));
                        }
                    });
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



    /*@Override
    protected void onResume() {
        super.onResume();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //getDataISS();

            }
        }, 0, 1000);
    }*/
}
