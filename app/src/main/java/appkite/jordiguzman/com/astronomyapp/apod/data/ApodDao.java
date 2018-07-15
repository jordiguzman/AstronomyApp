package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ApodDao {

    @Query("SELECT * FROM apod ORDER BY id")
    LiveData<List<ApodEntry>> loadAllData();

    @Insert
    void insertApod(ApodEntry apodEntry);

    @Delete
    void deleteApod(ApodEntry apodEntry);

    @Query("SELECT * FROM apod WHERE id = :id")
    LiveData<ApodEntry> loadApodById(int id);
}
