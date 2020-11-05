package com.example.todos;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ToDoProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.todos";
    public static final String PATH_TODO_LIST = "TODO_LIST";
    public static final String PATH_TODO_PLACE = "TODO_LIST_FROM_PLACE";
    public static final String PATH_TODO_COUNT = "TODOS_COUNT";
    public static final Uri CONTENT_URI_1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_LIST);
    public static final Uri CONTENT_URI_2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_PLACE);
    public static final Uri CONTENT_URI_3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_COUNT);
    public static final int TODOS_LIST = 1;
    public static final int TODOS_FROM_SPECIFIC_PLACE = 2;
    public static final int TODOS_COUNT = 3;
    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.todos";
    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.todos.place";
    public static final String MIME_TYPE_3 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.com.example.todos.todocount";
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_TODO_LIST, TODOS_LIST);
        MATCHER.addURI(AUTHORITY, PATH_TODO_PLACE, TODOS_FROM_SPECIFIC_PLACE);
        MATCHER.addURI(AUTHORITY, PATH_TODO_COUNT, TODOS_COUNT);
    }

    private DbAdapter dbAdapter;

    @Override
    public boolean onCreate() {
        dbAdapter = DbAdapter.getDbAdapterInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                cursor = dbAdapter.getCursorsForAllToDO();
                break;
            case TODOS_FROM_SPECIFIC_PLACE:
                cursor = dbAdapter.getCursorsForSpecificPlace(selectionArgs[0]);
                break;
            case TODOS_COUNT:
                cursor = dbAdapter.getCount();
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                return MIME_TYPE_1;
            case TODOS_FROM_SPECIFIC_PLACE:
                return MIME_TYPE_2;
            case TODOS_COUNT:
                return MIME_TYPE_3;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                returnUri = insertToDo(uri, values);
                break;
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteCount = -1;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                deleteCount = delete(selection, selectionArgs);
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updateCount = -1;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                updateCount = update(values, selection, selectionArgs);
                break;
        }
        return updateCount;
    }

    private Uri insertToDo(Uri uri, ContentValues values) {
        long id = dbAdapter.insert(values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_LIST + "/" + id);
    }

    private int delete(String selection, String[] selectionArgs) {
        return dbAdapter.delete(selection, selectionArgs);
    }

    private int update(ContentValues values, String selection, String[] selectionArgs) {
        return dbAdapter.update(values, selection, selectionArgs);
    }
}
