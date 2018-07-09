package appkite.jordiguzman.com.astronomyapp.apod.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;
import appkite.jordiguzman.com.astronomyapp.widget.GlideApp;

import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_COPYRIGHT;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_DATE;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_EXPLANATION;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_HURL;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_ID;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_TITLE;
import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.COLUMN_URL;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.itemPosition;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodData;


public class ApodDetailFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private int caseSnackBar;
    private int mMutedColor;
    private View linearLayout;


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
        FloatingActionButton fb_favorites = view.findViewById(R.id.fb_favorites);

        fb_favorites.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                itemPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private boolean isFavorited(){
        Cursor cursor = mContext.getContentResolver().query(ApodContract.ApodEntry.CONTENT_URI,
                null,
                null,
                null,
                COLUMN_ID);

        if (cursor != null){
            while (cursor.moveToNext()){
                String idApod = cursor.getString(1);
                String idApodActual = mApodData.get(itemPosition).getDate();

                if (idApod.equals(idApodActual)){
                    return false;
                }
            }
        }
        cursor.close();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.fb_favorites:
                if (isFavorited()){
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveApodData();
                        }
                    });
                }else {
                    caseSnackBar=1;
                    showSnackBar();
                }
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
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

            final LayoutInflater inflater = LayoutInflater.from(getContext());
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
            linearLayout = view.findViewById(R.id.linearLayout_apod_detail);

            final ImageView iv_photo_apod_detail = view.findViewById(R.id.iv_item_apod);

            String url_base_youtube_video = "http://img.youtube.com/vi/";
            String url_base_embed = "https://www.youtube.com/embed/";
            String url = mApodData.get(position).getUrl();
            int length = url.length();
            String result = url.substring(length - 3, length);
            if (result.equals("jpg") || result.equals("peg")
                    || result.equals("gif") || result.equals("png")){
                if (!mApodData.get(position).getUrl().isEmpty())setColorLinearlayout(mApodData.get(position).getUrl());
                GlideApp.with(mContext)
                        .load(mApodData.get(position).getUrl())
                        .into(iv_photo_apod_detail);


            }else {
                String key = url.substring(url_base_embed.length(), url.length() - 6);
                String urlResult = url_base_youtube_video + key + "/maxresdefault.jpg";
                if (!urlResult.isEmpty())setColorLinearlayout(urlResult);
                GlideApp.with(mContext)
                        .load(urlResult)
                        .into(iv_photo_apod_detail);

            }


            iv_photo_apod_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageApodActivity.class);
                    intent.putExtra("position", itemPosition);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            iv_photo_apod_detail,
                            "Image");
                    startActivity(intent, optionsCompat.toBundle());
                }
            });
            return view;
        }
        private void setColorLinearlayout(final String url){
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap= GlideApp.with(mContext)
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


        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    public void saveApodData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, mApodData.get(itemPosition).getDate());
        contentValues.put(COLUMN_TITLE, mApodData.get(itemPosition).getTitle());
        contentValues.put(COLUMN_DATE, mApodData.get(itemPosition).getDate());
        contentValues.put(COLUMN_EXPLANATION, mApodData.get(itemPosition).getExplanation());
        if (mApodData.get(itemPosition).getCopyright()==null){
            mApodData.get(itemPosition).setCopyright("No data");
        }
        contentValues.put(COLUMN_COPYRIGHT,mApodData.get(itemPosition).getCopyright());
        contentValues.put(COLUMN_URL, mApodData.get(itemPosition).getUrl());
        if (mApodData.get(itemPosition).getHdurl()==null){
            mApodData.get(itemPosition).setHdurl("No data");
        }
        contentValues.put(COLUMN_HURL, mApodData.get(itemPosition).getHdurl());
        ContentResolver resolver = mContext.getContentResolver();
        resolver.insert(ApodContract.ApodEntry.CONTENT_URI, contentValues);
        caseSnackBar=0;
        showSnackBar();

    }
    public void showSnackBar(){
        Snackbar snackbar;
        switch (caseSnackBar){
            case 0:
                 snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.data_saved, Snackbar.LENGTH_LONG );
                 View snackbarView = snackbar.getView();
                 int snackbarTextId = android.support.design.R.id.snackbar_text;
                 TextView textView = snackbarView.findViewById(snackbarTextId);
                 textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.model_text_size_16));
                 textView.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                 snackbar.show();
                 break;
            case 1:
                snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.is_favorited, Snackbar.LENGTH_LONG );
                View snackbarView1 = snackbar.getView();
                int snackbarTextId1 = android.support.design.R.id.snackbar_text;
                TextView textView1 = snackbarView1.findViewById(snackbarTextId1);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.model_text_size_16));
                textView1.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                snackbar.show();
                break;
        }


    }



}
