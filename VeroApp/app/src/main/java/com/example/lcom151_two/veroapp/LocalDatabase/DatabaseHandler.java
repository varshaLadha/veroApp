package com.example.lcom151_two.veroapp.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "faviouratePosts";
    private static final String TABLE_POSTS = "posts";
    private static final String KEY_ID = "id";
    private static final String KEY_POSTTEXT = "postText";
    private static final String KEY_POSTTIME = "postTime";
    private static final String KEY_POSTPIC = "postPic";
    private static final String KEY_USERPROFILE = "userProfile";
    private static final String KEY_DISPLAYNAME = "displayName";
    private static final String KEY_POSTID = "postId";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_LIKESCNT = "likescnt";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVIOURATEPOSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_POSTTEXT + " TEXT,"
                + KEY_POSTTIME + " TEXT,"
                +KEY_POSTPIC+" TEXT,"
                +KEY_USERPROFILE+" TEXT,"
                +KEY_DISPLAYNAME+" TEXT,"
                +KEY_POSTID+" INTEGER,"
                +KEY_COMMENTS+" INTEGER,"
                +KEY_LIKESCNT+" INTEGER" + ")";
        db.execSQL(CREATE_FAVIOURATEPOSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);

        onCreate(db);
    }

    public long addPost(FaviouratePosts posts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSTTEXT, posts.getPostText());
        values.put(KEY_POSTTIME, posts.getPostTime());
        values.put(KEY_POSTPIC,posts.getPostPic());
        values.put(KEY_USERPROFILE,posts.getUserProfile());
        values.put(KEY_DISPLAYNAME,posts.getDisplayName());
        values.put(KEY_POSTID,posts.getPostId());
        values.put(KEY_COMMENTS,posts.getComments());
        values.put(KEY_LIKESCNT,posts.getLikescnt());

        // Inserting Row
        long success=db.insert(TABLE_POSTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return success;
    }

    // code to get the single contact
    FaviouratePosts getPosts(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POSTS, new String[] { KEY_ID,
                        KEY_POSTTEXT, KEY_POSTTIME,KEY_POSTPIC,KEY_USERPROFILE,KEY_DISPLAYNAME,KEY_POSTID,KEY_COMMENTS,KEY_LIKESCNT }, KEY_POSTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FaviouratePosts posts = new FaviouratePosts(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),
                cursor.getString(4),cursor.getString(5),Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),Integer.parseInt(cursor.getString(8)));
        // return contact
        return posts;
    }

    // code to get all contacts in a list view
    public List<FaviouratePosts> getAllPosts() {
        List<FaviouratePosts> postsList = new ArrayList<FaviouratePosts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POSTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FaviouratePosts posts = new FaviouratePosts();
                posts.set_id(Integer.parseInt(cursor.getString(0)));
                posts.setPostText(cursor.getString(1));
                posts.setPostTime(cursor.getString(2));
                posts.setPostPic(cursor.getString(3));
                posts.setUserProfile(cursor.getString(4));
                posts.setDisplayName(cursor.getString(5));
                posts.setPostId(Integer.parseInt(cursor.getString(6)));
                posts.setComments(Integer.parseInt(cursor.getString(7)));
                posts.setLikescnt(Integer.parseInt(cursor.getString(8)));
                // Adding contact to list
                postsList.add(posts);
            } while (cursor.moveToNext());
        }
        // return contact list
        return postsList;
    }

    // Deleting single contact
    public long deletePost(FaviouratePosts posts) {
        SQLiteDatabase db = this.getWritableDatabase();
        long success=db.delete(TABLE_POSTS, KEY_POSTID + " = ?",
                new String[] { String.valueOf(posts.getPostId()) });
        db.close();
        return success;
    }

    // Getting contacts Count
    public int getPostsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_POSTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
