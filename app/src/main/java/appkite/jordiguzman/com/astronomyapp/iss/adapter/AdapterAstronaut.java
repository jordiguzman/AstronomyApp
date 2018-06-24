package appkite.jordiguzman.com.astronomyapp.iss.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.iss.model.Astronaut;

public class AdapterAstronaut extends RecyclerView.Adapter<AdapterAstronaut.AdapterAstronautViewHolder>{

    private Context mContext;
    private ArrayList<Astronaut> mAstronautData;

    public AdapterAstronaut(ArrayList<Astronaut> astronauts, Context context){
        this.mAstronautData= astronauts;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AdapterAstronautViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_iss_astronaut, parent, false);
        return new AdapterAstronautViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAstronautViewHolder holder, int position) {

        Glide.with(mContext)
                .load(mAstronautData.get(position).getBioPhoto())
                .into(holder.iv_astronaut);
        Glide.with(mContext)
                .load(mAstronautData.get(position).getFlag())
                .into(holder.iv_flag);
        holder.tv_name_astronaut.setText(mAstronautData.get(position).getName());
        holder.tv_title_astronaut.setText(mAstronautData.get(position).getRole());
        holder.tv_bio.setText(mAstronautData.get(position).getBio());

    }

    @Override
    public int getItemCount() {
        return mAstronautData.size();
    }

    class AdapterAstronautViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_astronaut, iv_flag;
        TextView tv_name_astronaut, tv_title_astronaut, tv_bio;

        AdapterAstronautViewHolder(View itemView) {
            super(itemView);
            iv_astronaut = itemView.findViewById(R.id.iv_astronaut);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            tv_name_astronaut = itemView.findViewById(R.id.tv_name_astronaut);
            tv_title_astronaut = itemView.findViewById(R.id.tv_title_astronaut);
            tv_bio = itemView.findViewById(R.id.tv_bio);
        }
    }
}
