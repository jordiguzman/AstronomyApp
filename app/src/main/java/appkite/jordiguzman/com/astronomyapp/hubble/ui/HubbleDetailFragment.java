package appkite.jordiguzman.com.astronomyapp.hubble.ui;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.ui.utils.DynamicHeightNetworkImageView;
import appkite.jordiguzman.com.astronomyapp.apod.ui.utils.ImageLoaderHelper;

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.name;

public class HubbleDetailFragment  extends Fragment{

    private Context mContext;

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
        mViewPager.setCurrentItem(HubbleActivity.itemPositionHubble);
    }



    class HubbleAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.pager_item_hubble, container, false);
            container.addView(view);

            TextView tv_title_pager_hubble_item = view.findViewById(R.id.tv_title_pager_hubble_item);
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.alfa_slab_one);
            tv_title_pager_hubble_item.setTypeface(typeface);
            tv_title_pager_hubble_item.setText(HubbleActivity.name.get(position));

            TextView tv_date_hubble_item = view.findViewById(R.id.tv_date_hubble_item);
            String dateTemp = HubbleActivity.date.get(position).substring(0, 10);
            tv_date_hubble_item.setText(dateTemp);

            TextView tv_creditt_hubble_item = view.findViewById(R.id.tv_creditt_hubble_item);
            String creditTemp = String.valueOf(Html.fromHtml(HubbleActivity.credits.get(position)));
            tv_creditt_hubble_item.setText(creditTemp);

            TextView tv_abstract_hubble_item = view.findViewById(R.id.tv_abstract_hubble_item);
            String abstractTemp = String.valueOf(Html.fromHtml(HubbleActivity.abstractData.get(position)));
            tv_abstract_hubble_item.setText(abstractTemp);

            DynamicHeightNetworkImageView photo_hubble_detail = view.findViewById(R.id.photo_hubble_detail);
            photo_hubble_detail.setImageUrl(HubbleActivity.urlImage.get(position),
                    ImageLoaderHelper.getInstance(mContext).getImageLoader());

            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
