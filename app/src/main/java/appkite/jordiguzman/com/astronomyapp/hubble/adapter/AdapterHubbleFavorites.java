package appkite.jordiguzman.com.astronomyapp.hubble.adapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.FavoritesHubbleActivity.dataLoadedHubble;
import static appkite.jordiguzman.com.astronomyapp.hubble.ui.FavoritesHubbleActivity.hubbleArrayList;

public class AdapterHubbleFavorites extends RecyclerView.Adapter<AdapterHubbleFavorites.AdapterHubbleFavoritesViewHolder>{

    private Context mContext;
    private ItemClickListenerHubbleFavorites mItemClickListenerHubbleFavorites;

    public AdapterHubbleFavorites(ArrayList<String[]> arrayList, Context context,
                                  ItemClickListenerHubbleFavorites itemClickListenerHubbleFavorites){
        hubbleArrayList = arrayList;
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
    public void onBindViewHolder(@NonNull AdapterHubbleFavoritesViewHolder holder, int position) {

        Glide.with(mContext)
                .load(dataLoadedHubble[position][3])
                .into(holder.iv_hubble);
        holder.tv_title_hubble.setText(dataLoadedHubble[position][0]);

    }

    @Override
    public int getItemCount() {
        return hubbleArrayList.size();
    }
    public interface ItemClickListenerHubbleFavorites{
        void onClickItem(int position);
    }

    class AdapterHubbleFavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ImageView iv_hubble;
         TextView tv_title_hubble;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterHubbleFavoritesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_hubble = itemView.findViewById(R.id.iv_hubble);
            tv_title_hubble =itemView.findViewById(R.id.tv_title_huble);
            iv_hubble.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerHubbleFavorites.onClickItem(clickPosition);
        }
    }
}
