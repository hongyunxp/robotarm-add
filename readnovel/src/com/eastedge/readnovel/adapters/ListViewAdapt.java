package com.eastedge.readnovel.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.beans.BFBook;
import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.common.HCData;
import com.eastedge.readnovel.common.Util;
import com.readnovel.base.util.LogUtils;
import com.xs.cn.R;
import com.xs.cn.http.DownFile;

/**
 * 书架ListView的Adapter
 * 
 * @author li.li
 *
 * Mar 21, 2013
 */
public class ListViewAdapt extends BaseAdapter {
	public Context context;
	public Vector<BFBook> vlist;
	public Handler handler;
	public static boolean isMove;

	public ListViewAdapt(Handler handler, Context context, Vector<BFBook> vlist) {
		this.handler = handler;
		this.context = context;
		this.vlist = vlist;
	}

	public int getCount() {
		return (vlist.size() - 1) / 9 + 1;
	}

	public Object getItem(int position) {
		return vlist.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		GalleryListItem item = null;
		if (convertView == null) {
			item = new GalleryListItem();
			View v = LayoutInflater.from(context).inflate(R.layout.bookshelf_row, null);
			doset(item, v);
			convertView = v;
			convertView.setTag(item);
		} else {
			item = (GalleryListItem) convertView.getTag();
		}

		int begin = position * 3;
		for (int i = 0; i < 3; i++) {
			if (vlist.size() <= begin + i) {
				item.txlist.get(i).setVisibility(View.GONE);
				item.dlist.get(i).setVisibility(View.GONE);
				item.yylist.get(i).setVisibility(View.GONE);
				item.yylist2.get(i).setVisibility(View.GONE);
				item.ulist.get(i).setVisibility(View.GONE);
			} else {
				final int p = begin + i;
				final BFBook book = vlist.get(p);
				if (HCData.downingBook.containsKey(book.getArticleid())) {
					book.setIsonDown(View.VISIBLE);
					DownFile f = HCData.downingBook.get(book.getArticleid());
					ProgressBar pro = item.prolist.get(i);
					pro.setMax(f.fileLen);
					pro.setProgress(f.downLen);
				} else {
					book.setIsonDown(View.GONE);
				}
				if (HCData.downOK.containsKey(book.getArticleid())) {
					book.setBookFile(HCData.downOK.remove(book.getArticleid()));
				}
				item.ulist.get(i).setVisibility(View.GONE);
				if (book.getIsUp() == 1) {
					item.ulist.get(i).setVisibility(View.VISIBLE);
				}
				item.btnlist.get(i).setVisibility(View.VISIBLE);
				item.txlist.get(i).setVisibility(View.VISIBLE);
				if (book.getBookDrawable() != null) {
					item.btnlist.get(i).setBackgroundDrawable(book.getBookDrawable());
				} else if (book.getImgFile() != null && !"".equals(book.getImgFile()) && new File(book.getImgFile()).exists()) {
					try {
						Drawable d = Drawable.createFromStream(context.getContentResolver()
								.openInputStream(Uri.fromFile(new File(book.getImgFile()))), null);
						book.setBookDrawable(d);
						item.btnlist.get(i).setBackgroundDrawable(d);
					} catch (FileNotFoundException e) {
						item.btnlist.get(i).setBackgroundResource(R.drawable.imgtest);
						LogUtils.error(e.getMessage(), e);
					}
				} else {
					item.btnlist.get(i).setBackgroundResource(R.drawable.imgtest);
				}
				item.txlist.get(i).setText("《" + Util.slStr(book.getTitle()) + "》");
				int vis = book.getIsdelete();
				item.dlist.get(i).setVisibility(vis);
				item.yylist2.get(i).setVisibility(book.getIsonDown());
				item.yylist.get(i).setVisibility(View.VISIBLE);
				item.btnlist.get(i).setOnLongClickListener(new Button.OnLongClickListener() {

					public boolean onLongClick(View v) {
						if (book.getIsdelete() != View.VISIBLE && !isMove) {
							Message msg = new Message();
							msg.what = 13;
							int[] location = new int[2];
							v.getLocationOnScreen(location);
							msg.arg1 = location[0];
							msg.arg2 = location[1];
							msg.obj = p;
							handler.sendMessage(msg);
						}
						return false;
					}
				});

				item.btnlist.get(i).setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						//点击删除
						if (book.getIsdelete() == View.VISIBLE) {
							Message msg = new Message();
							msg.arg1 = p;
							msg.what = 12;
							handler.sendMessage(msg);
							return;
						} else if (book.getBookFile() == null || "".equals(book.getBookFile())) {

							if (HCData.downOK.containsKey(book.getArticleid())) {
								book.setBookFile(HCData.downOK.remove(book.getArticleid()));
							} else {
								//点击下载
								downBook(p);
								return;
							}

						}

						/**
						 * 检查书的数据是否完整， 不完整重新下(是否有当前书的目录,没有重新下)
						 */
						//不存在书目录
						Shubenmulu mulu = Util.read(book.getArticleid());
						if (mulu == null || mulu.getMulist() == null || mulu.getMulist().size() <= 0) {
							//重新下载书
							downBook(p);
							return;
						}
						//不存在书数据
						File bookFile = Util.readBook(book.getArticleid());
						if (bookFile == null || !bookFile.exists()) {
							//重新下载书
							downBook(p);
							return;
						}

						//点击更新
						if (book.getIsUp() == 1) {
							Message msg = new Message();
							msg.arg1 = p;
							msg.what = 334;
							handler.sendMessage(msg);
							return;
						}

						//阅读书
						if (book.getIsdelete() != View.VISIBLE) {
							Message msg = new Message();
							msg.arg1 = p;
							msg.what = 333;
							handler.sendMessage(msg);
							return;
						}
					}
				});

				item.yylist2.get(i).setOnClickListener(new RelativeLayout.OnClickListener() {
					public void onClick(View v) {

					}
				});
			}
		}

		return convertView;

	}

	/**
	 * 下载书
	 * @param p
	 */
	private void downBook(int p) {
		//重新下载书
		Message msg = new Message();
		msg.arg1 = p;
		msg.what = 15;
		handler.sendMessage(msg);
	}

	private static final class GalleryListItem {

		public ArrayList<TextView> txlist = new ArrayList<TextView>();
		public ArrayList<Button> btnlist = new ArrayList<Button>();
		public ArrayList<ImageView> dlist = new ArrayList<ImageView>();
		public ArrayList<ImageView> ulist = new ArrayList<ImageView>();
		public ArrayList<RelativeLayout> yylist = new ArrayList<RelativeLayout>();
		public ArrayList<RelativeLayout> yylist2 = new ArrayList<RelativeLayout>();
		public ArrayList<ProgressBar> prolist = new ArrayList<ProgressBar>();

		public TextView tx1, tx2, tx3;
		public ProgressBar pro1, pro2, pro3;
		public Button btn1, btn2, btn3;
		public ImageView d1, d2, d3;
		public ImageView u1, u2, u3;
		public RelativeLayout yly1, yly2, yly3;
		public RelativeLayout yy1, yy2, yy3;

	}

	private void doset(GalleryListItem item, View v) {
		item.tx1 = (TextView) v.findViewById(R.id.bfitem_t1);
		item.tx2 = (TextView) v.findViewById(R.id.bfitem_t2);
		item.tx3 = (TextView) v.findViewById(R.id.bfitem_t3);

		item.d1 = (ImageView) v.findViewById(R.id.bfitem_d1);
		item.d2 = (ImageView) v.findViewById(R.id.bfitem_d2);
		item.d3 = (ImageView) v.findViewById(R.id.bfitem_d3);

		item.u1 = (ImageView) v.findViewById(R.id.bfitem_up1);
		item.u2 = (ImageView) v.findViewById(R.id.bfitem_up2);
		item.u3 = (ImageView) v.findViewById(R.id.bfitem_up3);

		item.btn1 = (Button) v.findViewById(R.id.bfitem_img1);
		item.btn2 = (Button) v.findViewById(R.id.bfitem_img2);
		item.btn3 = (Button) v.findViewById(R.id.bfitem_img3);

		item.yly1 = (RelativeLayout) v.findViewById(R.id.bfitem_yly1);
		item.yly2 = (RelativeLayout) v.findViewById(R.id.bfitem_yly2);
		item.yly3 = (RelativeLayout) v.findViewById(R.id.bfitem_yly3);

		item.yy1 = (RelativeLayout) v.findViewById(R.id.bfitem_yy1);
		item.yy2 = (RelativeLayout) v.findViewById(R.id.bfitem_yy2);
		item.yy3 = (RelativeLayout) v.findViewById(R.id.bfitem_yy3);

		item.pro1 = (ProgressBar) v.findViewById(R.id.bfitem_pro1);
		item.pro2 = (ProgressBar) v.findViewById(R.id.bfitem_pro2);
		item.pro3 = (ProgressBar) v.findViewById(R.id.bfitem_pro3);

		item.txlist.add(item.tx1);
		item.txlist.add(item.tx2);
		item.txlist.add(item.tx3);

		item.btnlist.add(item.btn1);
		item.btnlist.add(item.btn2);
		item.btnlist.add(item.btn3);

		item.dlist.add(item.d1);
		item.dlist.add(item.d2);
		item.dlist.add(item.d3);

		item.ulist.add(item.u1);
		item.ulist.add(item.u2);
		item.ulist.add(item.u3);

		item.yylist.add(item.yly1);
		item.yylist.add(item.yly2);
		item.yylist.add(item.yly3);

		item.yylist2.add(item.yy1);
		item.yylist2.add(item.yy2);
		item.yylist2.add(item.yy3);

		item.prolist.add(item.pro1);
		item.prolist.add(item.pro2);
		item.prolist.add(item.pro3);

	}
}
