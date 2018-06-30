package appkite.jordiguzman.com.astronomyapp.hubble.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.DynamicHeightNetworkImageView;
import appkite.jordiguzman.com.astronomyapp.mainUi.utils.ImageLoaderHelper;
import appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract;

import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.COLUMN_CREDITS;
import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.COLUMN_DESCRIPTION;
import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.COLUMN_ID;
import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.COLUMN_IMAGE;
import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.COLUMN_NAME;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.dataImages;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.dataImagesDetail;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.itemPositionHubble;

public class HubbleDetailFragment  extends Fragment implements View.OnClickListener{

    private Context mContext;
    private int caseSnackBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    }

    private boolean isFavoritedHubble(){
        Cursor cursor = mContext.getContentResolver().query(HubbleContract.HubbleEntry.CONTENT_URI,
                null,
                null,
                null,
                HubbleContract.HubbleEntry._ID);
        if (cursor != null){
            while (cursor.moveToNext()){
                String idHubble = String.valueOf(cursor.getInt(1));
                String idHubbleActual = dataImages.get(itemPositionHubble).getId();
                if (idHubble.equals(idHubbleActual)){
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
            case R.id.fb_favorites_hubble:
                if (isFavoritedHubble()){
                    saveHubbleData();
                }else {
                    caseSnackBar = 1;
                    showSnackBarHubble();
                }
                break;

        }
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



            DynamicHeightNetworkImageView photo_hubble_detail = view.findViewById(R.id.photo_hubble_detail);
            photo_hubble_detail.setImageUrl(dataImagesDetail.get(position).getImage(),
                    ImageLoaderHelper.getInstance(mContext).getImageLoader());

            photo_hubble_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageHubbleActivity.class);
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
    public void saveHubbleData(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, HubbleActivity.dataImages.get(itemPositionHubble).getId());
        contentValues.put(COLUMN_NAME, dataImagesDetail.get(itemPositionHubble).getName());
        contentValues.put(COLUMN_DESCRIPTION, dataImagesDetail.get(itemPositionHubble).getDescription());
        contentValues.put(COLUMN_CREDITS, dataImagesDetail.get(itemPositionHubble).getCredits());

        contentValues.put(COLUMN_IMAGE, dataImagesDetail.get(itemPositionHubble).getImage());
        ContentResolver resolver = mContext.getContentResolver();
        resolver.insert(HubbleContract.HubbleEntry.CONTENT_URI, contentValues);
        caseSnackBar = 0;
        showSnackBarHubble();

    }

    private void showSnackBarHubble() {
        Snackbar snackbarHubble;
        switch (caseSnackBar){
            case 0:
                snackbarHubble = Snackbar.make(getActivity().findViewById(R.id.card_fragment_hubble), R.string.data_saved, Snackbar.LENGTH_LONG );
                View snackbarView = snackbarHubble.getView();
                int snackbarTextId = android.support.design.R.id.snackbar_text;
                TextView textView = snackbarView.findViewById(snackbarTextId);
                textView.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                snackbarHubble.show();
                break;
            case 1:
                snackbarHubble = Snackbar.make(getActivity().findViewById(R.id.card_fragment_hubble), R.string.is_favorited, Snackbar.LENGTH_LONG );
                View snackbarView1 = snackbarHubble.getView();
                int snackbarTextId1 = android.support.design.R.id.snackbar_text;
                TextView textView1 = snackbarView1.findViewById(snackbarTextId1);
                textView1.setTextColor(ContextCompat.getColor(mContext,  R.color.colorAccent));
                snackbarHubble.show();
                break;
        }
    }


}
