package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModelHubble extends AndroidViewModel {

    private LiveData<List<HubbleEntry>> hubbleData;

    public MainViewModelHubble(@NonNull Application application) {
        super(application);
        AppDatabaseHubble databaseHubble = AppDatabaseHubble.getInstance(this.getApplication());
        hubbleData = databaseHubble.hubbleDao().loadAllData();
    }

    public LiveData<List<HubbleEntry>> getHubbleData(){
        return hubbleData;
  }
}
