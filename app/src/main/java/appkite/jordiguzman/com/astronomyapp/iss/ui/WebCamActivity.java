package appkite.jordiguzman.com.astronomyapp.iss.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import appkite.jordiguzman.com.astronomyapp.R;

public class WebCamActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_cam);

        hideNavigation();
        WebView webView = findViewById(R.id.web_cam);

        if (webView != null){
            webView.loadUrl("http://www.ustream.tv/channel/iss-hdev-payload/pop-out");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setVerticalScrollBarEnabled(false);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        }else {
            showSnackBar();
        }
    }
    public void showSnackBar(){
        Snackbar snackbar = Snackbar.make(this.findViewById(R.id.linearLayout_webcam), R.string.problems_with_webcam, Snackbar.LENGTH_LONG );
        snackbar.show();
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
}
