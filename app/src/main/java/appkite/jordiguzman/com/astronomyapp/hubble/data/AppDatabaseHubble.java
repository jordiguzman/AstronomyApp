package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {HubbleEntry.class}, version = 1, exportSchema = false)

public abstract class AppDatabaseHubble extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "hubbleData";
    private static AppDatabaseHubble sInstance;

    public static AppDatabaseHubble getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabaseHubble.class, AppDatabaseHubble.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract HubbleDao hubbleDao();
}
