package akashagarwal45.bunkeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendanceDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="attendance.db";
    public static final String TABLE_ATTENDANCE="attendance";
    public static final String COLUMN_SUBNAME="subjectname";
    public static final String COLUMN_DATE="date";
    //public static final String COLUMN_HOUR="hour";
    //public static final String COLUMN_MINUTE="minute";
    public static final String COLUMN_RESPONSE="response";      //0->absent  1->preent   2->holiday/cancelled

    public AttendanceDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + "(" +
                COLUMN_SUBNAME + " TEXT NOT NULL, " +
                COLUMN_DATE + " DATE, " +
     //           COLUMN_HOUR + " INT, " +
       //         COLUMN_MINUTE + " INT, " +
                COLUMN_RESPONSE + " INT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ATTENDANCE+";");
        onCreate(db);
    }

    //Method to save subject attendance record date wise
    public void saveAttendanceRecord(String subName,String date,int response){
        SQLiteDatabase db=getWritableDatabase();

        if(isAttendanceMarked(subName,date)){       //Entry already in table so just update it
                 String query="UPDATE " + TABLE_ATTENDANCE + " SET " +
                         COLUMN_RESPONSE + "=" + response + " WHERE " +
                         COLUMN_SUBNAME + "=\"" + subName + "\" AND " +
                         COLUMN_DATE + "=" + date + ";";

            db.execSQL(query);
        }

        else{                                       //Entry not in table so make a new entry
            ContentValues values=new ContentValues();

            values.put(COLUMN_SUBNAME,subName);
            values.put(COLUMN_DATE,date);
            values.put(COLUMN_RESPONSE,response);

            db.insert(TABLE_ATTENDANCE, null, values);

        }

        db.close();
    }

    //Method to check whether attendance for given subject name and date is marked(inserted) o not
    public boolean isAttendanceMarked(String subName,String date){
        SQLiteDatabase db=getWritableDatabase();

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_SUBNAME + "=\"" + subName + "\" AND " +
                COLUMN_DATE + "=" + date + ";";

        Cursor c=db.rawQuery(query, null);

//        db.close();

        if(c.getCount()<=0)
                return false;
        else
            return true;
    }

    //Method to return response for given subject and date
    public int getResponse(String subName,String date){
        SQLiteDatabase db=getReadableDatabase();

        int response;

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_DATE + "=" + date + " AND " +
                COLUMN_SUBNAME + "=\"" + subName + "\";";

        Cursor c = db.rawQuery(query, null);

        if(c.getCount()<=0)
            response=-1;
        else {
            c.moveToFirst();
            response = c.getInt(c.getColumnIndex(COLUMN_RESPONSE));
        }
        c.close();

        return response;
    }

    //Method to delete subject from table
    public void deleteSubject(String subName){
        SQLiteDatabase db=getWritableDatabase();

        String query="DELETE FROM " + TABLE_ATTENDANCE +
                " WHERE " +
                COLUMN_SUBNAME + "=\"" + subName + "\";";

        db.execSQL(query);

        db.close();
    }

    //To update subject name in schedule database when subject name is edited
    public void updateSubjectName(String oldName,String newName){
        SQLiteDatabase db=getWritableDatabase();

        String query="UPDATE " + TABLE_ATTENDANCE + " SET " +
                COLUMN_SUBNAME + "=\"" + newName + "\"" + " WHERE " +
                COLUMN_SUBNAME + "=\"" + oldName + "\";";

        db.execSQL(query);

        db.close();
    }

    //To return cursor to provide dates to show attendance when a subject is opened
    public Cursor showAttendanceonDate(String subName){
        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_SUBNAME + "=\"" + subName + "\";";

        return db.rawQuery(query,null);
    }

    //Method to remove all the entries in table, the table schema will not be affected
    public void deleteAllFromTable(){
        SQLiteDatabase db=getWritableDatabase();

        String query="DELETE FROM " + TABLE_ATTENDANCE + ";";

        db.execSQL(query);

    }
}
