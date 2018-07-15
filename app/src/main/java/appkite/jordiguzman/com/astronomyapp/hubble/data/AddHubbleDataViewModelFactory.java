package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class AddHubbleDataViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabaseHubble mDb;
    private final int mHubbleDataId;

    public AddHubbleDataViewModelFactory(AppDatabaseHubble databaseHubble, int hubbleDataId){
        mDb= databaseHubble;
        mHubbleDataId = hubbleDataId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddHubbleDataViewModel(mDb, mHubbleDataId);
    }
}
