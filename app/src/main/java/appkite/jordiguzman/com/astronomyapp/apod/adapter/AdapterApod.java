package appkite.jordiguzman.com.astronomyapp.apod.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.apod.model.Apod;
import appkite.jordiguzman.com.astronomyapp.apod.ui.ApodActivity;


public class AdapterApod extends RecyclerView.Adapter<AdapterApod.AdapterApodViewHolder> {


    private Context mContext;
    private List<Apod> mApodData;



    public AdapterApod (List<Apod> apods, Context context){
        this.mApodData = apods;
        this.mContext = context;
    }



    @NonNull
    @Override
    public AdapterApodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_apod, parent, false);

        return new AdapterApodViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApodViewHolder holder, int position) {

        holder.tv_title.setText(ApodActivity.mApodData.get(position).getTitle());
        holder.tv_date.setText(ApodActivity.mApodData.get(position).getDate());
        Glide.with(mContext)
                .load(ApodActivity.mApodData.get(position).getUrl())
                .into(holder.iv_apod);

    }

    @Override
    public int getItemCount() {
        return mApodData.size();
    }


    class AdapterApodViewHolder extends RecyclerView.ViewHolder{

        final TextView tv_title, tv_date;
        final ImageView iv_apod;

        AdapterApodViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_apod = itemView.findViewById(R.id.iv_apod);
        }
    }
}
