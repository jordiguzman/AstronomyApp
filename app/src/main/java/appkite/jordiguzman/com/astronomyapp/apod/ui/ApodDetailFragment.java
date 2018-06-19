package appkite.jordiguzman.com.astronomyapp.apod.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract;
import appkite.jordiguzman.com.astronomyapp.apod.ui.utils.DynamicHeightNetworkImageView;
import appkite.jordiguzman.com.astronomyapp.apod.ui.utils.ImageLoaderHelper;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.itemPosition;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;


public class ApodDetailFragment extends Fragment implements View.OnClickListener {

    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        assert container != null;
        mContext = container.getContext();
        return inflater.inflate(R.layout.fragment_apod_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager mViewPager = view.findViewById(R.id.pager_apod);
        mViewPager.setAdapter(new ApodPageAdapter());
        mViewPager.setCurrentItem(ApodActivity.itemPosition);
        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.fb_favorites);
        mFloatingActionButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_favorites:
               saveApodData();
                break;
        }
    }

    class ApodPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mApodData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.pager_item_apod, container, false);
            container.addView(view);
            TextView tv_title_pager_item = view.findViewById(R.id.tv_title_pager_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_pager_item.setTypeface(typeface);
            tv_title_pager_item.setText(mApodData.get(position).getTitle());

            TextView tv_date_pager_item = view.findViewById(R.id.tv_date_pager_item);
            tv_date_pager_item.setText(String.format("%s\n", mApodData.get(position).getDate()));

            TextView tv_explanation_pager_item = view.findViewById(R.id.tv_explanation_pager_item);
            tv_explanation_pager_item.setText(mApodData.get(position).getExplanation());

            TextView tv_copyright_pager_item = view.findViewById(R.id.tv_copyright_pager_item);
            if (mApodData.get(position).getCopyright() == null) {
                tv_copyright_pager_item.setText(String.format("%s\n", getString(R.string.no_data)));
            } else {
                tv_copyright_pager_item.setText(String.format("%s\n", mApodData.get(position).getCopyright()));
            }

            DynamicHeightNetworkImageView iv_photo_apod_detail = view.findViewById(R.id.photo_apod_detail);
            String url_base_youtube_video = "http://img.youtube.com/vi/";
            String url_base_embed = "https://www.youtube.com/embed/";
            String url = mApodData.get(position).getUrl();
            int length = url.length();
            String result = url.substring(length - 3, length);
            if (!result.equals("jpg")) {
                String key = url.substring(url_base_embed.length(), url.length() - 6);
                String urlResult = url_base_youtube_video + key + "/maxresdefault.jpg";
                iv_photo_apod_detail.setImageUrl(urlResult,
                        ImageLoaderHelper.getInstance(mContext).getImageLoader());
            } else {
                iv_photo_apod_detail.setImageUrl(mApodData.get(position).getUrl(),
                        ImageLoaderHelper.getInstance(mContext).getImageLoader());
            }

            iv_photo_apod_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), ImageApodActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });


            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    public void saveApodData() {
        Log.i("Data", String.valueOf(itemPosition));
        ContentValues contentValues = new ContentValues();
        contentValues.put(ApodContract.ApodEntry.COLUMN_TITLE, mApodData.get(itemPosition).getTitle());
        contentValues.put(ApodContract.ApodEntry.COLUMN_DATE, mApodData.get(itemPosition).getDate());
        contentValues.put(ApodContract.ApodEntry.COLUMN_EXPLANATION, mApodData.get(itemPosition).getExplanation());
        if (mApodData.get(itemPosition).getCopyright()==null){
            mApodData.get(itemPosition).setCopyright("No data");
        }
        contentValues.put(ApodContract.ApodEntry.COLUMN_COPYRIGHT,mApodData.get(itemPosition).getCopyright());
        contentValues.put(ApodContract.ApodEntry.COLUMN_URL, mApodData.get(itemPosition).getUrl());
        if (mApodData.get(itemPosition).getHdurl()==null){
            mApodData.get(itemPosition).setHdurl("No data");
        }
        contentValues.put(ApodContract.ApodEntry.COLUMN_HURL, mApodData.get(itemPosition).getHdurl());
        ContentResolver resolver = mContext.getContentResolver();
        resolver.insert(ApodContract.ApodEntry.CONTENT_URI, contentValues);
        //Toast.makeText(mContext, "Data saved", Toast.LENGTH_LONG).show();
        showSnackBar();

    }
    public void showSnackBar(){
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), "Data saved", Snackbar.LENGTH_LONG );
        snackbar.show();
    }



}
