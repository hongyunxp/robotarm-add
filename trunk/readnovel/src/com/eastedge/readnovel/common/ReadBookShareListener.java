package com.eastedge.readnovel.common;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.beans.Shubenmulu;
import com.eastedge.readnovel.task.BookDetailTask;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.openapi.QZoneAble;
import com.xs.cn.R;

/**
 * 阅读页面分享工具栏监听器
 * 
 * @author li.li
 *
 * Mar 8, 2013
 */
public class ReadBookShareListener implements OnClickListener {
	private static final int MAX_TEXT_SIZE = 100;
	private QZoneAble act;
	private EditText editText;
	private TextView textView;
	private TextView title;
	private Dialog dialog;
	private Button shareQQ;
	private Button shareSina;
	private String aId;//书id
	private String chapterName;

	public ReadBookShareListener(QZoneAble act) {
		super();

		this.act = act;

	}

	@Override
	public void onClick(View v) {//点击分享
		this.dialog = customDialog(act, R.layout.read_book_share_tools_dialog);//分享对话框 
		init();
		addListener();
		Shubenmulu mulu = Util.read(aId);

		if (mulu != null) {//本地阅读有目录
			String bookName = mulu.getTitle();
			setShareContent(bookName);
		} else {//在线阅读无目录
			//得到书的明细，从而得到书名，异步
			new BookDetailTask(act, this).execute();
		}

	}

	/**
	 * 更改分享内容
	 * @param bookName
	 */
	public void setShareContent(String bookName) {
		String shareContent = String.format(act.getString(R.string.readbook_share_content), "《" + bookName + "》" + "至第" + chapterName + "章节");
		editText.setText(String.format(shareContent, bookName));
		editText.setSelection(editText.getText().length());
		String shareContentLimit = String.format(act.getString(R.string.last_read_share_content_limit), MAX_TEXT_SIZE - shareContent.length());
		textView.setText(shareContentLimit);
	}

	private void init() {
		title = (TextView) dialog.findViewById(R.id.last_read_page_share_title);
		textView = (TextView) dialog.findViewById(R.id.last_read_page_share_content_count);
		editText = (EditText) dialog.findViewById(R.id.last_read_page_share_content);
		shareQQ = (Button) dialog.findViewById(R.id.read_book_share_qq_button);
		shareSina = (Button) dialog.findViewById(R.id.read_book_share_sina_button);
		dialog.show();

		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮
		 * 2. 当前为Sina登陆时，并且session未过期只显示sina分享按钮
		 * 3. 当都普通登陆时，qq分享和sina分享按钮都显示 
		 */
		CommonUtils.controlShareButton(act, shareQQ, shareSina);

	}

	private void addListener() {
		editText.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String shareContentLimit = String.format(act.getString(R.string.last_read_share_content_limit), MAX_TEXT_SIZE - s.length());
				textView.setText(shareContentLimit);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				temp = s;

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > MAX_TEXT_SIZE) {
					Toast.makeText(act, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
					s.delete(MAX_TEXT_SIZE, editText.getText().length());
					editText.setText(s);
					editText.setSelection(s.length());
				}
			}
		});

		//关闭
		title.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dialog.dismiss();
				return false;
			}
		});

		shareQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForQQ(act, editText.getText().toString(), aId);
				dialog.dismiss();

			}
		});

		shareSina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForSina(act, editText.getText().toString(), aId);
				dialog.dismiss();
			}
		});
	}

	public void setAId(String aId) {
		this.aId = aId;
	}

	public Dialog customDialog(Context ctx, int layoutResId) {
		Dialog dialog = new Dialog(ctx, R.style.Theme_FullHeightDialog);
		dialog.setContentView(layoutResId);

		return dialog;
	}

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

}
