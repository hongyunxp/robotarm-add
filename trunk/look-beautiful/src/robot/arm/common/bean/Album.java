/**
 * 
 */
package robot.arm.common.bean;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.common.LBConstants;
import robot.arm.provider.http.LoadImgProvider;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author li.li
 * 
 *         Apr 24, 2012
 * 
 */
public class Album {
	public static final int COUNT_PER_ROW = 1;
	private Activity act;
	private String[] images;

	public Album(Activity act, String[] images) {
		this.act = act;
		this.images = images;
	}

	public View coverRow() {
		View row = LayoutInflater.from(act).inflate(R.layout.common_content_list_row, null);
		ImageView image = (ImageView) row.findViewById(R.id.contentImage);
		image.setTag(R.string.detailUrl, images[0]);

		LoadImgProvider.getInstance(LBConstants.READNOVEL_IMGCACHE_ABS).load(images[0], image);

		return row;
	}

	public static List<Album> coverList(Activity act, List<String> images) {
		List<Album> list = new ArrayList<Album>(images.size() / COUNT_PER_ROW);

		String[] row = null;

		for (int i = 0; i < images.size(); i++) {

			row = new String[COUNT_PER_ROW];
			row[0] = images.get(i);
			list.add(new Album(act, row));

		}

		return list;
	}
}
