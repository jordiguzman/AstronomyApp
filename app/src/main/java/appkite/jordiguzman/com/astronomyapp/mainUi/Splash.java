package appkite.jordiguzman.com.astronomyapp.mainUi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.adapter.AdapterMain;

public class Splash extends AppCompatActivity {

    public static final String URL = "https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
            "o/images%2Fcreation.jpg?alt=media&token=155e09d8-32d5-4446-986b-6c032d40d9fd";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideNavigation();
        preDownloadMainImages();


        CountDownTimer countDownTimer = new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                toMain();
                finish();
            }
        };
        countDownTimer.start();    }


        public void preDownloadMainImages(){
            for (int i = 0; i< AdapterMain.URL_MAIN.length; i++){
                Glide.with(this)
                        .load(AdapterMain.URL_MAIN[i])
                        .preload(1024, 415);
            }
            Glide.with(this).
                    load(URL)
                    .preload(768, 512);
        }

        public void toMain(){
            Intent intent = new Intent(getApplicationContext(), MainActivityApp.class);
            startActivity(intent);
        }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideNavigation() {
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    @Override
    public void onBackPressed() {

    }
}
