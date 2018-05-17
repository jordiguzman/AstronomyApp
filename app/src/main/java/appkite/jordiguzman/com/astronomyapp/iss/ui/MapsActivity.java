package appkite.jordiguzman.com.astronomyapp.iss.ui;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.iss.util.ISSActual;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationManager locationManager;

    private GoogleMap mMap;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ISSActual();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    public void ISSActual() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org")
                .build();
        ISSService service = retrofit.create(ISSService.class);
        service.listObjects("iss-now.json", new Callback<ISSActual>() {
            @Override
            public void onResponse(Call<ISSActual> call, Response<ISSActual> response) {
                setPositionOnMap(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
            }

            @Override
            public void onFailure(Call<ISSActual> call, Throwable t) {

            }
        });

    }

    public void setPositionOnMap(double lat, double longi) {
        LatLng latLng = new LatLng(lat, longi);
        if (mCurrentLocation != null) {
            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            Marker user = mMap.addMarker(
                    new MarkerOptions().position(currentLatLng)
                            .title("Me"));
        }
        Marker iss = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("ISS")
                .snippet("International Space Station")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_iss)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
    }

    public interface ISSService {
        @GET("/{directory}")
        void listObjects(@Path("directory") String directory, Callback<ISSActual> issCallback);
    }
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            mCurrentLocation = location;
            ISSActual();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

}
