/**
 * 
 */
package com.bus3.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_switch);

		mSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
		mSwitcher.setDisplayedChild(0);

		btn_next = (Button) findViewById(R.id.next);
		btn_prev = (Button) findViewById(R.id.prev);

		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.showNext();
			}
		});
		btn_prev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwitcher.showPrevious();
			}
		});
	}
}
