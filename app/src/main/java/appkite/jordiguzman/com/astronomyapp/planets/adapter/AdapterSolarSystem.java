package appkite.jordiguzman.com.astronomyapp.planets.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.planets.ui.MainActivitySolarSystem.PLANETS;

public class AdapterSolarSystem extends RecyclerView.Adapter<AdapterSolarSystem.AdapterSystemViewHolder>{

    private Context mContext;
    private ArrayList<String> dataText;

    public AdapterSolarSystem(ArrayList<String> arrayList, Context context){
        this.dataText = arrayList;
        this.mContext = context;
    }

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
    }

    @Override
    public int getItemCount() {
        return PLANETS.length;
    }


    class AdapterSystemViewHolder extends RecyclerView.ViewHolder{

        final TextView tv_title;
        final ImageView iv_system;

        public AdapterSystemViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_system);
            iv_system = itemView.findViewById(R.id.iv_system_item);
        }
    }

}
