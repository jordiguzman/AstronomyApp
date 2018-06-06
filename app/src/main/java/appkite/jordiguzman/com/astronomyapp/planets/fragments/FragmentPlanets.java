package appkite.jordiguzman.com.astronomyapp.planets.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.planets.model.WikiPlanets;


public class FragmentPlanets extends Fragment {

    public static ArrayList<WikiPlanets> mWikiData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_planets, container, false);
        //TextView textView = rootView.findViewById(R.id.tv_planet_description);

        //textView.setText(Html.fromHtml(""));
        return rootView;
    }

}
