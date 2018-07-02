package appkite.jordiguzman.com.astronomyapp.apod.ui;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.DynamicHeightNetworkImageView;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.ImageLoaderHelper;

import static appkite.jordiguzman.com.astronomyapp.apod.ui.FavoritesApodActivity.dataLoadedApod;
import static appkite.jordiguzman.com.astronomyapp.apod.ui.FavoritesApodActivity.itemPositionFavorites;

public class FavoritesApodDetailFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View linearLayout;
    private int mMutedColor;


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
        mViewPager.setAdapter(new FavoritesApodPageAdapter());
        mViewPager.setCurrentItem(FavoritesApodActivity.itemPositionFavorites);
        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.fb_favorites);
        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp));
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = ApodContract.ApodEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(FavoritesApodActivity.dataLoadedApod[itemPositionFavorites][6]).build();
        contentResolver.delete(uri, null, null);
        snackBarDelete();

    }

    private void snackBarDelete() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.card_fragment_apod), R.string.data_deleted, Snackbar.LENGTH_LONG );
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
        snackbar.show();
    }


    class FavoritesApodPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return FavoritesApodActivity.apodArrayList.size();
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
            tv_title_pager_item.setText(dataLoadedApod[position][0]);

            TextView tv_date_pager_item = view.findViewById(R.id.tv_date_pager_item);
            tv_date_pager_item.setText(String.format("%s\n", dataLoadedApod[position][1]));

            TextView tv_explanation_pager_item = view.findViewById(R.id.tv_explanation_pager_item);
            tv_explanation_pager_item.setText(dataLoadedApod[position][2]);

            TextView tv_copyright_pager_item = view.findViewById(R.id.tv_copyright_pager_item);
            if (dataLoadedApod[position][3] == null) {
                tv_copyright_pager_item.setText(String.format("%s\n", getString(R.string.no_data)));
            } else {
                tv_copyright_pager_item.setText(String.format("%s\n", dataLoadedApod[position][3]));
            }
            linearLayout = view.findViewById(R.id.linearLayout_apod_detail);
            DynamicHeightNetworkImageView iv_photo_apod_detail = view.findViewById(R.id.photo_apod_detail);
            String url_base_youtube_video = "http://img.youtube.com/vi/";
            String url_base_embed = "https://www.youtube.com/embed/";
            String url = dataLoadedApod[position][4];
            int length = url.length();
            String result = url.substring(length - 3, length);
            if (!result.equals("jpg")) {
                String key = url.substring(url_base_embed.length(), url.length() - 6);
                String urlResult = url_base_youtube_video + key + "/maxresdefault.jpg";
                iv_photo_apod_detail.setImageUrl(urlResult,
                        ImageLoaderHelper.getInstance(mContext).getImageLoader());
                setColorLinearlayout(urlResult);
            } else {
                iv_photo_apod_detail.setImageUrl(dataLoadedApod[position][4],
                        ImageLoaderHelper.getInstance(mContext).getImageLoader());
                setColorLinearlayout(dataLoadedApod[position][4]);
            }
            iv_photo_apod_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), ImageApodActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("isFavorited", isFavorited);
                    startActivity(intent);
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
