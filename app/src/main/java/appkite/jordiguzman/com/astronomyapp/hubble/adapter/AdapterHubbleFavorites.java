package appkite.jordiguzman.com.astronomyapp.hubble.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleEntry;

public class AdapterHubbleFavorites extends RecyclerView.Adapter<AdapterHubbleFavorites.AdapterHubbleFavoritesViewHolder>{

    private Context mContext;
    private ItemClickListenerHubbleFavorites mItemClickListenerHubbleFavorites;
    private final Handler handler = new Handler();
    private List<HubbleEntry> mHubbleData;
    public AdapterHubbleFavorites(List<HubbleEntry> hubbleEntries, Context context,
                                  ItemClickListenerHubbleFavorites itemClickListenerHubbleFavorites){
        this.mHubbleData = hubbleEntries;
        this.mContext = context;
        this.mItemClickListenerHubbleFavorites = itemClickListenerHubbleFavorites;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public AdapterHubbleFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_hubble, parent, false);
        return new AdapterHubbleFavoritesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterHubbleFavoritesViewHolder holder, int position) {
        final String url =mHubbleData.get(position).getImage();
        Glide.with(mContext)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mContext)
                                        .load(url)
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.ic_galaxy)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .override(800,800))
                                        .into(holder.iv_hubble);
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
                .into(holder.iv_hubble);
        holder.tv_title_hubble.setText(mHubbleData.get(position).getName());
        String creditsTemp = String.valueOf(Html.fromHtml(mHubbleData.get(position).getCredits()));
        holder.tv_credits.setText(creditsTemp);
    }

    @Override
    public int getItemCount() {
        if (mHubbleData== null){
            return 0;
        }
        return mHubbleData.size();
    }


    public List<HubbleEntry> getHubbleData(){
        return mHubbleData;
    }
    public void setHubbleData(List<HubbleEntry> hubbleEntries){
        mHubbleData = hubbleEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListenerHubbleFavorites{
        void onClickItem(int position);
    }

    class AdapterHubbleFavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ImageView iv_hubble;
         TextView tv_title_hubble, tv_credits;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterHubbleFavoritesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_hubble = itemView.findViewById(R.id.iv_hubble);
            tv_title_hubble =itemView.findViewById(R.id.tv_title_huble);
            tv_credits = itemView.findViewById(R.id.tv_credits);
            iv_hubble.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerHubbleFavorites.onClickItem(clickPosition);
        }
    }
}
