/**
 * 
 */
package robot.arm.common;

import java.util.Arrays;
import java.util.List;

import robot.arm.R;
import robot.arm.TouchImageViewActivity;
import robot.arm.core.TabInvHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class BaseActivity extends Activity {
	protected static List<Integer> list = Arrays.asList(R.layout.album_cover_row, R.layout.album_cover_row, R.layout.album_cover_row,
			R.layout.album_cover_row);
	
	protected static List<Integer> list2 = Arrays.asList(R.layout.album_content_row, R.layout.album_content_row, R.layout.album_content_row,
			R.layout.album_content_row);
	
	protected static List<Integer> album;
	
	protected TabInvHandler tabInvHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabInvHandler = ((TabInvHandler) getParent());
	}

	public void details(View view) {
		Intent intent = new Intent(this, TouchImageViewActivity.class);
		startActivity(intent);
	}

	public void background(int resId) {
		TableLayout tl = (TableLayout) findViewById(R.id.images_content);
		tl.setBackgroundResource(resId);
	}
	
	public void title(int resId){
		tabInvHandler.setTitle(resId);
	}

}
