package appkite.jordiguzman.com.astronomyapp.planets.adapter;


import android.content.Context;
import android.content.res.Resources;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.PLANETS;
import static appkite.jordiguzman.com.astronomyapp.planets.data.Urls.URL_PLANETS;

public class AdapterSolarSystem extends RecyclerView.Adapter<AdapterSolarSystem.AdapterSystemViewHolder>{

    private Context mContext;
    private ItemClickListenerSystem mItemClickListenerSystem;

    public AdapterSolarSystem( Context context, ItemClickListenerSystem itemClickListenerSystem){
        this.mContext = context;
        this.mItemClickListenerSystem = itemClickListenerSystem;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public AdapterSystemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_system, parent, false);

        return new AdapterSystemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSystemViewHolder holder, int position) {
        holder.tv_title.setText(PLANETS[position]);
        Resources resources = mContext.getResources();
        String [] subtitle = resources.getStringArray(R.array.subtitle_array);
        holder.tv_subtitle.setText(subtitle[position]);
        Glide.with(mContext)
                .load(URL_PLANETS[position])
                .apply(new RequestOptions().transform(new RoundedCorners(15)))
                .into(holder.iv_system);
    }

    @Override
    public int getItemCount() {
        return PLANETS.length;
    }



    public interface ItemClickListenerSystem{
        void onClickItem(int position);
    }

    class AdapterSystemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView tv_title, tv_subtitle;
        final ImageView iv_system;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterSystemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_title = itemView.findViewById(R.id.tv_title_system);
            iv_system = itemView.findViewById(R.id.iv_system_item);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle_system);
            iv_system.setClipToOutline(true);
        }


        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerSystem.onClickItem(clickPosition);
        }
    }

}
