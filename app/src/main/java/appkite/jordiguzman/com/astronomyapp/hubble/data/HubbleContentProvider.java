package appkite.jordiguzman.com.astronomyapp.hubble.data;


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

import static appkite.jordiguzman.com.astronomyapp.hubble.data.HubbleContract.HubbleEntry.TABLE_NAME;


public class HubbleContentProvider extends ContentProvider{

    public static final int HUBBLE = 101;
    public static final int HUBBLE_WITH_ID = 102;
     private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(HubbleContract.AUTHORITY, HubbleContract.PATH_HUBBLE, HUBBLE);
        uriMatcher.addURI(HubbleContract.AUTHORITY, HubbleContract.PATH_HUBBLE + "/#", HUBBLE_WITH_ID);
        return uriMatcher;
    }
    private HubbleDbHelper mHubbleDbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mHubbleDbHelper = new HubbleDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
       final SQLiteDatabase db
                = mHubbleDbHelper.getWritableDatabase();
       int match = sUriMatcher.match(uri);
       Uri returnUri;
       switch (match){
           case HUBBLE:
               long id = db.insert(TABLE_NAME, null, values);
               if (id > 0){
                   returnUri = ContentUris.withAppendedId(HubbleContract.HubbleEntry.CONTENT_URI, id);
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
        final SQLiteDatabase db = mHubbleDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case HUBBLE:
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
       final SQLiteDatabase db = mHubbleDbHelper.getWritableDatabase();
       int match = sUriMatcher.match(uri);
       int hubbleDeleted;
       switch (match){
           case HUBBLE_WITH_ID:
               String id = uri.getPathSegments().get(1);
               hubbleDeleted = db.delete(TABLE_NAME, HubbleContract.HubbleEntry._ID + "=?", new String[]{id});
               break;
           default:
               throw new UnsupportedOperationException(getContext().getString(R.string.unkow_uri) + uri);
       }
       if (hubbleDeleted != 0){
           getContext().getContentResolver().notifyChange(uri, null);
       }
       return hubbleDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
