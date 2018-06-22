package appkite.jordiguzman.com.astronomyapp.earth.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.earth.model.Earth;

import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthActivity.earthArrayList;
import static appkite.jordiguzman.com.astronomyapp.earth.ui.EarthDetailActivity.dateApi;

public class AdapterEarth extends RecyclerView.Adapter<AdapterEarth.AdapterEarthViewHolder> {

    private Context mContext;
    private ArrayList<Earth> mEarthData;
    private ItemClickListenerEarth mItemClickListenerEarth;



    public AdapterEarth(Context mContext, ArrayList<Earth> mEarthData, ItemClickListenerEarth itemClickListenerEarth) {
        this.mContext = mContext;
        this.mEarthData = mEarthData;
        this.mItemClickListenerEarth = itemClickListenerEarth;
    }

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
        String  url = URL + dateApi+ "png/" + earthArrayList.get(position).getImage() +
                ".png";

        Glide.with(mContext)
                .load(url)
                .error(Glide.with(mContext)
                .load(url))
                .preload(600,600);
        Glide.get(mContext)
                .setMemoryCategory(MemoryCategory.HIGH);
        Glide.with(mContext)
                .load(url)
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
        AdapterEarthViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_date = itemView.findViewById(R.id.tv_date_earth);
            iv_earth = itemView.findViewById(R.id.iv_earth);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerEarth.onClickItem(clickPosition);
        }
    }
}
