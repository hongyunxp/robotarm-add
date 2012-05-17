package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.common.bean.AlbumCover;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mokoclient.core.bean.PostBean;

public class AlbumCoverAdapter extends BaseAdapter {
	private List<AlbumCover> list;

	public AlbumCoverAdapter(List<AlbumCover> albumCoverList) {
		init(albumCoverList);
	}

	public AlbumCoverAdapter(Activity act, List<PostBean> imageUrlList) {
		List<AlbumCover> albumCoverList = AlbumCover.coverList(act, imageUrlList);

		init(albumCoverList);
	}

	public AlbumCoverAdapter() {
	}

	private void init(List<AlbumCover> albumCoverList) {
		this.list = albumCoverList;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+position+"|"+(convertView==null));
		final AlbumCover item = list.get(position);

		View row = null;

		if (row == null) {
			row = item.coverRow();
		}

		return row;
	}

	public void addList(List<AlbumCover> covers) {
		List<AlbumCover> l = new ArrayList<AlbumCover>();

		if (list != null)
			l.addAll(list);
		if (covers != null)
			l.addAll(covers);

		list = l;
	}

	public void addList(Activity act, List<PostBean> imageUrlList) {
		List<AlbumCover> albumCoverList = AlbumCover.coverList(act, imageUrlList);

		addList(albumCoverList);
	}

}
