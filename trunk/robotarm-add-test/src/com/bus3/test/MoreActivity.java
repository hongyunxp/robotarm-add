package com.bus3.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import robot.arm.utils.BaseUtils;
import robot.arm.utils.BaseUtils.Result;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.SynthesizerDialog;
import com.iflytek.ui.SynthesizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;

public class MoreActivity extends BaseActivity {
	private static final String TAG = MoreActivity.class.getSimpleName();
	private static final String APP = "appid=4f62ce1a";

	private static final String temp = "中华人民共和国";// 语音合成使用文字
	private static final String rec = "中国,美国,我是学生";// 语音识别使用文字

	private List<District> list;

	// 地址选择三级联动
	private Spinner spinner1;// 省
	private Spinner spinner2;// 市
	private Spinner spinner3;// 区县

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_content);

		tabInvHandler().setTitle(R.layout.more_title);

		 initDistinct();// 初始化地址选择器

	}

	public void startSubAct(View view) {
		tabInvHandler().startSubActivity(R.id.main_tools_more, MoreSecondActivity.class);
	}

	// 语音识别（ARS - Automated Speech Recognition）
	public void ars(final View view) {
		RecognizerDialog isrDialog = new RecognizerDialog(this, APP);
		isrDialog.setEngine("sms", null, null);

		exeRecongizerDialog(isrDialog, view);
	}

	// 语音识别-文字
	public void ars2(final View view) throws UnsupportedEncodingException {
		// 创建上传对话框-上传文字
		UploadDialog uploadDialog = new UploadDialog(this, APP);
		uploadDialog.setContent(rec.getBytes("UTF-8"), "dt=keylist", "contact");

		uploadDialog.setListener(new UploadDialogListener() {

			@Override
			public void onDataUploaded(String contentID, String extendID) {
				view.setTag(extendID);
			}

			@Override
			public void onEnd(SpeechError error) {
				// 创建识别对话框-识别语音
				RecognizerDialog isrDialog = new RecognizerDialog(MoreActivity.this, APP);
				isrDialog.setEngine(null, null, String.valueOf(view.getTag()));
				exeRecongizerDialog(isrDialog, view);
			}

		});

		uploadDialog.show();

	}

	// 语音合成(tts - Text To Speech)
	public void tts(View view) {
		SynthesizerDialog synDialog = new SynthesizerDialog(this, APP);
		synDialog.setText(temp, "dtt=Keylist");
		synDialog.setListener(new SynthesizerDialogListener() {

			@Override
			public void onEnd(SpeechError arg0) {
				Toast.makeText(MoreActivity.this, temp, Toast.LENGTH_LONG).show();
			}
		});
		synDialog.show();
	}

	// 语音合成-后台模式
	public void tts2(View view) {
		SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(this, APP);
		player.setVoiceName("vivixiaomei");
		player.playText(temp, "ent=vivi21,bft=5", null);
		Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
	}

	private void exeRecongizerDialog(RecognizerDialog isrDialog, final View view) {
		isrDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onEnd(SpeechError error) {
				if (error != null)
					Log.e(TAG, error.getErrorDesc());

				String text = String.valueOf(view.getTag());

				if (text != null && text.length() > 0)
					Toast.makeText(MoreActivity.this, text, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(MoreActivity.this, "未识别", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
				if (isLast) {
					StringBuilder textBuilder = new StringBuilder();

					for (RecognizerResult result : results)
						textBuilder.append(result.text);

					view.setTag(textBuilder.toString());

				}

			}

		});

		isrDialog.show();

	}

	public void viewSwitch(View view) {
		Intent intent = new Intent(this, ViewSwitcherActivity.class);
		startActivity(intent);
	}

	public void viewFlipper(View view) {
		Intent intent = new Intent(this, SwitchFlipperActivity.class);
		startActivity(intent);
	}

	// 初始地址选择
	private void initDistinct() {
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);

		try {
			Result result = BaseUtils.get("http://192.168.0.104:8080/test.js", null, null, HTTP.UTF_8);
			String json = result.httpEntityContent();
			JSONObject jo = new JSONObject(json);

			JSONArray ja = jo.names();

			// 加载所有地址
			list = new ArrayList<District>(ja.length());

			for (int i = 0; i < ja.length(); i++) {

				String id = String.valueOf(ja.get(i));
				JSONArray content = jo.getJSONArray(id);
				String name = content.getString(1);
				String parentId = content.getString(0);

				District d = new District(id, name, parentId);
				list.add(d);
			}

			// 排序(由小到大)
			Collections.sort(list, new Comparator<District>() {

				@Override
				public int compare(District d1, District d2) {
					int id1 = Integer.valueOf(d1.getId());
					int id2 = Integer.valueOf(d2.getId());

					return (id1 < id2 ? -1 : (id1 == id2 ? 0 : 1));

				}
			});

			// 查找省级
			List<District> list1 = new ArrayList<District>();
			for (District d : list) {
				System.out.println(d.getId());
				if (d.getParentId().equals("1"))
					list1.add(d);
			}
			DistrictArrayAdapter adapter1 = new DistrictArrayAdapter(this, list1);
			spinner1.setAdapter(adapter1);// 设置省级
			spinner1.setSelected(false);
			spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					// 查找市级
					List<District> list2 = new ArrayList<District>();
					for (District d : list) {
						if (d.getParentId().equals(view.getTag()))
							list2.add(d);
					}
					DistrictArrayAdapter adapter1 = new DistrictArrayAdapter(MoreActivity.this, list2);
					spinner2.setAdapter(adapter1);// 设置市级
					spinner2.setSelected(false);
					spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							// 查找区县级
							List<District> list3 = new ArrayList<District>();
							for (District d : list) {
								if (d.getParentId().equals(view.getTag()))
									list3.add(d);
							}
							DistrictArrayAdapter adapter3 = new DistrictArrayAdapter(MoreActivity.this, list3);
							spinner3.setAdapter(adapter3);// 设置区县级
							spinner3.setSelected(false);
							spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

									District select1 = (District) spinner1.getSelectedItem();
									District select2 = (District) spinner2.getSelectedItem();
									District select3 = (District) spinner3.getSelectedItem();

									System.out.println("当前选择的地址为|" + select1.getName() + "|" + select2.getName() + "|" + select3.getName());
								}

								@Override
								public void onNothingSelected(AdapterView<?> arg0) {
								}

							});
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}

					});
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
