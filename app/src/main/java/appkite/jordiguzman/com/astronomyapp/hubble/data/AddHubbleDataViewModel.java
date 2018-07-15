package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class AddHubbleDataViewModel extends ViewModel {

    private LiveData<HubbleEntry> hubbleEntryLiveData;

    AddHubbleDataViewModel (AppDatabaseHubble databaseHubble, int hubbleId){
        hubbleEntryLiveData = databaseHubble.hubbleDao().loadHubbleById(hubbleId);
    }

    public LiveData<HubbleEntry> getHubbleEntryLiveData(){
        return hubbleEntryLiveData;
    }
}
