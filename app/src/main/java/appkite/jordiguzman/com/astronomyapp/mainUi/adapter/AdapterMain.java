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

    private Context mContext;
    public static final String[] URL_MAIN ={"https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
            "o/images%2F01_apod.jpg?alt=media&token=9ae5e9e4-94f6-4165-81f5-a57be2645499",
            "https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
                    "o/images%2F02_earth.png?alt=media&token=a68e8f14-c675-4ecd-9abb-6c5154f4d62b",
            "https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
                    "o/images%2F03_planets.jpg?alt=media&token=8f822c3e-b0df-4653-84bb-2871349392a9"
    ,"https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
            "o/images%2F04_iss.jpg?alt=media&token=579b55cf-7718-43fa-98aa-57810b52e37c"
    ,"https://firebasestorage.googleapis.com/v0/b/astronomyapp-2bbd6.appspot.com/" +
            "o/images%2F05_hubble.jpg?alt=media&token=269154da-bb25-481b-b25d-adaa78cfec0c"};

     private final int [] STRINGS_MAIN= {R.string.apod, R.string.earth, R.string.planets
     ,R.string.iss, R.string.hubble};
     private final int[] STRINGS_MAIN_SUB = {R.string.apod_sub, R.string.earth_sub, R.string.planets_sub,
     R.string.iss, R.string.hubble_sub};
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
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder,  int position) {

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


    public interface ItemClickListener{
        void onClickItem(int position);
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
