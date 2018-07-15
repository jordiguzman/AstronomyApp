package appkite.jordiguzman.com.astronomyapp.apod.ui;


import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApodFavorites;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodEntry;
import appkite.jordiguzman.com.astronomyapp.apod.data.AppDatabase;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.ImageLoaderHelper;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.FavoritesApodActivity.itemPositionFavorites;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.FavoritesApodActivity.mApodDataList;

public class FavoritesApodDetailFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View linearLayout;
    private int mMutedColor;


    private AdapterApodFavorites adapterApodFavorites;
    private AppDatabase mDb;

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
        mDb = AppDatabase.getInstance(getContext());
        adapterApodFavorites = new AdapterApodFavorites(mApodDataList, mContext, null);
        mViewPager.setAdapter(new FavoritesApodPageAdapter());
        mViewPager.setCurrentItem(FavoritesApodActivity.itemPositionFavorites);
        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.fb_favorites);
        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp));
        mFloatingActionButton.setOnClickListener(this);

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });

    }

    @Override
    public void onClick(final View v) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ApodEntry> apodEntries = adapterApodFavorites.getApodData();
                mDb.apodDao().deleteApod(apodEntries.get(itemPositionFavorites));
                snackBarDelete();
            }
        });

    }

    private void snackBarDelete() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.data_deleted, Snackbar.LENGTH_SHORT );
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
        snackbar.show();
    }


    class FavoritesApodPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mApodDataList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.pager_item_apod, container, false);
            container.addView(view);
            final boolean isFavorited = true;

            TextView tv_title_pager_item = view.findViewById(R.id.tv_title_pager_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_pager_item.setTypeface(typeface);
            tv_title_pager_item.setText(mApodDataList.get(position).getTitle());

            TextView tv_date_pager_item = view.findViewById(R.id.tv_date_pager_item);
            tv_date_pager_item.setText(String.format("%s\n", mApodDataList.get(position).getDate()));

            TextView tv_explanation_pager_item = view.findViewById(R.id.tv_explanation_pager_item);
            tv_explanation_pager_item.setText(mApodDataList.get(position).getExplanation());

            TextView tv_copyright_pager_item = view.findViewById(R.id.tv_copyright_pager_item);
            if (mApodDataList.get(position).getCopyrigth() == null) {
                tv_copyright_pager_item.setText(String.format("%s\n", getString(R.string.no_data)));
            } else {
                tv_copyright_pager_item.setText(String.format("%s\n", mApodDataList.get(position).getCopyrigth()));
            }
            linearLayout = view.findViewById(R.id.linearLayout_apod_detail);
            final ImageView iv_photo_apod_detail = view.findViewById(R.id.iv_item_apod);
            String url_base_youtube_video = "http://img.youtube.com/vi/";
            String url_base_embed = "https://www.youtube.com/embed/";
            String url = mApodDataList.get(position).getUrl();
            int length = url.length();
            String result = url.substring(length - 3, length);
            if (result.equals("jpg") || result.equals("peg")
                    || result.equals("gif") || result.equals("png")){
                Glide.with(mContext)
                        .load(mApodDataList.get(position).getUrl())
                        .into(iv_photo_apod_detail);
                if (isAdded())setColorLinearlayout(mApodDataList.get(position).getUrl());

            }else {
                String key = url.substring(url_base_embed.length(), url.length() - 6);
                String urlResult = url_base_youtube_video + key + "/maxresdefault.jpg";
                Glide.with(mContext)
                        .load(urlResult)
                        .into(iv_photo_apod_detail);

                if (isAdded())setColorLinearlayout(urlResult);
            }

            iv_photo_apod_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), ImageApodActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("isFavorited", isFavorited);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            iv_photo_apod_detail,
                            "Image");
                    startActivity(intent, optionsCompat.toBundle());
                }
            });
            return view;
        }
        private void setColorLinearlayout(String url){
            ImageLoaderHelper.getInstance(mContext).getImageLoader()
                    .get(url, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                            Bitmap bitmap = imageContainer.getBitmap();
                            if (bitmap !=null){
                                Palette p = Palette.from(bitmap).generate();
                                mMutedColor = p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
                                linearLayout.setBackgroundColor(mMutedColor);
                            }
                        }
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


}
