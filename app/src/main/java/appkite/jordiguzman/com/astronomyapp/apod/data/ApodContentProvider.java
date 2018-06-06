package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import appkite.jordiguzman.com.astronomyapp.R;

import static appkite.jordiguzman.com.astronomyapp.apod.data.ApodContract.ApodEntry.TABLE_NAME;

public class ApodContentProvider extends ContentProvider{
    
    public static final int APODS = 101;
    public static final int APODS_WITH_ID = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    
    
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ApodContract.AUTHORITY, ApodContract.PATH_APOD, APODS);
        uriMatcher.addURI(ApodContract.AUTHORITY, ApodContract.PATH_APOD + "/#", APODS_WITH_ID);
        return uriMatcher;
    }
    private ApodDbHelper mApodDbHelper;
    
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mApodDbHelper = new ApodDbHelper(context);
        return true;
    }


    @SuppressLint("StringFormatInvalid")
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
         final SQLiteDatabase db = mApodDbHelper.getWritableDatabase();
         int match = sUriMatcher.match(uri);
         Uri returnUri;
         switch (match){
             case APODS:
                 long id = db.insert(TABLE_NAME, null, values);
                 if (id > 0){
                    returnUri = ContentUris.withAppendedId(ApodContract.ApodEntry.CONTENT_URI, id);
                 }else throw new SQLException(getContext().getString(R.string.failed_insert_row) + uri.toString());
                 break;
                 default:
                     throw new UnsupportedOperationException(getContext().getString(R.string.unkow_uri) + uri);
         }
         getContext().getContentResolver().notifyChange(uri, null);
         return returnUri;
    }
    
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mApodDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case APODS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unkow_uri) + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mApodDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int apodDeteled;
        switch (match){
            case APODS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                apodDeteled = db.delete(TABLE_NAME, ApodContract.ApodEntry._ID + "=?", new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unkow_uri) + uri);
        }
        if (apodDeteled !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return apodDeteled;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
