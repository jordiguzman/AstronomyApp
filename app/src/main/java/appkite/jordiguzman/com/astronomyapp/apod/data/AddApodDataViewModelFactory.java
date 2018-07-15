package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class AddApodDataViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final  int mApodDataId;

    public AddApodDataViewModelFactory(AppDatabase database, int apodDataId){
        mDb = database;
        mApodDataId = apodDataId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddApodDataViewModel(mDb, mApodDataId);
    }
}
