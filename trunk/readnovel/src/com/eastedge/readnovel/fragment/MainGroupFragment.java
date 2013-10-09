package com.eastedge.readnovel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eastedge.readnovel.common.LocalStore;
import com.xs.cn.R;

/**
 * 导航抽屉
 * 
 * 
 */
public class MainGroupFragment extends Fragment {

	private RadioGroup mRadioGroup;
	public RadioButton btn1, btn2, btn3, btn4, btn5;
	private boolean refreshSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_group, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		LocalStore.setfirstload(getActivity(), "1");

		mRadioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
		mRadioGroup.setOnCheckedChangeListener(mRadioGroupListener);
		btn1 = (RadioButton) getActivity().findViewById(R.id.menu_btn1);
		btn2 = (RadioButton) getActivity().findViewById(R.id.menu_btn2);
		btn3 = (RadioButton) getActivity().findViewById(R.id.menu_btn3);
		btn4 = (RadioButton) getActivity().findViewById(R.id.menu_btn4);
		btn5 = (RadioButton) getActivity().findViewById(R.id.menu_btn5);

		int index = getActivity().getIntent().getIntExtra("index", 1);
		switch (index) {
		case 1:
			btn1.performClick();
			break;
		case 2:
			btn2.performClick();
			break;
		case 3:
			btn3.performClick();
			break;
		case 4:
			btn4.performClick();
			break;
		case 5:
			btn5.performClick();
			break;
		}
	}

	private RadioGroup.OnCheckedChangeListener mRadioGroupListener = new RadioGroup.OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (checkedId == btn1.getId()) {
				doBtn1();
			} else if (checkedId == btn2.getId()) {
				doOnCheckedChanged(new MenuPaihangFragment());
			} else if (checkedId == btn3.getId()) {
				doOnCheckedChanged(new MenuNewBookFragment());
			} else if (checkedId == btn4.getId()) {
				doOnCheckedChanged(new MenuFenleiFragment());
			} else if (checkedId == btn5.getId()) {
				doBtn5();
			}
		}

	};

	public void doBtn5() {
		doOnCheckedChanged(new MenuSearchFragment());
	}

	private void doBtn1() {
		doOnCheckedChanged(new MenuTuijianFragment());
	}

	public boolean isRefreshSearch() {
		return refreshSearch;
	}

	public void setRefreshSearch(boolean refreshSearch) {
		this.refreshSearch = refreshSearch;
	}

	/**
	 * 选择标签
	 * 
	 * @param fragment
	 */
	private void doOnCheckedChanged(Fragment fragment) {
		FragmentManager fManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fTransaction.replace(R.id.mainLinear, fragment);
		// fTransaction.addToBackStack(null);
		fTransaction.commit();
	}

}
