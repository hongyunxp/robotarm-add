/**
 * 
 */
package robot.arm.common;

import java.util.ArrayList;
import java.util.List;

import robot.arm.common.bean.Album;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author li.li
 * 
 *         Apr 19, 2012
 * 
 */
public class AlbumAdapter extends BaseAdapter {
	private List<Album> albums;

	public AlbumAdapter(List<Album> albumCoverList) {
		init(albumCoverList);
	}

	public AlbumAdapter(Activity act, List<String> images) {
		List<Album> albumList = Album.coverList(act, images);
		init(albumList);
	}
	
	public AlbumAdapter() {
	}

	private void init(List<Album> albums) {
		this.albums = albums;
	}

	@Override
	public int getCount() {
		return albums.size();
	}

	@Override
	public Object getItem(int position) {
		return albums.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return albums.get(position).coverRow();
	}

	public void addList(List<Album> albumList) {
		List<Album> l = new ArrayList<Album>();

		if (albums != null)
			l.addAll(albums);
		if (albumList != null)
			l.addAll(albumList);

		albums = l;
		
	}
	
	public void addList(Activity act, List<String> imageUrlList) {
		List<Album> albumCoverList = Album.coverList(act, imageUrlList);

		addList(albumCoverList);
	}

}
