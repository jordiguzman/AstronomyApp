package appkite.jordiguzman.com.astronomyapp.planets;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.planets.renderer.GlRenderer;

public class MainActivityPlanets extends AppCompatActivity {


    private GLSurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView = new GLSurfaceView(this);
        mSurfaceView.setRenderer(new GlRenderer(this));
        setContentView(mSurfaceView);


    }
    @Override
    protected void onResume() {
        super.onResume();
        this.mSurfaceView.onResume();
    }

    /**
     * Also pause the glSurface.
     */
    @Override
    protected void onPause() {
        this.mSurfaceView.onPause();
        super.onPause();
    }
}