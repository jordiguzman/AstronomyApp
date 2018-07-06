package appkite.jordiguzman.com.astronomyapp.earth.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;
import appkite.jordiguzman.com.astronomyapp.widget.GlideApp;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthDetailActivity.dateApi;

public class AdapterEarth extends RecyclerView.Adapter<AdapterEarth.AdapterEarthViewHolder> {

    private Context mContext;
    private ArrayList<Earth> mEarthData;
    private ItemClickListenerEarth mItemClickListenerEarth;
    private final Handler handler = new Handler();



    public AdapterEarth(Context mContext, ArrayList<Earth> mEarthData, ItemClickListenerEarth itemClickListenerEarth) {
        this.mContext = mContext;
        this.mEarthData = mEarthData;
        this.mItemClickListenerEarth = itemClickListenerEarth;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public AdapterEarthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_earth, parent, false);

        return new AdapterEarthViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterEarthViewHolder holder, int position) {
        String URL = "https://epic.gsfc.nasa.gov/archive/natural/";
        final String  url = URL + dateApi+ "png/" + earthArrayList.get(position).getImage() +
                ".png";



        GlideApp.with(mContext)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                GlideApp.with(mContext)
                                        .load(url)
                                        .into(holder.iv_earth);
                            }
                        });
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(Glide.with(mContext)
                .load(url))
                .apply(new RequestOptions().transform(new RoundedCorners(15))
                        .placeholder(R.drawable.ic_galaxy)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(800,800))
                .into(holder.iv_earth);

         holder.tv_date.setText(mEarthData.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return mEarthData.size();
    }

    public interface ItemClickListenerEarth{
        void onClickItem(int position);
    }

    class AdapterEarthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_date;
        ImageView iv_earth;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterEarthViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.tv_date_earth);
            iv_earth = itemView.findViewById(R.id.iv_earth);
            iv_earth.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerEarth.onClickItem(clickPosition);
        }
    }
}
