package akashagarwal45.bunkeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AttendanceDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME="attendance1.db";
    public static final String TABLE_ATTENDANCE="attendance";
    public static final String COLUMN_SUBNAME="subjectname";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_HOUR="hour";
    public static final String COLUMN_MINUTE="minute";
    public static final String COLUMN_RESPONSE="response";      //0->absent  1->present   2->holiday/cancelled

    public AttendanceDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + "(" +
                COLUMN_SUBNAME + " TEXT NOT NULL, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_HOUR + " TEXT, " +
                COLUMN_MINUTE + " TEXT, " +
                COLUMN_RESPONSE + " INT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ATTENDANCE+";");
        onCreate(db);
    }

    //Method to save subject attendance record date wise
    public void saveAttendanceRecord(String subName,String date,int response,String hour,String minute){

        SQLiteDatabase db=getWritableDatabase();

        Log.d("Akash","In saveAttendanceRecord()");

        if(isAttendanceMarked(subName,date,hour,minute)){       //Entry already in table so just update it
                 String query="UPDATE " + TABLE_ATTENDANCE + " SET " +
                         COLUMN_RESPONSE + "=" + response + " WHERE " +
                         COLUMN_SUBNAME + "=\"" + subName + "\" AND " +
                         COLUMN_HOUR + "=\"" + hour + "\" AND " +
                         COLUMN_MINUTE + "=\"" + minute + "\" AND " +
                         COLUMN_DATE + "=\"" + date + "\";";

                Log.d("Akash","Attendance already marked");

            db.execSQL(query);
        }

        else{                                       //Entry not in table so make a new entry
            ContentValues values=new ContentValues();

            Log.d("Akash",subName+" "+date+" "+response+" "+hour+" "+minute);

            values.put(COLUMN_SUBNAME,subName);
            values.put(COLUMN_DATE,date);
            values.put(COLUMN_RESPONSE,response);
            values.put(COLUMN_HOUR,hour);
            values.put(COLUMN_MINUTE,minute);
            long val=db.insert(TABLE_ATTENDANCE, null, values);

            Log.d("Akash",""+val);

            Log.d("Akash","Entry created");
        }

        db.close();
    }

    //Method to check whether attendance for given subject name and date is marked(inserted) or not
    public boolean isAttendanceMarked(String subName,String date,String hour,String minute){
        SQLiteDatabase db=getWritableDatabase();

        Log.d("Akash","In isAttendanceMarked(): "+subName+" "+date+" "+" "+hour+" "+minute);

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_SUBNAME + "=\"" + subName + "\" AND " +
                COLUMN_HOUR + "=\"" + hour + "\" AND " +
                COLUMN_MINUTE + "=\"" + minute + "\" AND " +
                COLUMN_DATE + "=\"" + date + "\";";

        Cursor c=db.rawQuery(query, null);

//        db.close();

        Log.d("Akash","From isAttendanceMarked(): "+c.getCount());

        if(c.getCount()<=0)
                return false;
        else
            return true;
    }

    //Method to return response for given subject and date
    public int getResponse(String subName,String date,String hour,String minute){
        SQLiteDatabase db=getReadableDatabase();

        Log.d("Akash","In getResponse(): "+subName+" "+date+" "+" "+hour+" "+minute);

        int response;

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_DATE + "=\"" + date + "\" AND " +
                COLUMN_HOUR + "=\"" + hour + "\" AND " +
                COLUMN_MINUTE + "=\"" + minute + "\" AND " +
                COLUMN_SUBNAME + "=\"" + subName + "\";";

        Cursor c = db.rawQuery(query, null);

        //c.moveToFirst();

        Log.d("Akash",""+"From getResponse(): "+c.getCount());

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

    //Method to deal with multiple classes of same subject on a date and color the calendar accordingly
    //Returns 0 is all absent, 1 if any absent and 2 if all present
    public int forMultipleClasses(String subname,String date)
    {
        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " +
                COLUMN_SUBNAME + "=\"" + subname + "\" AND " +
                COLUMN_DATE + "=" + date + ";";

        Cursor c=db.rawQuery(query,null);

        int absent=0,total,present=0;

        c.moveToFirst();

        while(!c.isAfterLast())
        {
            if(c.getInt(c.getColumnIndex(COLUMN_RESPONSE))==0)          //absent
                absent++;
            if(c.getInt(c.getColumnIndex(COLUMN_RESPONSE))==1)
                present++;
            c.moveToNext();
        }

        total=c.getCount();
        c.close();
        db.close();
        if(absent==0&&present!=0)               //all present
            return 2;
        else if(absent==total&&total!=0)      //all absent
                return 0;
        else if(absent>0)
            return 1;           //some absent and some present
        return -1;

    }
}