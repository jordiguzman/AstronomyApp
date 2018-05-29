package appkite.jordiguzman.com.astronomyapp.iss.ui;

import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import appkite.jordiguzman.com.astronomyapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback  {



    private GoogleMap mMap;

    private Timer mTimer;
    private Double latitude, longitude, altitude, velocity;
    private String visibility, latitudeInf, longitudeInf;
    public static Marker iss;
    private MarkerOptions mMarkerOptions;
    private RequestQueue mRequestQueue;
    @BindView(R.id.tv_data_iss_position)
    TextView tv_data_iss_position;
    @BindView(R.id.tv_data_iss)
    TextView tv_data_iss;
    private DecimalFormat decimalFormat;
    private  LatLng ISS;


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

    }


    public void getDataISS(){

        final String URL = "https://api.wheretheiss.at/v1/satellites/25544";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    latitude = Double.parseDouble(response.getString("latitude"));
                    longitude = Double.parseDouble(response.getString("longitude"));
                    altitude = Double.parseDouble(response.getString("altitude"));
                    velocity = Double.parseDouble(response.getString("velocity"));
                    visibility = response.getString("visibility");
                    if (latitude<0){
                        latitudeInf = "° S";
                    }else {
                        latitudeInf = "° N";
                    }
                    if (longitude<0){
                        longitudeInf = "° E";
                    }else {
                        longitudeInf = "° W";
                    }


                    Date date = Calendar.getInstance().getTime();
                    final String dateFormated= new SimpleDateFormat("hh:mm:ss").format(date);

                    ISS = new LatLng(latitude, longitude);
                    decimalFormat = new DecimalFormat("0.00");

                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(ISS));
                            mMap.getUiSettings().setScrollGesturesEnabled(true);
                            if (iss != null){
                                iss.remove();
                            }
                            if (latitude != null){
                                tv_data_iss_position.setText("");
                            }
                            mMarkerOptions.position(ISS);
                            iss = mMap.addMarker(mMarkerOptions);
                            tv_data_iss_position.append(getString(R.string.latitude).concat(String.valueOf(decimalFormat.format(latitude)).concat(latitudeInf))
                            .concat("\n").concat(getString(R.string.longitude).concat(String.valueOf(decimalFormat.format(longitude)).concat(longitudeInf))));
                            final StringBuilder dataISS = new StringBuilder();
                            dataISS.append(getString(R.string.altitude)).append(String.valueOf(decimalFormat.format(altitude))).append(" Km").append("\n")
                                    .append(getString(R.string.velocity)).append(String.valueOf(decimalFormat.format(velocity))).append(" Kph").append("\n")
                                    .append(getString(R.string.time)).append(dateFormated).append("\n")
                                    .append(getString(R.string.visibility)).append(visibility).append("\n");

                            tv_data_iss.setText(dataISS);

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
