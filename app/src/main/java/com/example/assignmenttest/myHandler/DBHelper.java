package com.example.assignmenttest.myHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.assignmenttest.Parameters.PostParams;
import com.example.assignmenttest.Parameters.RegisterParams;
import com.example.assignmenttest.model.PostModel;
import com.example.assignmenttest.model.UserModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, RegisterParams.DB_NAME, null, RegisterParams.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        // to read return data use cursor but in this case execSQL is to write table
        // use execSQL to write or create
        String create = "CREATE TABLE " + RegisterParams.TABLE_NAME + "(" + RegisterParams.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RegisterParams.KEY_NAME + " TEXT," + RegisterParams.KEY_PASSWORD + " TEXT,"
                + RegisterParams.KEY_EMAIL + " TEXT," + RegisterParams.KEY_DATE_REGISTERED + " TEXT," + RegisterParams.KEY_DATE_LAST_UPDATED + " TEXT " + ")";
        db.execSQL(create);

        String ActiveUser = "CREATE TABLE active_user"  + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "user_name TEXT" + ")";
        db.execSQL(ActiveUser);

        String createPost = "CREATE TABLE " + PostParams.TABLE_NAME + "(" + PostParams.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PostParams.KEY_USERNAME + " TEXT," + PostParams.KEY_NAME + " TEXT," + PostParams.KEY_DATE + " TEXT," + PostParams.KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createPost);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + RegisterParams.TABLE_NAME + "'");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + PostParams.TABLE_NAME + "'");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + "active_user" + "'");

        onCreate(sqLiteDatabase);

        // create new version of table on upgrade/change is schemas
    }

    // creating different methods for add, update and delete

    public Boolean addUser(String userName, String password, String email, String dateRegistered, String dateUpdated){
        // for changes in database use writableDatabase
        SQLiteDatabase db = this.getWritableDatabase();
        //contentValues
        ContentValues contentValues = new ContentValues();
        // Note: it is important to put not null value
        contentValues.put(RegisterParams.KEY_NAME, userName);
        contentValues.put(RegisterParams.KEY_PASSWORD, password);
        contentValues.put(RegisterParams.KEY_EMAIL, email);
        contentValues.put(RegisterParams.KEY_DATE_REGISTERED, dateRegistered);
        contentValues.put(RegisterParams.KEY_DATE_LAST_UPDATED, dateUpdated);
        //pass table_name,
        long result = db.insert(RegisterParams.TABLE_NAME,null,contentValues);
        if (result==-1){
            return false;
        }
        else {
            return true;
        }

    }

    public Boolean checkusername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RegisterParams.TABLE_NAME + " WHERE userName = ?", new String[]{username});
        if (cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkUserNamePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RegisterParams.TABLE_NAME + " WHERE userName = ? and password = ?", new String[]{username,password});
        if (cursor.getCount() > 0){
           setActiveUser(username);
            return true;
        }
        else {
            return false;
        }
    }

    public void setActiveUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("user_name", username);
        db.insert("active_user", null, contentValues1);
    }
    public String getActiveUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from active_user", null); // selecting all value table
        String user = "";
        if(cursor.moveToNext()){
        user = cursor.getString(1);
        }
        return user;
    }
    public Cursor getActiveUserDetails(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + RegisterParams.TABLE_NAME + " WHERE userName = ?", new String[]{username});
        return cursor1;
    }

    public void deleteActiveUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        // using where clause and where argument
        // checking multiple parameter by using where clause (?)
        db.execSQL("DELETE FROM active_user");
    }
    //for to update profile of user
    public void updateUserProfile(String userName, String name, String email, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(RegisterParams.KEY_NAME, name);
        updateValues.put(RegisterParams.KEY_EMAIL, email);
        updateValues.put(RegisterParams.KEY_DATE_LAST_UPDATED, date);
        db.update(RegisterParams.TABLE_NAME, updateValues, RegisterParams.KEY_NAME + " =? " , new String[]{userName});
    }

    // for post
    public Boolean insertUserData(String username, String name, String date, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PostParams.KEY_USERNAME, username);
        contentValues.put(PostParams.KEY_NAME, name);
        contentValues.put(PostParams.KEY_DATE, date);
        contentValues.put(PostParams.KEY_DESCRIPTION, description);
        // to check result if inserted or not
        long result = db.insert(PostParams.TABLE_NAME, null, contentValues);
        if (result==-1){
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<PostModel> fetchPost(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + PostParams.TABLE_NAME + " ORDER BY " + PostParams.KEY_DATE + " DESC", null); // selecting all value table
        ArrayList<PostModel> arraysOfPost = new ArrayList<>();
        while (cursor.moveToNext()){
            PostModel model = new PostModel();
            model.id = cursor.getInt(0);
            model.username = cursor.getString(1);
            model.title = cursor.getString(2);
            model.date = cursor.getString(3);
            model.description = cursor.getString(4);
            arraysOfPost.add(model);
        }
        return arraysOfPost;
    }

    // for update post
    public void updateUserPost(PostModel postModel, String name, String description, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PostParams.KEY_NAME, name);
        contentValues.put(PostParams.KEY_DATE, date);
        contentValues.put(PostParams.KEY_DESCRIPTION, description);
        db.update(PostParams.TABLE_NAME, contentValues, PostParams.KEY_USERNAME + " =? " + " AND " + PostParams.KEY_DATE + " =? ", new String[]{postModel.username, postModel.date});
    }

    public void deleteUserPost(PostModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        // using where clause and where argument
        // checking multiple parameter by using where clause (?)
        db.delete(PostParams.TABLE_NAME,PostParams.KEY_USERNAME + " =? " + " AND " + PostParams.KEY_DATE + " =? ", new String[]{model.username,model.date});
    }
// To show list of user registerd in this app in admin view

    public ArrayList<UserModel> fetchUser(){
        // fetching row of table by use of cursor
        SQLiteDatabase db = this.getReadableDatabase();
        // to read data using query
        Cursor cursor = db.rawQuery("SELECT * FROM " + RegisterParams.TABLE_NAME, null);
        // we need to move cursor upto last of list

        // fetching row and storing it in structure and showing
        ArrayList<UserModel> postArrays = new ArrayList<>();
        while (cursor.moveToNext()){
            //creating new structure to store in array list
            UserModel model = new UserModel();
            // giving index of id
            // getting
            model.id = cursor.getInt(0);// 0 for id, this index for column index
            model.userName = cursor.getString(1);// return string of column 1 of that row
            model.email = cursor.getString(3);
            model.date_registered = cursor.getString(4);
            postArrays.add(model);
        }
        return postArrays;
    }

	//Update user by admin

    // example: to update title we use id and title so we need two datatype, so use structure class userModel which has both datatype
    public void updateUser(UserModel userModel, String name, String email, String date){
        // if updating single record use primary key
        // if updating multiple record use non primary key
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(RegisterParams.KEY_NAME, name);
        updateValues.put(RegisterParams.KEY_EMAIL, email);
        updateValues.put(RegisterParams.KEY_DATE_REGISTERED, date);
        // to update by use of put
        // using where clause
        //updating by use of id
        // where key_id =
        db.update(RegisterParams.TABLE_NAME, updateValues, RegisterParams.KEY_ID + " =? " , new String[]{String.valueOf(userModel.id)});
    }

// delete user by admin

    public void deleteUser(UserModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        // using where clause and where argument
        // checking multiple parameter by using where clause (?)
        db.delete(PostParams.TABLE_NAME, PostParams.KEY_USERNAME + " =? ", new String[]{model.userName});
        db.delete(RegisterParams.TABLE_NAME, RegisterParams.KEY_ID + " =? ", new String[]{String.valueOf(model.id)});
    }
}
