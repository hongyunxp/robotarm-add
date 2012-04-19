package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.R;
import robot.arm.utils.BaseUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AlbumCoverAdapter extends BaseAdapter {
	private List<String> list;

	private Activity act;

	public AlbumCoverAdapter(Activity act, List<String> list) {
		this.act = act;
		this.list = list;
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
		View row = LayoutInflater.from(act).inflate(R.layout.album_cover_list_row, null);
		ImageView iv = (ImageView) row.findViewById(R.id.image1);

		BaseUtils.loadImageSync(act, list.get(position), iv);

		return row;
	}

	public void addList(List<String> list) {
		List<String> l = new ArrayList<String>(this.list.size() + list.size());
		l.addAll(this.list);
		l.addAll(list);

		this.list = l;
	}
}
