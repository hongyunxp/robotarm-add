/**
 * 
 */
package robot.arm.common.bean;

import robot.arm.R;
import robot.arm.utils.LoadImageUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author li.li
 * 
 *         Apr 20, 2012
 * 
 */
public class AlbumCover {
	Activity act;
	String[] images;

	public AlbumCover(Activity act, String[] images) {
		this.act = act;
		this.images = images;
	}

	public View coverListRow() {
		View row = LayoutInflater.from(act).inflate(R.layout.album_cover_list_row, null);
		ImageView iv1 = (ImageView) row.findViewById(R.id.image1);
		ImageView iv2 = (ImageView) row.findViewById(R.id.image2);

		LoadImageUtils.loadImageSync(act, images[0], iv1);
		LoadImageUtils.loadImageSync(act, images[1], iv2);

		return row;
	}
}
