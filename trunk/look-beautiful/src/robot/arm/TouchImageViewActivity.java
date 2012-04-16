/**
 * 
 */
package robot.arm;

import robot.arm.provider.view.touch.TouchImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class TouchImageViewActivity extends Activity {
	private TouchImageView touchImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touch_image_content);

		touchImageView = (TouchImageView) findViewById(R.id.touch_image_view);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.m3);
		touchImageView.setImageBitmap(bm);
		touchImageView.reset();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ((TabInvHandler) getParent()).titleVisible(false);
		// ((TabInvHandler) getParent()).tabVisible(false);
		// ((TabInvHandler) getParent()).needCloseSoftInput(true);
	}
}
