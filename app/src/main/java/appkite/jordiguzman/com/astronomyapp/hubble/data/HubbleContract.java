package appkite.jordiguzman.com.astronomyapp.hubble.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class HubbleContract {

    static final String AUTHORITY = "appkite.jordiguzman.com.astronomyapp.hubble";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_HUBBLE = "hubble";

    public static final class HubbleEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_HUBBLE)
                .build();
        static final String TABLE_NAME = "hubble";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CREDITS = "credits";
        public static final String COLUMN_IMAGE = "image";

    }
}
