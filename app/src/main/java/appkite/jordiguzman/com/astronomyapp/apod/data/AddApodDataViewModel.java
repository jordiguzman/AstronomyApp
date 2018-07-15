package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddApodDataViewModel extends ViewModel{

    private LiveData<ApodEntry> apodEntryLiveData;

    AddApodDataViewModel(AppDatabase database, int apodId){
        apodEntryLiveData = database.apodDao().loadApodById(apodId);
    }

    public LiveData<ApodEntry> getApodEntryLiveData(){
        return apodEntryLiveData;
    }
}
