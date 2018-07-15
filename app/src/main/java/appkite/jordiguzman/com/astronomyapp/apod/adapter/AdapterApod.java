package appkite.jordiguzman.com.astronomyapp.apod.adapter;

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;


public class AdapterApod extends RecyclerView.Adapter<AdapterApod.AdapterApodViewHolder> {


    private final Context mContext;
    public static ArrayList<Apod> mApodData = new ArrayList<>();
    private final ItemClickListenerApod mItemClickListenerApod;


    public AdapterApod(ArrayList<Apod> apods, Context context, ItemClickListenerApod itemClickListenerApod) {
        mApodData = apods;
        this.mContext = context;
        this.mItemClickListenerApod = itemClickListenerApod;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public AdapterApodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_apod, parent, false);

        return new AdapterApodViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApodViewHolder holder, int position) {
        String url_base_youtube_video = "http://img.youtube.com/vi/";
        String url_base_embed = "https://www.youtube.com/embed/";
        holder.tv_title.setText(mApodData.get(position).getTitle());
        holder.tv_date.setText(mApodData.get(position).getDate());
        String url = mApodData.get(position).getUrl();
        int length = url.length();
        String result = url.substring(length - 3, length);

        if (result.equals("peg") || result.equals("jpg")
                || result.equals("gif") || result.equals("png")){
            Glide.with(mContext)
                    .load(mApodData.get(position).getUrl())
                    .apply(new RequestOptions().transform(new RoundedCorners(15))
                            .error(R.drawable.ic_galaxy)
                            .placeholder(R.drawable.ic_galaxy))
                    .into(holder.iv_apod);
        }
        else   {
            String key = url.substring(url_base_embed.length(), url.length() - 6);
            String urlResult = url_base_youtube_video + key + "/0.jpg";
            Glide.with(mContext)
                    .load(urlResult)
                    .apply(new RequestOptions().transform(new RoundedCorners(15))
                            .error(R.drawable.ic_galaxy)
                            .placeholder(R.drawable.ic_galaxy))
                    .into(holder.iv_apod);
        }


    }

    @Override
    public int getItemCount() {
        if (mApodData == null){
            return 0;
        }
        return mApodData.size();
    }

    public interface ItemClickListenerApod {
        void onClickItem(int position);
    }

    class AdapterApodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_title, tv_date;
        final ImageView iv_apod;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterApodViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_apod = itemView.findViewById(R.id.iv_item_apod);
            iv_apod.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerApod.onClickItem(clickPosition);
        }
    }

}
