/**
 * 
 */
package com.bus3.test;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bus3.R;

/**
 * @author li.li
 * 
 *         Mar 21, 2012
 * 
 */
public class DistrictArrayAdapter extends BaseAdapter {
	private List<District> list;

	private Activity act;

	public DistrictArrayAdapter(Activity act, List<District> list) {
		this.act = act;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public District getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		District district = list.get(position);

		View item = LayoutInflater.from(act).inflate(R.layout.item, parent, false);
		TextView tv = (TextView) item.findViewById(R.id.textViewId);
		tv.setText(district.getName());

		return item;
	}

}
