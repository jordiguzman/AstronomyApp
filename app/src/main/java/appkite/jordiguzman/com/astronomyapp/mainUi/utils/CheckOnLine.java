package appkite.jordiguzman.com.astronomyapp.mainUi.utils;


import android.content.Context;
import android.net.ConnectivityManager;

public class CheckOnLine {


    private Context mContext;

    public CheckOnLine(Context context){
        mContext = context;
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



}
