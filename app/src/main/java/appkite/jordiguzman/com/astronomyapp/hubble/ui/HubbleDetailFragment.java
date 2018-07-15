package appkite.jordiguzman.com.astronomyapp.hubble.ui;


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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.data.AppDatabaseHubble;
import appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleEntry;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.AppExecutors;

import static appkite.jordiguzman.com.astronomyapp.hubble.adapter.AdapterHubble.mDataImagesDetail;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.dataImagesDetail;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.itemPositionHubble;

public class HubbleDetailFragment  extends Fragment implements View.OnClickListener{

    private Context mContext;
    private int caseSnackBar;
    private int mMutedColor;
    private View linearLayout;
    private HubbleAdapter hubbleAdapter = new HubbleAdapter();
    private AppDatabaseHubble mDb;
    public static ArrayList<String> names = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert  container != null;
        mContext = container.getContext();

        return inflater.inflate(R.layout.fragment_hubble_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager mViewPager = view.findViewById(R.id.pager_hubble);
        mViewPager.setAdapter(new HubbleAdapter());
        mViewPager.setCurrentItem(itemPositionHubble);
        FloatingActionButton fb_favorites = view.findViewById(R.id.fb_favorites_hubble);
        fb_favorites.setOnClickListener(this);
        hubbleAdapter.notifyDataSetChanged();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                itemPositionHubble = position;
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

    private boolean isFavoritedHubble(){
        String idItem = mDataImagesDetail.get(itemPositionHubble).getName();
        Log.i("ideItem", mDataImagesDetail.get(itemPositionHubble).getName());
        for (int i= 0; i < names.size(); i++){
            String idItemFavorites = names.get(i);
            Log.i("idItemFavorites", names.get(i));
            if (idItem.equals(idItemFavorites)){
                Log.i("idItemFavorites", idItemFavorites);
                return false;
            }
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_favorites_hubble:
                if (isFavoritedHubble()){
                     saveDataHubble();

                }else {
                    caseSnackBar = 1;
                    showSnackBarHubble();
                }
                break;

        }
    }
    public void saveDataHubble(){
        mDb = AppDatabaseHubble.getInstance(getContext());
        String name = dataImagesDetail.get(itemPositionHubble).getName();
        String description = dataImagesDetail.get(itemPositionHubble).getDescription();
        String credits = dataImagesDetail.get(itemPositionHubble).getCredits();
        String image = dataImagesDetail.get(itemPositionHubble).getImage();
        final HubbleEntry hubbleEntry = new HubbleEntry(name, description, credits,
                image);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.hubbleDao().insertHubble(hubbleEntry);
                names.add(dataImagesDetail.get(itemPositionHubble).getName());
            }
        });
        caseSnackBar = 0;
        showSnackBarHubble();
    }


    class HubbleAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return dataImagesDetail.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.pager_item_hubble, container, false);
            container.addView(view);


            TextView tv_title_pager_hubble_item = view.findViewById(R.id.tv_title_pager_hubble_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_pager_hubble_item.setTypeface(typeface);
            tv_title_pager_hubble_item.setText(dataImagesDetail.get(position).getName());

            TextView tv_description_hubble_item = view.findViewById(R.id.tv_description_hubble_item);
            tv_description_hubble_item.setText(Html.fromHtml(dataImagesDetail.get(position).getDescription()));

            TextView tv_creditt_hubble_item = view.findViewById(R.id.tv_creditt_hubble_item);
            String creditTemp = String.valueOf(Html.fromHtml(dataImagesDetail.get(position).getCredits()));
            tv_creditt_hubble_item.setText(creditTemp);


            linearLayout = view.findViewById(R.id.linearLayout_hubble_detail);
            final ImageView photo_hubble_detail = view.findViewById(R.id.photo_hubble_detail);
            Glide.with(mContext)
                    .load(dataImagesDetail.get(position).getImage())
                    .into(photo_hubble_detail);



            setColorLinearlayout(dataImagesDetail.get(position).getImage());




            photo_hubble_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageHubbleActivity.class);
                    intent.putExtra("position", position);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity(),
                            photo_hubble_detail,
                            "image");
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



        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    private void showSnackBarHubble() {
        Snackbar snackbarHubble;
        switch (caseSnackBar){
            case 0:
                snackbarHubble = Snackbar.make(getActivity().findViewById(R.id.card_fragment_hubble), R.string.data_saved, Snackbar.LENGTH_SHORT );
                View snackbarView = snackbarHubble.getView();
                int snackbarTextId = android.support.design.R.id.snackbar_text;
                TextView textView = snackbarView.findViewById(snackbarTextId);
                textView.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                snackbarHubble.show();
                break;
            case 1:
                snackbarHubble = Snackbar.make(getActivity().findViewById(R.id.card_fragment_hubble), R.string.is_favorited, Snackbar.LENGTH_SHORT );
                View snackbarView1 = snackbarHubble.getView();
                int snackbarTextId1 = android.support.design.R.id.snackbar_text;
                TextView textView1 = snackbarView1.findViewById(snackbarTextId1);
                textView1.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                snackbarHubble.show();
                break;
        }
    }


}
