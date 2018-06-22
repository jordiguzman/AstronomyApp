package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApodDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "apodDb.db";
    private static final int VERSION = 1;

    ApodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + ApodContract.ApodEntry.TABLE_NAME + " (" +
                ApodContract.ApodEntry._ID + " INTEGER PRIMARY KEY, " +
                ApodContract.ApodEntry.COLUMN_COPYRIGHT + " TEXT NOT NULL, " +
                ApodContract.ApodEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ApodContract.ApodEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ApodContract.ApodEntry.COLUMN_EXPLANATION + " TEXT NOT NULL, " +
                ApodContract.ApodEntry.COLUMN_URL + " TEXT NOT NULL, " +
                ApodContract.ApodEntry.COLUMN_HURL + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", ApodContract.ApodEntry.TABLE_NAME));
        onCreate(db);
    }
}
