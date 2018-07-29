package appkite.jordiguzman.com.astronomyapp.iss.adapter;


import android.annotation.SuppressLint;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.iss.model.Astronaut;

public class AdapterAstronaut extends RecyclerView.Adapter<AdapterAstronaut.AdapterAstronautViewHolder>{

    private final Context mContext;
    private final ArrayList<Astronaut> mAstronautData;
    private final TwitterWikiClickListener  mTwitterWikiClickListener;

    public AdapterAstronaut(ArrayList<Astronaut> astronauts, Context context, TwitterWikiClickListener twitterClickListener){
        this.mAstronautData= astronauts;
        this.mContext = context;
        mTwitterWikiClickListener = twitterClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                .apply(new RequestOptions().transform(new RoundedCorners(15))
                .error(R.drawable.ic_galaxy)
                .placeholder(R.drawable.ic_galaxy))
                .into(holder.iv_astronaut);
        Glide.with(mContext)
                .load(mAstronautData.get(position).getFlag())
                .into(holder.iv_flag);
        String URL_NO_TWITTER = "https://pasalavida30.files.wordpress.com/2018/07/no_twitter.png";
        String URL_TWITTER = "https://pasalavida30.files.wordpress.com/2018/07/twitter.png";
        if (mAstronautData.get(position).getTwitter().isEmpty()){
            Glide.with(mContext)
                    .load(URL_NO_TWITTER)
                    .into(holder.iv_twitter);
            holder.iv_twitter.setClickable(false);
        }else {
            Glide.with(mContext)
                    .load(URL_TWITTER)
                    .into(holder.iv_twitter);
        }
        String URL_NO_WIKI = "https://pasalavida30.files.wordpress.com/2018/07/no_wiki.png";
        String URL_WIKI = "https://pasalavida30.files.wordpress.com/2018/07/wiki.png";
        if (mAstronautData.get(position).getBioWiki().isEmpty()){
             Glide.with(mContext)
                     .load(URL_NO_WIKI)
                     .into(holder.iv_wikipedia);
            holder.iv_wikipedia.setClickable(false);
        }else {
            Glide.with(mContext)
                    .load(URL_WIKI)
                    .into(holder.iv_wikipedia);
        }
        holder.tv_name_astronaut.setText(mAstronautData.get(position).getName());
        holder.tv_title_astronaut.setText(String.format("%s at the ISS", mAstronautData.get(position).getRole()));
        holder.tv_bio.setText(mAstronautData.get(position).getBio());

        try {
            holder.tv_time_in_space.setText(String.format("%s days in space", String.valueOf(calculateDaysInSpace(position))));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    private Integer calculateDaysInSpace(int position) throws ParseException{
        Date today = Calendar.getInstance().getTime();
        long diff;
        int daysInSpace;
        diff= today.getTime() - convertDate(position).getTime();
        daysInSpace = (int) TimeUnit.MILLISECONDS.toDays(diff);
        return daysInSpace;
    }
    private Date convertDate(int position) throws ParseException {
        String dateLaunchTemp = mAstronautData.get(position).getLaunchDate().substring(0, 10);
        int a = 4;
        int b = 7;
        dateLaunchTemp = new StringBuilder(dateLaunchTemp).replace(a, a+1, "/").replace(b, b+1, "/").toString();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.parse(dateLaunchTemp);
    }
    @Override
    public int getItemCount() {
        return mAstronautData.size();
    }

    public interface TwitterWikiClickListener {
        void onClickTwiter(int position, int view);

    }

    class AdapterAstronautViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView iv_astronaut, iv_flag, iv_twitter, iv_wikipedia;
        TextView tv_name_astronaut, tv_title_astronaut, tv_bio, tv_time_in_space;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        AdapterAstronautViewHolder(View itemView) {
            super(itemView);
            iv_astronaut = itemView.findViewById(R.id.iv_astronaut);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            iv_twitter = itemView.findViewById(R.id.iv_twitter);
            iv_wikipedia = itemView.findViewById(R.id.iv_wikipedia);
            iv_twitter.setOnClickListener(this);
            iv_wikipedia.setOnClickListener(this);
            tv_name_astronaut = itemView.findViewById(R.id.tv_name_astronaut);
            tv_title_astronaut = itemView.findViewById(R.id.tv_title_astronaut);
            tv_bio = itemView.findViewById(R.id.tv_bio);
            tv_time_in_space = itemView.findViewById(R.id.tv_time_in_space);
            iv_astronaut.setClipToOutline(true);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            switch (v.getId()){
                case R.id.iv_twitter:
                    mTwitterWikiClickListener.onClickTwiter(clickPosition, R.id.iv_twitter);
                    break;
                case R.id.iv_wikipedia:
                    mTwitterWikiClickListener.onClickTwiter(clickPosition,R.id.iv_wikipedia );
                    break;
            }

        }
    }
}
