package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<ApodEntry>> apodData;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        apodData = database.apodDao().loadAllData();
    }
    public LiveData<List<ApodEntry>> getApodData(){
        return apodData;
    }
}
