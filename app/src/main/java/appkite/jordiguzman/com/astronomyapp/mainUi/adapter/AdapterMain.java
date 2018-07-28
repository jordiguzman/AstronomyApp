package appkite.jordiguzman.com.astronomyapp.mainUi.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import appkite.jordiguzman.com.astronomyapp.R;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.AdapterViewHolder> {

    public static final String[] URL_MAIN = {"https://pasalavida30.files.wordpress.com/2018/07/01_apod.jpg",
            "https://pasalavida30.files.wordpress.com/2018/07/02_earth.png",
            "https://pasalavida30.files.wordpress.com/2018/07/03_planets.jpg",
            "https://pasalavida30.files.wordpress.com/2018/07/04_iss.jpg",
            "https://pasalavida30.files.wordpress.com/2018/07/05_hubble.jpg"};
    private final int[] STRINGS_MAIN = {R.string.apod, R.string.earth, R.string.planets
            , R.string.iss, R.string.hubble};
    private final int[] STRINGS_MAIN_SUB = {R.string.apod_sub, R.string.earth_sub, R.string.planets_sub,
            R.string.iss, R.string.hubble_sub};
    private Context mContext;
    private ItemClickListener mItemClickListener = null;

    public AdapterMain(Context mContext, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.mItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View rootView = inflater.inflate(R.layout.item_main, parent, false);


        return new AdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder, int position) {

        holder.tv_main_title.setText(STRINGS_MAIN[position]);
        holder.tv_main_subtitle.setText(STRINGS_MAIN_SUB[position]);
        Glide.with(mContext)
                .load(URL_MAIN[position])
                .into(holder.iv_thumbnail);


    }

    @Override
    public int getItemCount() {
        return URL_MAIN.length;
    }


    public interface ItemClickListener {
        void onClickItem(int position);
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_thumbnail;
        TextView tv_main_title, tv_main_subtitle;


        AdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_thumbnail = itemView.findViewById(R.id.thumbnail);
            tv_main_title = itemView.findViewById(R.id.main_title);
            tv_main_subtitle = itemView.findViewById(R.id.main_subtitle);

        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();

            mItemClickListener.onClickItem(clickPosition);
        }
    }
}
