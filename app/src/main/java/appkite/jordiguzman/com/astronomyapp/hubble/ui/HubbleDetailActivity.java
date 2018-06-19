package appkite.jordiguzman.com.astronomyapp.hubble.ui;

import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;

import appkite.jordiguzman.com.astronomyapp.R;

public class HubbleDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_hubble_detail);

        if (savedInstanceState ==null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HubbleDetailFragment fragment = new HubbleDetailFragment();
            transaction.replace(R.id.content_fragment_hubble, fragment);
            transaction.commit();

        }
    }
}
