package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HubbleDbHelper  extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "hubbleDb.db";
    private static final int VERSION = 1;

    public HubbleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + HubbleContract.HubbleEntry.TABLE_NAME + " (" +
                HubbleContract.HubbleEntry._ID + " INTEGER PRIMARY KEY, " +
                HubbleContract.HubbleEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                HubbleContract.HubbleEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HubbleContract.HubbleEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                HubbleContract.HubbleEntry.COLUMN_CREDITS + " TEXT NOT NULL, " +
                HubbleContract.HubbleEntry.COLUMN_IMAGE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", HubbleContract.HubbleEntry.TABLE_NAME));
            onCreate(db);
    }
}
