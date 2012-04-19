package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AlbumArtAdapter extends BaseAdapter {
	private List<Integer> list;

	private Activity act;

	public AlbumArtAdapter(Activity act, List<Integer> list) {
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
		View row = LayoutInflater.from(act).inflate(list.get(position), null);

		return row;
	}

	public void addList(List<Integer> list){
		List<Integer> l=new ArrayList<Integer>(this.list.size()+list.size());
		l.addAll(this.list);
		l.addAll(list);
		
		this.list=l;
	}
}
