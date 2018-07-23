package appkite.jordiguzman.com.astronomyapp.hubble.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.ImageLoaderHelper;

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.FavoritesHubbleActivity.itemPositionFavoritesHubble;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.FavoritesHubbleActivity.mHubbleDataList;

public class FavoritesHubbleDetailFragment extends Fragment  {

    private Context mContext;
    private int mMutedColor;
    private View linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = container.getContext();
        return inflater.inflate(R.layout.fragment_hubble_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager mViewPager = view.findViewById(R.id.pager_hubble);
        mViewPager.setAdapter(new FavoritesHubblePageAdapter());
        mViewPager.setCurrentItem(itemPositionFavoritesHubble);
        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.fb_favorites_hubble);
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                itemPositionFavoritesHubble = position;

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

    class FavoritesHubblePageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mHubbleDataList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.pager_item_hubble, container, false);
            container.addView(view);
            final boolean isFavorited = true;

            TextView tv_title_pager_hubble_item = view.findViewById(R.id.tv_title_pager_hubble_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_pager_hubble_item.setTypeface(typeface);
            tv_title_pager_hubble_item.setText(mHubbleDataList.get(position).getName());

            TextView tv_description_hubble_item = view.findViewById(R.id.tv_description_hubble_item);
            tv_description_hubble_item.setText(mHubbleDataList.get(position).getDescription());

            TextView tv_creditt_hubble_item = view.findViewById(R.id.tv_creditt_hubble_item);
            String creditTemp = String.valueOf(Html.fromHtml(mHubbleDataList.get(position).getCredits()));
            tv_creditt_hubble_item.setText(creditTemp);

            linearLayout = view.findViewById(R.id.linearLayout_hubble_detail);
            final ImageView photo_hubble_detail = view.findViewById(R.id.photo_hubble_detail);
            Glide.with(mContext)
                    .load(mHubbleDataList.get(position).getImage())
                    .into(photo_hubble_detail);

            setColorLinearlayout(mHubbleDataList.get(position).getImage());
            photo_hubble_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageHubbleActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("isFavorited", isFavorited);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            photo_hubble_detail,
                            "image");
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
                                if (isAdded()){
                                    mMutedColor = p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
                                    linearLayout.setBackgroundColor(mMutedColor);
                                }
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
