package akashagarwal45.bunkeasy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="user.db";           //Database or file name
    public static final String TABLE_USERS="users";                 //Table name
    public static final String COLUMN_NAME="name";                  //column for names
    public static final String COLUMN_USERNAME="userName";          //column for usernames
    public static final String COLUMN_PASSWORD="passWord";          //column for passwords
    public static final String COLUMN_EMAIL="email";                //column for email


    public UserDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table
        String query="CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
            COLUMN_NAME + " TEXT NOT NULL, "+
            COLUMN_USERNAME + " TEXT NOT NULL, "+
            COLUMN_PASSWORD + " TEXT NULL, "+
            COLUMN_EMAIL + " TEXT"+
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to delete a table
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
    }

    //method to add users in a database
    public void addUser(Users user){

        //adding users details to content value object
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME,user.getName());
        values.put(COLUMN_USERNAME,user.getUserName());
        values.put(COLUMN_PASSWORD,user.getPassWord());
        values.put(COLUMN_EMAIL,user.getEmail());

        //obtain a writable database
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_USERS, null, values);

        //closing the database
        db.close();
    }

    public boolean authUser(String userId,String userPass){
        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT * FROM "+TABLE_USERS+";";

        Cursor c=db.rawQuery(query,null);

        c.moveToFirst();

        do {
            if(c.getString(c.getColumnIndex(COLUMN_USERNAME)).equals(userId)&&c.getString(c.getColumnIndex(COLUMN_PASSWORD)).equals(userPass)){
                c.close();
                db.close();
                return true;
            }
        }while(c.moveToNext());

        c.close();
        db.close();
        return false;
    }

    public boolean searchUser(String userId){
        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT * FROM "+TABLE_USERS+";";

        Cursor c=db.rawQuery(query,null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_USERNAME)).equals(userId)){
                c.close();
                db.close();
                return true;
            }
            else
                c.moveToNext();
        }

        c.close();
        db.close();
        return false;
    }

}