package com.xs.cn.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastedge.readnovel.adapters.AdapterForLinearLayout2;
import com.eastedge.readnovel.beans.Shuping_huifuinfo;
import com.eastedge.readnovel.common.CloseActivity;
import com.eastedge.readnovel.threads.ShupinghuifuThread;
import com.mobclick.android.MobclickAgent;
import com.xs.cn.R;

public class Novel_shuping_huifu extends Activity {
	private Intent intent;
	private int tid;
	ShupinghuifuThread sphfth;
	int i = 1;
	// private LinearLayoutForListView2 sphf_lv;
	private ListView sphf_lv;
	private AdapterForLinearLayout2 adapter;
	private LinearLayout loadingLayout = null;
	private ArrayList<Shuping_huifuinfo> list = null;
	// private String Message;
	private String Subject;
	private String Lastpost;
	private String Lastposter;
	private TextView textView1;
	private LinearLayout ly_hufu_head;
	private ProgressDialog progress;

	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				list = sphfth.sphf.getHFlist();
				if (sphfth.sphf.getCur_page_number()==sphfth.sphf.getTotal_page_number()) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					loadingLayout.addView(loadMoreBtn);
				}
				adapter = new AdapterForLinearLayout2(Novel_shuping_huifu.this, list, Lastpost, Lastposter);
				sphf_lv.setAdapter(adapter);
				break;
			case 2:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}

				list.addAll(sphfth.sphf.getHFlist());
				if (sphfth.sphf.getCur_page_number()==sphfth.sphf.getTotal_page_number()) {
					loadingLayout.removeAllViews();
				} else {
					loadingLayout.removeAllViews();
					loadingLayout.addView(loadMoreBtn);
				}
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_sp_hf);
		CloseActivity.add(this);

		sphf_lv = (ListView) findViewById(R.id.sphf_lv);

		LayoutInflater inflater = Novel_shuping_huifu.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.novel_sp_hf_head2, null);
		ly_hufu_head = (LinearLayout) view.findViewById(R.id.ly_hufu_head);
		textView1 = (TextView) view.findViewById(R.id.textView1);
		ly_hufu_head.setPadding(-1, -1, -1, -1);

		sphf_lv.addHeaderView(view);
		sphf_lv.addFooterView(showLayout());

		intent = getIntent();
		final Bundle bundle = intent.getBundleExtra("newbook");

		tid = bundle.getInt("Tid");
		Subject = bundle.getString("Subject");
		Lastpost = bundle.getString("Lastpost");
		Lastposter = bundle.getString("Lastposter");

		textView1.setText(Subject);
		setTopBar();

		i = 1;
		sphfth = new ShupinghuifuThread(Novel_shuping_huifu.this, handler,
				Novel_sbxxy.Articleid, tid, i);
		sphfth.start();

		progress = ProgressDialog.show(Novel_shuping_huifu.this, "温馨提示",
				"加载中，请稍后...");
		progress.show();
	}

	private void setTopBar() {
		RelativeLayout rl;
		Button left1;
		TextView topTv;
		rl = (RelativeLayout) findViewById(R.id.webview_top);
		left1 = (Button) rl.findViewById(R.id.title_btn_left1);
		topTv = (TextView) rl.findViewById(R.id.title_tv);

		left1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (v.getId() == R.id.title_btn_left1) {
					finish();
				}
			}
		});

		topTv.setText(Novel_sbxxy.title);
	}

	// 以下为分页显示代码
	private static LinearLayout searchLayout = null;
	private static Button loadMoreBtn = null;

	public LinearLayout showLayout() {
		searchLayout = new LinearLayout(this);
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);

		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setPadding(0, 0, 15, 0);
		// progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_medium));
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView textView = new TextView(this);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);
		loadMoreBtn = new Button(getApplicationContext());
		loadMoreBtn.setText("更多评论...");
		loadMoreBtn.setBackgroundColor(Color.WHITE);
		loadMoreBtn.setTextColor(Color.rgb(0, 102, 184));
		loadMoreBtn.setTextSize(16);

		loadMoreBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				loadingLayout.removeAllViews();
				loadingLayout.addView(searchLayout,
						new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT));

				i++;
				sphfth = new ShupinghuifuThread(Novel_shuping_huifu.this,
						handler, Novel_sbxxy.Articleid, tid, i);
				sphfth.start();
			}
		});

		loadingLayout = new LinearLayout(this);
		LinearLayout.LayoutParams sl = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		sl.bottomMargin = 1;
		loadingLayout.addView(loadMoreBtn, sl);
		loadingLayout.setGravity(Gravity.CENTER);
		return loadingLayout;
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		CloseActivity.curActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		CloseActivity.remove(this);
	}
}
