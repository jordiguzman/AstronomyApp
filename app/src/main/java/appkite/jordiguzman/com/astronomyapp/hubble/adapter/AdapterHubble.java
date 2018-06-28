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
import com.bumptech.glide.MemoryCategory;

import java.util.ArrayList;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.hubble.model.ImagesDetail;

public class AdapterHubble extends RecyclerView.Adapter<AdapterHubble.AdapterHubbleViewHolder>{

    private Context mContext;
    private ArrayList<ImagesDetail> mDataImagesDetail;
    private ItemClickListenerHubble mItemClickListenerHubble;

    public AdapterHubble(Context context, ArrayList<ImagesDetail> dataImagesDetail, ItemClickListenerHubble itemClickListenerHubble){
        mContext = context;
        mDataImagesDetail = dataImagesDetail;
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

        Glide.get(mContext)
                .setMemoryCategory(MemoryCategory.HIGH);
        Glide.with(mContext)
                .load(mDataImagesDetail.get(position).getImage())
                .into(holder.iv_hubble);
        holder.tv_hubble.setText(mDataImagesDetail.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataImagesDetail.size();
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
