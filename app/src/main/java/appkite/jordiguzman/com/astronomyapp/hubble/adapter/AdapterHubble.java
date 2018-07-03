package appkite.jordiguzman.com.astronomyapp.hubble.adapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        String creditTemp = String.valueOf(Html.fromHtml((mDataImagesDetail.get(position).getCredits())));
        holder.tv_credits.setText(mContext.getString(R.string.credits).concat(creditTemp));

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
        TextView tv_hubble, tv_credits;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterHubbleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            iv_hubble = itemView.findViewById(R.id.iv_hubble);
            tv_hubble = itemView.findViewById(R.id.tv_title_huble);
            tv_credits = itemView.findViewById(R.id.tv_credits);
            iv_hubble.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mItemClickListenerHubble.onClickItem(clickPosition);
        }
    }
}
