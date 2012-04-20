package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.common.bean.AlbumCover;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AlbumCoverAdapter extends BaseAdapter {
	private List<AlbumCover> list;

	public AlbumCoverAdapter(List<AlbumCover> albumCoverList) {
		init(albumCoverList);
	}

	public AlbumCoverAdapter(Activity act, List<String> imageUrlList) {
		List<AlbumCover> albumCoverList = AlbumCover.coverList(act, imageUrlList);

		init(albumCoverList);
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

		return list.get(position).coverRow();
	}

	public void addList(List<AlbumCover> covers) {
		List<AlbumCover> l = new ArrayList<AlbumCover>(covers.size() + list.size());
		l.addAll(list);
		l.addAll(covers);

		list = l;
	}

	public void addList(Activity act, List<String> imageUrlList) {
		List<AlbumCover> albumCoverList = AlbumCover.coverList(act, imageUrlList);

		addList(albumCoverList);
	}
}
