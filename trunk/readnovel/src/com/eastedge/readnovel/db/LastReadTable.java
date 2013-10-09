package com.eastedge.readnovel.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.eastedge.readnovel.beans.RDBook;

public class LastReadTable extends BaseDBAccess{

	public static final String Table_lastread = "lastread"; // 数据表
	public static final String KEY_ID = "_id"; 				// 表ID
	public static final String KEY_articleid = "articleid"; // 书id
	public static final String KEY_bookName = "bookname"; 	// 书名
	public static final String KEY_bookfile = "bookfile"; 	// 书本地路径
	public static final String KEY_textId = "textid"; 		// 本章id
	public static final String KEY_isVip = "isvip"; 		// 是否vip
	public static final String KEY_posi = "posi"; 			// 阅读到位置
	public static final String KEY_time = "time"; 			// 时间
	public static final String KEY_finishFlag = "finishFlag"; // 是否完结
	public static final String KEY_isol = "isol"; 			// s是否在线阅读
	
	public static final String DB_CREATE=
	        "CREATE TABLE "+Table_lastread
	        +" ("+KEY_ID+" integer primary key autoincrement, "
	        +KEY_articleid+" text , "
	        +KEY_bookName+" text ,"
	        +KEY_bookfile+" text ,"
	        +KEY_textId+" text ,"
	        +KEY_isVip+" integer,"
	        +KEY_time+" integer ,"
	        +KEY_isol+" integer ,"
	        +KEY_finishFlag+" integer ,"
	        +KEY_posi+" integer);";
	
	public LastReadTable(Context context) {
		super(context);
	}
	
	
    /**
     * 最后次阅读记录
     */
    public long insertLastRead(RDBook rd){
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_articleid, rd.getArticleId());
        newValues.put(KEY_bookName, rd.getBookName());
        newValues.put(KEY_bookfile, rd.getBookFile());
        newValues.put(KEY_textId, rd.getTextId());
        newValues.put(KEY_isVip, rd.getIsVip());
        newValues.put(KEY_posi, rd.getPosi());
        newValues.put(KEY_time, System.currentTimeMillis());
        newValues.put(KEY_isol, rd.getIsOL());
        newValues.put(KEY_finishFlag, rd.getFinishflag());
        return db.insert(Table_lastread, null, newValues);
    }
	
    
	public long remove(String aid)
	{
		return db.delete(Table_lastread, KEY_articleid+"='"+aid+"' and isol=0", null);
	}
    
    
    public RDBook queryLastBook(String aid,int f){
    	  String cond="";  
//    	  if(f==1){
//    		 cond=" and isol=1";
//    	  }
//    	  if(f==2){
//    		  cond=" and (isol=0 or isvip=1)";
//    	  }
    	  if(aid!=null){
    		  aid=KEY_articleid+"='"+aid+"'"+cond;
    	  }
    	  Cursor result = db.query(Table_lastread, 
    			  new String[] {KEY_ID,KEY_articleid,KEY_bookName,KEY_bookfile,KEY_textId,KEY_isVip,KEY_posi,KEY_time,KEY_isol,KEY_finishFlag}, 
    			  aid, null, null, null, "time desc");
    	  
    	  return convertToBook(result);
    }

    private RDBook convertToBook(Cursor cursor){
    	 int resultCounts = cursor.getCount();
    	 if(resultCounts == 0 || !cursor.moveToFirst()){
            return null ;
         }
         cursor.moveToFirst();
         RDBook rd=new RDBook();
         rd.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
         rd.setArticleId(cursor.getString(cursor.getColumnIndex(KEY_articleid)));
         rd.setBookName(cursor.getString(cursor.getColumnIndex(KEY_bookName)));
         rd.setBookFile(cursor.getString(cursor.getColumnIndex(KEY_bookfile)));
         rd.setTextId(cursor.getString(cursor.getColumnIndex(KEY_textId)));
         rd.setIsVip(cursor.getInt(cursor.getColumnIndex(KEY_isVip)));
         rd.setPosi(cursor.getInt(cursor.getColumnIndex(KEY_posi)));
         rd.setIsOL(cursor.getInt(cursor.getColumnIndex(KEY_isol)));
         rd.setFinishflag(cursor.getInt(cursor.getColumnIndex(KEY_finishFlag)));
         cursor.close();
         return rd;
   }
    
    
}
