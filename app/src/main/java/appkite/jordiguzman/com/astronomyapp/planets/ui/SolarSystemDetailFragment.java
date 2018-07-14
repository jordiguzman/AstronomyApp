package appkite.jordiguzman.com.astronomyapp.planets.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.PLANETS;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.URL_PLANETS;
import static appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity.itemPositionSolar;
import static appkite.jordiguzman.com.astronomyapp.planets.ui.SolarSystemActivity.wikiPlanetsText;

public class SolarSystemDetailFragment extends Fragment{

    private Context mContext;
    private View linearLayout;
    private int mMutedColor;
    private SolarSystemAdapter solarSystemAdapter = new SolarSystemAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        mContext = container.getContext();
        return inflater.inflate(R.layout.fragment_solar_system_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager mViewPager = view.findViewById(R.id.pager_solar_system);
        mViewPager.setAdapter(new SolarSystemAdapter());
        mViewPager.setCurrentItem(itemPositionSolar);
        solarSystemAdapter.notifyDataSetChanged();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                itemPositionSolar = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }



    class SolarSystemAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return PLANETS.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            final LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.pager_item_solar_system, container, false);
            container.addView(view);
            solarSystemAdapter.notifyDataSetChanged();
            TextView tv_title_solar_system_item = view.findViewById(R.id.tv_title_pager_solar_system_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_solar_system_item.setTypeface(typeface);
            tv_title_solar_system_item.setText(PLANETS[position]);

            TextView tv_explanation_solar_system_item = view.findViewById(R.id.tv_explanation_pager_solar_system_item);
            try {
                tv_explanation_solar_system_item.setText(wikiPlanetsText.get(position));
            }catch (Exception e){
                e.getMessage();
               SolarSystemActivity.wikiApiText();
            }





            Resources resources = mContext.getResources();
            String[] subTitle = resources.getStringArray(R.array.subtitle_array);
            TextView tv_subtitles = view.findViewById(R.id.tv_subtitle_pager_solar_system);
            tv_subtitles.setText(subTitle[position]);

            final ImageView photo_solar_system_detail = view.findViewById(R.id.photo_solar_system_detail);
            Glide.with(mContext)
                    .load(URL_PLANETS[position])
                    .into(photo_solar_system_detail);

            photo_solar_system_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageSolarSystemActivity.class);
                    intent.putExtra("position", itemPositionSolar);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            photo_solar_system_detail,
                            "image");
                    startActivity(intent, activityOptionsCompat.toBundle());
                }
            });

            linearLayout = view.findViewById(R.id.linearLayout_solar_system_detail);
            setColorLinearlayout(URL_PLANETS[position]);
            return view;
        }

        private void setColorLinearlayout(final String url){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap= Glide.with(mContext)
                                .asBitmap()
                                .load(url)
                                .submit(500,500)
                                .get();
                        if (bitmap !=null){
                            Palette p = Palette.from(bitmap).generate();
                            mMutedColor = p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
                            linearLayout.setBackgroundColor(mMutedColor);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }



}
