package com.bus3.test;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs, R.attr.galleryStyle);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		System.out.println("MyGallery.onScroll()" + e1.getX() + "|" + e2.getX() + "|" + computeHorizontalScrollOffset());

		if (e2.getX() > e1.getX() && computeHorizontalScrollOffset() > 1) {// to right
			super.onScroll(e1, e2, distanceX, distanceY);
		} else if (e2.getX() < e1.getX() && computeHorizontalScrollOffset() < getChildCount()) {// to left
			super.onScroll(e1, e2, distanceX, distanceY);
		}

		return true;
	}

}
