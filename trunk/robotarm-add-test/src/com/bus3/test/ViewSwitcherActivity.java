/**
 * 
 */
package com.bus3.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

import com.bus3.R;

/**
 * @author li.li
 * 
 *         Apr 16, 2012
 * 
 */
public class ViewSwitcherActivity extends Activity {
	private ViewSwitcher mSwitcher;
	private Button btn_prev, btn_next;

	private Animation inRightToLeft;
	private Animation outRightToLeft;
	private Animation inLeftToRight;
	private Animation outLeftToRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_switch);

		mSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
		mSwitcher.setDisplayedChild(0);

		btn_next = (Button) findViewById(R.id.next);
		btn_prev = (Button) findViewById(R.id.prev);

		inRightToLeft = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.in_right_to_left);
		outRightToLeft = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.out_right_to_left);
		inLeftToRight = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.in_left_to_right);
		outLeftToRight = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.out_left_to_right);

		mSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.setInAnimation(inRightToLeft);
				mSwitcher.setOutAnimation(outRightToLeft);

				mSwitcher.showNext();
			}
		});
		btn_prev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.setInAnimation(inLeftToRight);
				mSwitcher.setOutAnimation(outLeftToRight);

				mSwitcher.showPrevious();
			}
		});
	}
}
