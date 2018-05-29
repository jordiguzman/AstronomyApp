package appkite.jordiguzman.com.astronomyapp.earth.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthActivity extends AppCompatActivity {


    private final String URL = "https://epic.gsfc.nasa.gov/epic-archive/jpg/";

    private String caption, image, date;
    public static ArrayList<Earth> earthArrayList = new ArrayList<>();
    @BindView(R.id.tv_data_earth)
    TextView tv_data_earth;
    @BindView(R.id.iv_data_earth)
    ImageView iv_data_earth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth);
        ButterKnife.bind(this);

        image = earthArrayList.get(0).getImage();
        tv_data_earth.setText(image);

        String url= URL + image + ".jpg";
        Log.i("Url: ", url);
        Glide.with(this)
                .load(url)
                .into(iv_data_earth);





    }





}
