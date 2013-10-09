package com.xs.cn.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.utils.CommonUtils;
import com.readnovel.base.openapi.QZoneAble;
import com.xs.cn.R;

/**
 * 最后一页处理
 * 
 * @author li.li
 *
 * Mar 7, 2013
 */
public class ReadBookLastActivity extends QZoneAble implements OnClickListener {
	private static final int MAX_TEXT_SIZE = 100;
	private EditText editText;
	private TextView textView;

	private Button left2;
	private TextView tv;
	private String bookName;
	private String chapterName;
	private String bookId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_book_lastpage);
		
		CloseActivity.add(this);

		bookName = getIntent().getStringExtra("bookName");
		chapterName = getIntent().getStringExtra("chapterName");
		bookId = getIntent().getStringExtra("bookId");

		setTopBar();

		init();

		addListener();
	}

	private void init() {
		textView = (TextView) findViewById(R.id.last_read_page_share_content_count);
		editText = (EditText) findViewById(R.id.last_read_page_share_content);

		String shareContent = String.format(getString(R.string.last_read_share_content), chapterName);
		editText.setText(String.format(shareContent, bookName));
		editText.setSelection(editText.getText().length());

		String shareContentLimit = String.format(getString(R.string.last_read_share_content_limit), MAX_TEXT_SIZE - shareContent.length());
		textView.setText(shareContentLimit);

		/**
		 * 1. 当前为QQ登陆时，并且session未过期只显示QQ分享按钮
		 * 2. 当前为Sina登陆时，并且session未过期只显示sina分享按钮
		 * 3. 当都普通登陆时，qq分享和sina分享按钮都显示 
		 */
		Button shareQQ = (Button) findViewById(R.id.read_book_share_qq_button);
		Button shareSina = (Button) findViewById(R.id.read_book_share_sina_button);
		CommonUtils.controlShareButton(this, shareQQ, shareSina);

		shareQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForQQ(ReadBookLastActivity.this, editText.getText().toString(), bookId);

			}
		});

		shareSina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.shareForSina(ReadBookLastActivity.this, editText.getText().toString(), bookId);
			}
		});
	}

	private void addListener() {
		editText.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String shareContentLimit = String.format(getString(R.string.last_read_share_content_limit), MAX_TEXT_SIZE - s.length());
				textView.setText(shareContentLimit);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				temp = s;

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > MAX_TEXT_SIZE) {
					Toast.makeText(ReadBookLastActivity.this, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
					s.delete(MAX_TEXT_SIZE, editText.getText().length());
					editText.setText(s);
					editText.setSelection(s.length());
				}
			}
		});
	}

	private void setTopBar() {
		left2 = (Button) findViewById(R.id.title_btn_left2);
		tv = (TextView) findViewById(R.id.title_tv);

		tv.setText("分享到");

		left2.setText("  返回");
		left2.setVisibility(View.VISIBLE);
		left2.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.title_btn_left2) {//点击返回到书架
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}

	}
	
	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
