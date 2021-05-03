package com.easierphone.storageapp.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import com.easierphone.storageapp.BuildConfig;
import com.easierphone.storageapp.model.DocumentsContract;

public class ExplorerProvider extends ContentProvider {
    private static final String TAG = "ExplorerProvider";

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".explorer";
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_BOOKMARK = 1;
    private static final int URI_BOOKMARK_ID = 2;

    static {
        sMatcher.addURI(AUTHORITY, "bookmark", URI_BOOKMARK);
        sMatcher.addURI(AUTHORITY, "bookmark/*", URI_BOOKMARK_ID);
    }

    public static final String TABLE_BOOKMARK = "bookmark";

    public static class BookmarkColumns {
        public static final String TITLE = "title";
        public static final String ROOT_ID = DocumentsContract.Root.COLUMN_ROOT_ID;
        public static final String DOCUMENT_ID = DocumentsContract.Document.COLUMN_DOCUMENT_ID;
        public static final String AUTHORITY = "authority";
        public static final String ICON = "icon";
        public static final String PATH = "path";
        public static final String FLAGS = "flags";
    }

    public static Uri buildBookmark() {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY).appendPath(TABLE_BOOKMARK).build();
    }

    private DatabaseHelper mHelper;

    @SuppressWarnings("unused")
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "internal.db";
        private final Context mContext;
        private static final int VERSION_INIT = 5;
        private static final int VERSION_CONNECTIONS = 6;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, VERSION_CONNECTIONS);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTablesV1(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            int upgradeTo = oldVersion + 1;
            while (upgradeTo <= newVersion) {
                switch (upgradeTo) {
                    case VERSION_CONNECTIONS:
                        break;
                }
                upgradeTo++;
            }
        }

        private void createTablesV1(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_BOOKMARK + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BookmarkColumns.TITLE + " TEXT," +
                    BookmarkColumns.AUTHORITY + " TEXT," +
                    BookmarkColumns.ROOT_ID + " TEXT," +
                    BookmarkColumns.ICON + " INTEGER," +
                    BookmarkColumns.PATH + " TEXT," +
                    BookmarkColumns.FLAGS + " INTEGER," +
                    "UNIQUE (" + BookmarkColumns.AUTHORITY + ", " + BookmarkColumns.ROOT_ID + ", " + BookmarkColumns.PATH + ") ON CONFLICT REPLACE " +
                    ")");
        }

    }

    public ExplorerProvider() {
    }

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        switch (sMatcher.match(uri)) {
            case URI_BOOKMARK:
                return db.query(TABLE_BOOKMARK, projection, selection,
                        selectionArgs, null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Unsupported Uri " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sMatcher.match(uri)) {
            case URI_BOOKMARK:
                // Ensure that row exists, then update with changed values
                //db.insertWithOnConflict(TABLE_BOOKMARK, null, key, SQLiteDatabase.CONFLICT_IGNORE);
                db.insert(TABLE_BOOKMARK, null, values);

                return uri;
            default:
                throw new UnsupportedOperationException("Unsupported Uri " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sMatcher.match(uri)) {
            default:
                throw new UnsupportedOperationException("Unsupported Uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        int count;
        String id;
        switch (sMatcher.match(uri)) {
            case URI_BOOKMARK_ID:
                id = uri.getLastPathSegment();
                count = db.delete(TABLE_BOOKMARK,
                        BaseColumns._ID + "=?",
                        new String[]{id});
                break;
            case URI_BOOKMARK:
                count = db.delete(TABLE_BOOKMARK,
                        selection,
                        selectionArgs);

                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri " + uri);
        }

        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
