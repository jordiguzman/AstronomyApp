package appkite.jordiguzman.com.astronomyapp.hubble.adapter;


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

import static appkite.jordiguzman.com.astronomyapp.hubble.ui.HubbleActivity.urlThumbnail;

public class AdapterHubble extends RecyclerView.Adapter<AdapterHubble.AdapterHubbleViewHolder>{

    private Context mContext;
    private ArrayList<String> nameAdapter;
    private ItemClickListenerHubble mItemClickListenerHubble;

    public AdapterHubble(Context context, ArrayList<String> name, ItemClickListenerHubble itemClickListenerHubble){
        mContext = context;
        nameAdapter = name;
        mItemClickListenerHubble = itemClickListenerHubble;
    }


    @NonNull
    @Override
    public AdapterHubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_hubble, parent, false);

        return new AdapterHubbleViewHolder(rootView);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterHubbleViewHolder holder, int position) {

        Glide.with(mContext)
                .load(urlThumbnail.get(position))
                .into(holder.iv_hubble);
        holder.tv_hubble.setText(nameAdapter.get(position));
    }

    @Override
    public int getItemCount() {
        return nameAdapter.size();
    }

    public interface ItemClickListenerHubble{
        void onClickItem(int position);
    }

    class AdapterHubbleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView iv_hubble;
        TextView tv_hubble;

        AdapterHubbleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_hubble = itemView.findViewById(R.id.iv_hubble);
            tv_hubble = itemView.findViewById(R.id.tv_title_huble);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerHubble.onClickItem(clickPosition);
        }
    }
}
