package akashagarwal45.bunkeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="user.db";
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
        values.put(COLUMN_DAY,day);

        db.insert(TABLE_SCHEDULE,null,values);

    }
}