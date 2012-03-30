package com.bus3.test;

import java.util.Arrays;
import java.util.List;

import robot.arm.provider.view.gif.GifView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;

public class HomeActivity extends BaseActivity {
	private static List<Integer> picList = Arrays.asList(R.drawable.girl1, R.drawable.girl2, R.drawable.girl3, R.drawable.girl4, R.drawable.girl5,
			R.drawable.girl6);
	private AdapterView<ImageGroupAdapter> gallery;
	private Toast toast;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_content);

		tabInvHandler().setTitle(R.layout.main_title);

		gallery = (AdapterView<ImageGroupAdapter>) findViewById(R.id.mygallery);
		gallery.setAdapter(new ImageGroupAdapter(this, picList));
		// gallery.setOnItemSelectedListener(new OnItemSelectedListener() {//
		// 显示第几个
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View v, int
		// position, long id) {
		// Toast.makeText(HomeActivity.this, (position + 1) + "/" +
		// picList.size(), Toast.LENGTH_SHORT).show(); // Toast显示图片位置
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> paramAdapterView) {
		// Toast.makeText(HomeActivity.this, "无选择", Toast.LENGTH_SHORT).show();
		// // Toast显示图片位置
		// }
		//
		// });

		// gallery.setSelection(1);// 使靠左侧显示
		toast = Toast.makeText(HomeActivity.this, "点击了：", Toast.LENGTH_SHORT);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("HomeActivity.onCreate(...).new OnItemClickListener() {...}.onItemClick()%%%%%%" + position);

				toast.setText("点击了：" + (position + 1) + "/" + gallery.getChildCount());
				toast.show();
			}
		});

		Gallery g = (Gallery) findViewById(R.id.my_gallery);
		g.setAdapter(new BaseAdapter() {
			private List<Integer> list = Arrays.asList(R.drawable.my_gif1, R.drawable.my_gif2, R.drawable.my_gif1);

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


				ImageView image = new GifView(HomeActivity.this);
				
				LayoutParams la = new LayoutParams(300, 300);
				image.setLayoutParams(la);
				
				image.setImageResource(list.get(position));

				return image;
			}

		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler().setTitle(R.layout.main_title);
	}

}
