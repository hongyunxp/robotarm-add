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
import android.widget.TextView;

import com.mokoclient.core.bean.PostBean;

/**
 * @author li.li
 * 
 *         Apr 20, 2012
 * 
 */
public class AlbumCover {

	public static final int COUNT_PER_ROW = 2;
	private Activity act;
	private PostBean[] images;
	private List<ImageView> ivs;

	public AlbumCover(Activity act, PostBean[] images) {
		this.act = act;
		this.images = images;
	}

	public View coverRow() {
		// 实例化一行数据
		View row = LayoutInflater.from(act).inflate(R.layout.common_cover_list_row, null);
		ImageView iv1 = (ImageView) row.findViewById(R.id.image1);
		ImageView iv2 = (ImageView) row.findViewById(R.id.image2);

		TextView tv1 = (TextView) row.findViewById(R.id.text1);
		TextView tv2 = (TextView) row.findViewById(R.id.text2);

		// 设置专辑的明细url和专辑标题
		iv1.setTag(R.string.detailUrl, images[0].getDetailUrl());
		iv2.setTag(R.string.detailUrl, images[1].getDetailUrl());
		iv1.setTag(R.string.postTitle, images[0].getTitle());
		iv2.setTag(R.string.postTitle, images[1].getTitle());

		tv1.setVisibility(View.VISIBLE);
		tv2.setVisibility(View.VISIBLE);
		tv1.setText(images[0].getTitle());
		tv2.setText(images[1].getTitle());

		// 加载专辑图片
		LoadImageUtils.loadImageSync(act, images[0].getCoverUrl(), iv1);
		LoadImageUtils.loadImageSync(act, images[1].getCoverUrl(), iv2);

		ivs = new ArrayList<ImageView>(COUNT_PER_ROW);
		ivs.add(iv1);
		ivs.add(iv2);
		
		return row;
	}

	public static List<AlbumCover> coverList(Activity act, List<PostBean> images) {
		List<AlbumCover> list = new ArrayList<AlbumCover>(images.size() / COUNT_PER_ROW);

		PostBean[] row = null;

		for (int i = 0; i < images.size(); i++) {
			if (i % COUNT_PER_ROW == 0) {

				row = new PostBean[COUNT_PER_ROW];
				row[i % COUNT_PER_ROW] = images.get(i);

			} else if (i % COUNT_PER_ROW == 1) {

				row[i % COUNT_PER_ROW] = images.get(i);
				list.add(new AlbumCover(act, row));

			}
		}

		return list;
	}

	public Activity getAct() {
		return act;
	}

	@Override
	public String toString() {
		return act.getClass().getName();
	}

}
