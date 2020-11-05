package com.example.todos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbAdapter {

    public static final String TAG = DbAdapter.class.getSimpleName();
    public static final String DB_NAME = "todolist.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_TODO = "table_todo";
    public static final String COLUMN_TODO_ID = "task_id";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_PLACE = "place";

//    private static String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "(" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY, " + COLUMN_TODO + " TEXT NOT NULL) ";
    public static String CREATE_TABLE_TODO="CREATE TABLE "+TABLE_TODO+"("+COLUMN_TODO_ID+" INTEGER PRIMARY KEY, "+COLUMN_TODO+" TEXT NOT NULL, "+ COLUMN_PLACE+ " TEXT )";
    private static DbAdapter dbAdapterInstance;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    private DbAdapter(Context context) {
        this.context = context;
        sqLiteDatabase = new DbHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static DbAdapter getDbAdapterInstance(Context context) {
        if (dbAdapterInstance == null) {
            dbAdapterInstance = new DbAdapter(context);
            return dbAdapterInstance;
        }
        return dbAdapterInstance;
    }

    public Cursor getCursorsForAllToDO() {
        Cursor cursor = sqLiteDatabase.query(TABLE_TODO,
                new String[]{COLUMN_TODO_ID, COLUMN_TODO, COLUMN_PLACE},
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor getCursorsForSpecificPlace(String place) {
        Cursor cursor = sqLiteDatabase.query(TABLE_TODO,
                new String[]{COLUMN_TODO_ID, COLUMN_TODO},
                COLUMN_PLACE + "LIKE '%" + place + "%'",
                null,
                null,
                null,
                null);
        return cursor;
    }

    public Cursor getCount() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_TODO, null);
//        new String[]{COLUMN_TODO_ID, COLUMN_TODO, COLUMN_PLACE},
//                null,
//                null,
//                null,
//                null,
//                null);
        return cursor;
    }


    public boolean insert(String toDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, toDoItem);

        return sqLiteDatabase.insert(TABLE_TODO, null, contentValues) > 0;
    }

    public void  insert(String toDoItem, String place) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, toDoItem);
        contentValues.put(COLUMN_PLACE, place);
    }

    public long insert(ContentValues contentValues1) {
        return sqLiteDatabase.insert(TABLE_TODO, null, contentValues1);
    }

    public boolean delete(int taskId) {
        return sqLiteDatabase.delete(TABLE_TODO, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    public int delete(String whereClause, String[] whereValues) {
        return sqLiteDatabase.delete(TABLE_TODO, whereClause, whereValues);
    }

    public boolean modify(int taskId, String newToDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, newToDoItem);

        return sqLiteDatabase.update(TABLE_TODO, contentValues, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    public int update(ContentValues contentValues, String s, String[] strings) {
        return sqLiteDatabase.update(TABLE_TODO, contentValues, s, strings);
    }

    public List<ToDo> getAllToDos() {
        List<ToDo> toDoList = new ArrayList<ToDo>();
        Log.d(TAG, CREATE_TABLE_TODO);
        Cursor cursor = sqLiteDatabase.query(TABLE_TODO,
                new String[]{COLUMN_TODO_ID, COLUMN_TODO},
                null,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ToDo toDo = new ToDo(cursor.getLong(0), cursor.getString(1));
                toDoList.add(toDo);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return toDoList;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_TODO);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
