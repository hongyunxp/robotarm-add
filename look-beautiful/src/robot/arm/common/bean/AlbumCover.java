/**
 * 
 */
package robot.arm.common.bean;

import java.util.ArrayList;
import java.util.List;

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
	public static final int COUNT_PER_ROW = 2;
	private Activity act;
	private String[] images;
	private List<ImageView> ivs;

	public AlbumCover(Activity act, String[] images) {
		this.act = act;
		this.images = images;
	}

	public View coverRow() {
		View row = LayoutInflater.from(act).inflate(R.layout.album_cover_list_row, null);
		ImageView iv1 = (ImageView) row.findViewById(R.id.image1);
		ImageView iv2 = (ImageView) row.findViewById(R.id.image2);

		LoadImageUtils.loadImageSync(act, images[0], iv1);
		LoadImageUtils.loadImageSync(act, images[1], iv2);
		
		ivs=new ArrayList<ImageView>(COUNT_PER_ROW);
		ivs.add(iv1);
		ivs.add(iv2);

		return row;
	}

	public static List<AlbumCover> coverList(Activity act,List<String> images) {
		List<AlbumCover> list = new ArrayList<AlbumCover>(images.size() / COUNT_PER_ROW);

		String[] row = null;

		for (int i = 0; i < images.size(); i++) {
			if (i % COUNT_PER_ROW == 0) {
				
				row = new String[COUNT_PER_ROW];
				row[i % COUNT_PER_ROW] = images.get(i);
				
			} else if (i % COUNT_PER_ROW == 1) {
				
				row[i % COUNT_PER_ROW] = images.get(i);
				list.add(new AlbumCover(act, row));
				
			}
		}

		return list;
	}
	
}
