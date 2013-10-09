package com.eastedge.readnovel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserBookTable extends BaseDBAccess{

	public static final String Table_tbName = "hdbook";     // 同步过的书
	public static final String KEY_ID = "_id"; 				// 表ID
	public static final String KEY_articleid = "articleid"; // 书id
	public static final String KEY_uid = "uid"; 			// 用户id
	public static final String KEY_isVip = "isVip"; 		// 是否vip
	

	public static final String DB_CREATE=
	        "CREATE TABLE "+Table_tbName
	        +" ("+KEY_ID+" integer primary key autoincrement, "
	        +KEY_articleid+" text , "
	        +KEY_isVip+" integer ,"
	        +KEY_uid+" text);";
	
	
	public UserBookTable(Context context) {
		super(context);
	}
	public UserBookTable(Context context,SQLiteDatabase db) {
		super(context);
		super.db=db;
	}

    public long insert(String aid,String uid,int isVip){
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_articleid, aid);
        newValues.put(KEY_uid, uid);
        newValues.put(KEY_isVip,isVip);
        return db.insert(Table_tbName, null, newValues);
    }
	
    public boolean exitBook(String aid,String uid,int isVip)
	{
		Cursor cursor=db.query(Table_tbName, new String[]{KEY_ID}, "articleid='"+aid+"' and uid='"+uid+"' and isvip="+isVip, null, null, null, null);
		if(cursor==null)return false;
		
		boolean fla=cursor.moveToFirst();
		if(cursor!=null&&!cursor.isClosed()){
			cursor.close();
		}
		return fla;
	}
    
}
