package com.bus3.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import robot.arm.provider.view.gif.GifView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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

	private List<ImageView> images;

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
		
		//画图示例
		ImageView arrow = (ImageView) findViewById(R.id.home_gallery_select_arrow);
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.girl6);
		
		Bitmap image = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888); //图形的存储空间
		Canvas canvas = new Canvas(image);// 画板
		Paint paint = new Paint(Paint.DITHER_FLAG);//画笔
		
		canvas.drawBitmap(bm, 0, 0,paint);
		
		arrow.setImageBitmap(image);//在ImageView上设置图形

	}

	@Override
	protected void onResume() {
		super.onResume();
		tabInvHandler().setTitle(R.layout.main_title);

		images = new ArrayList<ImageView>();

		Gallery g = (Gallery) findViewById(R.id.my_gallery);
		g.setAdapter(new BaseAdapter() {
			private List<Integer> list = Arrays.asList(R.drawable.my_gif1, R.drawable.my_gif2, R.drawable.girl1, R.drawable.girl2, R.drawable.my_gif1);

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

				LayoutParams la = new LayoutParams(250, 250);
				image.setLayoutParams(la);

				image.setImageResource(list.get(position));

				images.add(image);

				return image;
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		for (ImageView image : images) {
			if (image instanceof GifView)

				((GifView) image).destroy();// 切换时释放图片资源
		}
	}
}
