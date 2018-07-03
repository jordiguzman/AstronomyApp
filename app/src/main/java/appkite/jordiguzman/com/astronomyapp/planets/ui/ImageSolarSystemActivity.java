package appkite.jordiguzman.com.astronomyapp.planets.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.planets.data.Urls;
import appkite.jordiguzman.com.astronomyapp.widget.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageSolarSystemActivity extends AppCompatActivity {

    @BindView(R.id.ib_image_earth)
    ImageButton ib_image_earth;
    @BindView(R.id.iv_image_earth)
    ImageView iv_image_solar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_earth);
        ButterKnife.bind(this);

        ib_image_earth.setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");

        GlideApp.with(this)
                .load(Urls.URL_PLANETS[position])
                .into(iv_image_solar);



    }
}
