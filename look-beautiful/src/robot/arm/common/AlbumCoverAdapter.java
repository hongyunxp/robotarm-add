package robot.arm.common;

import java.util.List;

import robot.arm.common.bean.AlbumCover;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AlbumCoverAdapter extends BaseAdapter {
	private List<AlbumCover> list;

	public AlbumCoverAdapter(List<AlbumCover> albumCoverList) {
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

}
