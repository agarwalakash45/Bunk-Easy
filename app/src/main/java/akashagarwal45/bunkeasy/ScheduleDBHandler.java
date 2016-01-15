package akashagarwal45.bunkeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=6;
    private static final String DATABASE_NAME="schedule.db";
    public static final String TABLE_SCHEDULE="schedule";
    public static final String COLUMN_SUBJECTNAME="subname";
    public static final String COLUMN_HOUR="hour";
    public static final String COLUMN_MINUTE="minute";
    public static final String COLUMN_DAY="day";
    public ScheduleDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE + " (" +
                COLUMN_SUBJECTNAME + " TEXT NOT NULL, "+
                COLUMN_HOUR + " INT, "+
                COLUMN_MINUTE + " INT, "+
                COLUMN_DAY + " TEXT"+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="DROP TABLE IF EXISTS " + TABLE_SCHEDULE;
        db.execSQL(query);
        onCreate(db);
    }

    //Method to save schedule when day ant time is selected
    public void addSchedule(String subjectName,int hour,int minute,String day){
        SQLiteDatabase db=getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(COLUMN_SUBJECTNAME,subjectName);
        values.put(COLUMN_HOUR,hour);
        values.put(COLUMN_MINUTE,minute);
        values.put(COLUMN_DAY, day);

        db.insert(TABLE_SCHEDULE, null, values);

        db.close();
    }

    //Method to return cursor to display all entries
    public Cursor todoCursor(){
        SQLiteDatabase db=getWritableDatabase();

        //Log.d("Akash","In todoCursor()");

        String query="SELECT rowid _id,* FROM "+TABLE_SCHEDULE + " ORDER BY " +
                COLUMN_SUBJECTNAME + ";";

        //Log.d("Akash","In todoCursor()");
        /*
            for CursorAdapter to work, cursor should contain _id column. In SQL there is always a row id associated with every row.
            so aliasing it as _id
         */

        return db.rawQuery(query,null);
    }

    //To delete subject from schedule when subject is deleted
    public void deleteSubjectFromSchedule(String subName){
        SQLiteDatabase db=getWritableDatabase();

        String query="DELETE FROM " + TABLE_SCHEDULE + " WHERE " +
                COLUMN_SUBJECTNAME + "=\"" + subName + "\";";

        db.execSQL(query);

        db.close();
    }

    //To update subject name in schedule database when subject name is edited
    public void updateSubjectName(String oldName,String newName){
        SQLiteDatabase db=getWritableDatabase();

        String query="UPDATE " + TABLE_SCHEDULE + " SET " +
                COLUMN_SUBJECTNAME + "=\"" + newName + "\"" + " WHERE " +
                COLUMN_SUBJECTNAME + "=\"" + oldName + "\";";

        db.execSQL(query);

        db.close();
    }

    //Method that returns cursor to List of subjects when day of week is given
    public Cursor subjectsListGivenDayCursor(String day){
        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT rowid AS _id,* FROM " + TABLE_SCHEDULE +
                " WHERE " + COLUMN_DAY + "=\"" + day + "\";";

        //db.close();

        return db.rawQuery(query,null);
    }

    //Method to remove all the entries in table, the table schema will not be affected
    public void deleteAllFromTable(){
        SQLiteDatabase db=getWritableDatabase();

        String query="DELETE FROM " + TABLE_SCHEDULE + ";";

        db.execSQL(query);

    }
}