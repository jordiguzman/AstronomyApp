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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.adapter.AdapterApod;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodEntry;
import appkite.jordiguzman.com.astronomyapp.apod.data.AppDatabase;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.itemPosition;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity.mApodDataMain;


public class ApodDetailFragment extends Fragment implements View.OnClickListener {

    public static ArrayList<String> dates = new ArrayList<>();
    private Context mContext;
    private int caseSnackBar;
    private int mMutedColor;
    private View linearLayout;
    private ApodPageAdapter apodPageAdapter = new ApodPageAdapter();
    private AppDatabase mDb;
    private FloatingActionButton fb_favorites;
    private ApodEntry apodEntry;


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
        mViewPager.setCurrentItem(itemPosition);
        fb_favorites = view.findViewById(R.id.fb_favorites);
        apodPageAdapter.notifyDataSetChanged();
        fb_favorites.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                itemPosition = position;
                changeIconFavorites();
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

    private boolean isFavorited() {
        String idItem = AdapterApod.mApodData.get(itemPosition).getDate();
        for (int i = 0; i < dates.size(); i++) {
            String idItemFavorites = dates.get(i);
            if (idItem.equals(idItemFavorites)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_favorites:
                if (isFavorited()) {
                    saveDataApod();
                } else {
                    caseSnackBar = 1;
                    showSnackBar();
                }
                break;
        }
    }

    public void saveDataApod() {
        mDb = AppDatabase.getInstance(getContext());
        String copyright = mApodDataMain.get(itemPosition).getCopyright();
        String title = mApodDataMain.get(itemPosition).getTitle();
        String date = mApodDataMain.get(itemPosition).getDate();
        String explanation = mApodDataMain.get(itemPosition).getExplanation();
        String url = mApodDataMain.get(itemPosition).getUrl();
        apodEntry = new ApodEntry(copyright, title, date,
                explanation, url);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.apodDao().insertApod(apodEntry);
                dates.add(mApodDataMain.get(itemPosition).getDate());


            }
        });
        fb_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        caseSnackBar = 0;
        showSnackBar();
    }

    public void showSnackBar() {
        Snackbar snackbar = null;
        switch (caseSnackBar) {
            case 0:
                snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.data_saved, Snackbar.LENGTH_SHORT);
                break;
            case 1:
                snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.is_favorited, Snackbar.LENGTH_SHORT);
                break;
        }
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.model_text_size_16));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        snackbar.show();


    }

    @Override
    public void onResume() {
        super.onResume();
        changeIconFavorites();
    }

    private void changeIconFavorites() {
        if (isFavorited()) {
            fb_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        } else {
            fb_favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }
    }

    class ApodPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mApodDataMain.size();
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
            tv_title_pager_item.setText(mApodDataMain.get(position).getTitle());

            TextView tv_date_pager_item = view.findViewById(R.id.tv_date_pager_item);
            tv_date_pager_item.setText(String.format("%s\n", mApodDataMain.get(position).getDate()));

            TextView tv_explanation_pager_item = view.findViewById(R.id.tv_explanation_pager_item);
            tv_explanation_pager_item.setText(mApodDataMain.get(position).getExplanation());

            TextView tv_copyright_pager_item = view.findViewById(R.id.tv_copyright_pager_item);

            if (mApodDataMain.get(position).getCopyright() == null) {
                tv_copyright_pager_item.setText(String.format("%s\n", getString(R.string.no_data)));
            } else {
                tv_copyright_pager_item.setText(String.format("%s\n", mApodDataMain.get(position).getCopyright()));
            }
            linearLayout = view.findViewById(R.id.linearLayout_apod_detail);

            final ImageView iv_photo_apod_detail = view.findViewById(R.id.iv_item_apod);

            String url_base_youtube_video = "http://img.youtube.com/vi/";
            String url_base_embed = "https://www.youtube.com/embed/";
            String url = mApodDataMain.get(position).getUrl();
            int length = url.length();
            String result = url.substring(length - 3, length);
            if (result.equals("jpg") || result.equals("peg")
                    || result.equals("gif") || result.equals("png")) {
                if (!mApodDataMain.get(position).getUrl().isEmpty()){
                    setColorLinearlayout(mApodDataMain.get(position).getUrl());
                }

                Glide.with(mContext)
                        .load(mApodDataMain.get(position).getUrl())
                        .into(iv_photo_apod_detail);


            } else {
                String key = url.substring(url_base_embed.length(), url.length() - 6);
                String urlResult = url_base_youtube_video + key + "/maxresdefault.jpg";
                if (!urlResult.isEmpty()) {
                    setColorLinearlayout(urlResult);
                }
                Glide.with(mContext)
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

        private void setColorLinearlayout(final String url) {

             AppExecutors.getInstance().networkIO().execute(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         Bitmap bitmap = Glide.with(mContext)
                                 .asBitmap()
                                 .load(url)
                                 .submit(500, 500)
                                 .get();

                         if (bitmap != null) {
                             Palette p = Palette.from(bitmap).generate();
                             mMutedColor = p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));


                         }
                     } catch (InterruptedException | ExecutionException e) {
                         e.printStackTrace();
                     }
                 }
             });
            if (mMutedColor==0){
                mMutedColor = ContextCompat.getColor(mContext, R.color.model_random1);
                linearLayout.setBackgroundColor(mMutedColor);
            }else {
                linearLayout.setBackgroundColor(mMutedColor);
            }


        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
