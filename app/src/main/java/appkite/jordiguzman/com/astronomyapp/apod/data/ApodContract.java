package appkite.jordiguzman.com.astronomyapp.apod.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class ApodContract {

    static final String AUTHORITY = "appkite.jordiguzman.com.astronomyapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_APOD = "apod";

    public static final class ApodEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_APOD)
                .build();
        static final String TABLE_NAME = "apod";
        public static final String COLUMN_COPYRIGHT = "copyright";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = " date";
        public static final String COLUMN_EXPLANATION = "explanation";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_HURL = "hurl";

    }
}
