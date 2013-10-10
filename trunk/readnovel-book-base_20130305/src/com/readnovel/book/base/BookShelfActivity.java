package com.readnovel.book.base;
import android.app.Activity;
import android.os.Bundle;

/*
 * 书架，write by wangwei
 *  2013 08 27 0:19
 *  书架流程：
 *    1.数据库书架表调入，显示封面
 *    2.gallery配置，需要进行
 *    
 *     
 *  
 *  
 */


public class BookShelfActivity extends Activity{
 
   @Override
   protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    // 初始化的时候需要进行相应的配置
	
   }
  
   @Override
	protected void onStart() {
		super.onStart();
	
   }
   
   @Override
	protected void onResume() {
		super.onResume();
	
   }
   
   @Override
	protected void onStop() {
		super.onStop();
   }
   
   @Override
	protected void onDestroy() {
		super.onDestroy();
   }

}
