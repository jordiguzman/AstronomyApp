package appkite.jordiguzman.com.astronomyapp.iss.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.iss.model.Astronaut;
import butterknife.BindView;
import butterknife.ButterKnife;

import static appkite.jordiguzman.com.astronomyapp.iss.ui.AstronautsActivity.spaceAstronauts;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback  {



    private GoogleMap mMap;

    private Timer mTimer;
    private Timer mPolyTimer;
    private Double latitude, longitude, altitude, velocity;
    private String visibility, latitudeInf, longitudeInf;
    public static Marker iss;
    private MarkerOptions mMarkerOptions;
    private RequestQueue mRequestQueue;
    @BindView(R.id.tv_data_iss_position)
    TextView tv_data_iss_position;
    @BindView(R.id.tv_data_iss)
    TextView tv_data_iss;
    @BindView(R.id.ib_iss)
    ImageButton ib_iss;
    private DecimalFormat decimalFormat;
    private  LatLng ISS;
    private int mPolyCounter;
    private int mPoly;
    private boolean fixISS;
    private int mProgress;
    private Polyline mPolyLine;
    private int mCurrentColor;
    private int mCurrentWidth;
    private Polyline[] mPolyArray;
    private LatLng mLast;
    private Date currentDate;
    String url= "http://www.howmanypeopleareinspacerightnow.com/peopleinspace.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRequestQueue = Volley.newRequestQueue(this);
        mPolyArray = new Polyline[200];
        mCurrentColor = Color.WHITE;
        mCurrentWidth = 5;
        new HttpAsyntaskDataAstronauts().execute(url);

    }

    public void clickMenuIss(final View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        final PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_iss, popupMenu.getMenu());

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fix_iss:
                        fixISS = !fixISS;
                        break;
                    case R.id.menu_astronauts:
                        Intent intent = new Intent(getApplicationContext(), AstronautsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_cam:
                        Intent intent1 = new Intent(getApplicationContext(), WebCamActivity.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(2);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (mTimer == null){
            mTimer = new Timer();
        }
        if (mPolyTimer == null) {
            mPolyTimer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {
                    asyncTaskPolyline();
                }
            };
            mPolyTimer.schedule(hourlyTask, 0L, 5400000);
        }


        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataISS();

            }
        }, 0, 3000);

        mMarkerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_iss));
        mMarkerOptions.anchor(0.5f, 0.5f);

    }

    private void asyncTaskPolyline() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();
                getDataISS();
                mPoly = 0;
                mPolyCounter = 0;
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                     currentDate = new Date();
                    mProgress = 0;
                    for (int i = 0; i < 20; ++i) {
                        int mLastPatience = 0;
                        while (i > 0 && mLastPatience < 10 && mProgress < i) {
                            Thread.sleep(300);
                            ++mLastPatience;
                        }

                        updatePolyline(currentDate);
                        Thread.sleep(1000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void updatePolyline(Date currentDate) {
        long currentLong = currentDate.getTime() / 1000;
        final long[] futureTen = new long[10];

        for (int i = 0; i < futureTen.length; ++i) {
            futureTen[i] = currentLong + (30 * mPoly++);
        }

        final StringBuilder urlBuilder = new StringBuilder();
        for (long aFutureTen : futureTen) {
            urlBuilder.append(aFutureTen).append(",");
        }
        urlBuilder.setLength(urlBuilder.length() - 1);
        final String units = "miles";
        final String url = "https://api.wheretheiss.at/v1/satellites/25544/positions?timestamps=" +
                urlBuilder.toString() +
                "&units=" +
                units;

        final int finalStart = mPoly;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    final LatLng[] latLngs = new LatLng[10];

                    for (int i = 0; i < response.length(); ++i) {
                        latLngs[i] = new LatLng(response.getJSONObject(i).getDouble("latitude"),
                                response.getJSONObject(i).getDouble("longitude"));
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (finalStart == 10) {
                                for (int i = 0; i < futureTen.length - 1; ++i) {
                                    mPolyLine = mMap.addPolyline(new PolylineOptions()
                                            .add(latLngs[i], latLngs[i + 1])
                                            .width(mCurrentWidth)
                                            .color(mCurrentColor));
                                    mPolyArray[mPolyCounter++] = mPolyLine;
                                }
                                mLast = latLngs[latLngs.length - 1];
                                ++mProgress;
                            } else {
                                mPolyArray[mPolyCounter++] = mMap.addPolyline(new PolylineOptions()
                                        .add(mLast, latLngs[0])
                                        .width(mCurrentWidth)
                                        .color(mCurrentColor));
                                for (int i = 0; i < futureTen.length - 1; ++i) {
                                    mPolyArray[mPolyCounter++] = mMap.addPolyline(new PolylineOptions()
                                            .add(latLngs[i], latLngs[i + 1])
                                            .width(mCurrentWidth)
                                            .color(mCurrentColor));
                                }
                                mLast = latLngs[latLngs.length - 1];
                                ++mProgress;
                            }
                        }
                    });
                } catch (Exception e) {
                    if (mLast == null) {
                        e.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

            }
        });
        mRequestQueue.add(jsonArrayRequest);
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


                    Date date = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
                    final String dateFormated= new SimpleDateFormat("hh:mm:ss").format(date);

                    ISS = new LatLng(latitude, longitude);
                    decimalFormat = new DecimalFormat("0.00");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(ISS));

                            if (fixISS){
                                mMap.getUiSettings().setScrollGesturesEnabled(true);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(ISS));


                            }else {
                                mMap.getUiSettings().setScrollGesturesEnabled(false);
                            }
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
                Log.e("ERROR VOLLEY: ", "is" + volleyError.getMessage());
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    private static String converInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)result.append(line);

        inputStream.close();
        return result.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null){
            mTimer.cancel();
        }
    }
    @SuppressLint("StaticFieldLeak")
    class HttpAsyntaskDataAstronauts extends AsyncTask<String, Void, String>{

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

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray people = jsonObject.getJSONArray("people");

                for (int i=0; i< people.length(); i++){
                    JSONObject oneAstronaut = people.getJSONObject(i);
                    String name= oneAstronaut.getString("name");
                    String bioPhoto =oneAstronaut.getString("biophoto");
                    String flag = oneAstronaut.getString("countryflag");
                    String launchDate = oneAstronaut.getString("launchdate");
                    String role = oneAstronaut.getString("title");
                    String location = oneAstronaut.getString("location");
                    String bio = oneAstronaut.getString("bio");
                    String bioWiki = oneAstronaut.getString("biolink");
                    String twitter = oneAstronaut.getString("twitter");
                    Astronaut astronaut = new Astronaut(name, bioPhoto, flag, launchDate, role, location, bio, bioWiki, twitter);
                    spaceAstronauts.add(astronaut);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
