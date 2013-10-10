package com.readnovel.book.base.adapter;

import java.util.ArrayList;

import com.readnovel.book.base.R;
import com.readnovel.book.base.bean.MoreDanBen;
import com.readnovel.book.base.common.Constants;
import com.readnovel.book.base.http.LoadImgProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreAdapter2 extends BaseAdapter {
	private final Context context;
	private ArrayList<MoreDanBen> listmore = new ArrayList<MoreDanBen>();// 存放更多对象的集合

	public MoreAdapter2(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return listmore.size();
	}

	@Override
	public Object getItem(int position) {
		return listmore.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewCache viewCache;
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.more_list2, null);
			rowView.setMinimumHeight(100);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ImageView imageView = viewCache.getImageLogoView();
		TextView title_tv = viewCache.getTitleTextView();
		TextView downnum_tv = viewCache.getDownNumTextView();
		TextView type_tv = viewCache.getTypeTextView();
		TextView author_tv = viewCache.getAuthorTextView();
		Button downloadBtn = viewCache.getDownLoadBtn();
		downloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Uri uri = Uri.parse(listmore.get(position).getAppurl());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
			}
		});
		title_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		title_tv.setText("《" + listmore.get(position).getBookTitle() + "》");
		downnum_tv.setText(listmore.get(position).getDownLoadNum());
		type_tv.setText("类型：" + listmore.get(position).getType() + "|");
		author_tv.setText("作者：" + listmore.get(position).getAuthor());
		String imageUrl = listmore.get(position).getBookLogo();
		LoadImgProvider.getInstance(Constants.READNOVEL_IMGCACHE).load(imageUrl, imageView);
		return rowView;
	}

	public void add(ArrayList<MoreDanBen> moreList) {
		ArrayList<MoreDanBen> l = new ArrayList<MoreDanBen>();
		if (listmore != null)
			l.addAll(listmore);
		if (moreList != null)
			l.addAll(moreList);
		listmore = l;
	}

	class ViewCache {
		private View baseView;
		private TextView titletextView;
		private TextView typetextView;
		private ImageView imageView;
		private TextView downNumtextView;
		private TextView authortextView;
		private Button downloadBtn;
		public ViewCache(View baseView) {
			this.baseView = baseView;
		}

		public TextView getTitleTextView() {
			if (titletextView == null) {
				titletextView = (TextView) baseView.findViewById(R.id.more_book_title);
			}
			return titletextView;
		}

		public TextView getDownNumTextView() {
			if (downNumtextView == null) {
				downNumtextView = (TextView) baseView.findViewById(R.id.more_download_num);
			}
			return downNumtextView;
		}

		public TextView getTypeTextView() {
			if (typetextView == null) {
				typetextView = (TextView) baseView.findViewById(R.id.more_type);
			}
			return typetextView;
		}

		public TextView getAuthorTextView() {
			if (authortextView == null) {
				authortextView = (TextView) baseView.findViewById(R.id.more_author);
			}
			return authortextView;
		}

		public ImageView getImageLogoView() {
			if (imageView == null) {
				imageView = (ImageView) baseView.findViewById(R.id.more_heji_pic);
			}
			return imageView;
		}

		public Button getDownLoadBtn() {
			if (downloadBtn == null) {
				downloadBtn = (Button) baseView.findViewById(R.id.more_download_btn);
			}
			return downloadBtn;
		}
	}
}
