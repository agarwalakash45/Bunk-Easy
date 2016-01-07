package akashagarwal45.bunkeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SubjectDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=5;
    private static final String DATABASE_NAME="subject.db";
    public static final String TABLE_SUBJECTS="subjects";
    public static final String COLUMN_SUBNAME="subname";
    public static final String COLUMN_MISS="missed";
    public static final String COLUMN_TOTAL="total";

    public SubjectDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + "(" +
                COLUMN_SUBNAME + " TEXT NOT NULL, " +
                COLUMN_MISS + " INT, " +
                COLUMN_TOTAL + " INT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        onCreate(db);
    }

    public void addSubject(Subjects subject){

        ContentValues values=new ContentValues();
        values.put(COLUMN_SUBNAME,subject.getSubName());
        values.put(COLUMN_MISS,subject.getMissClass());
        values.put(COLUMN_TOTAL, subject.getTotClass());

        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_SUBJECTS, null, values);
        db.close();
    }

    public Cursor todoCursor(){
        SQLiteDatabase db=getWritableDatabase();

        String query="SELECT rowid _id,* FROM "+TABLE_SUBJECTS+";";
        /*
            for CursorAdapter to work, cursor should contain _id column. In SQL there is always a row id associated with every row.
         */

        return db.rawQuery(query, null);

    }

    //method to update subject name
    public void editSubjectName(String newSubName,String oldSubName){
        SQLiteDatabase db=getWritableDatabase();

        String query="UPDATE "+TABLE_SUBJECTS+" SET "+
                COLUMN_SUBNAME + "=\"" + newSubName +
                "\" WHERE " +
                COLUMN_SUBNAME + "=\"" + oldSubName + "\";";

        db.execSQL(query);

        db.close();
    }

    //method to delete subject
    public void deleteSubject(String subName){
        SQLiteDatabase db=getWritableDatabase();

        String query="DELETE FROM " + TABLE_SUBJECTS +
                " WHERE " +
                COLUMN_SUBNAME + "=\"" + subName + "\";";

        db.execSQL(query);

        db.close();
    }
}