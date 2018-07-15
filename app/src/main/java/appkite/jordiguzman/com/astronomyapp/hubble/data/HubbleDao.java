package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HubbleDao {

    @Query("SELECT * FROM hubble ORDER BY id")
    LiveData<List<HubbleEntry>> loadAllData();

    @Insert
    void insertHubble(HubbleEntry hubbleEntry);

    @Delete
    void deleteHubble(HubbleEntry hubbleEntry);

    @Query("SELECT * FROM hubble WHERE id = :id")
    LiveData<HubbleEntry> loadHubbleById(int id);

}
