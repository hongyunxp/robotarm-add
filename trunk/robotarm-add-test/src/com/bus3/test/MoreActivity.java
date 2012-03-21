package com.bus3.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bus3.R;
import com.bus3.common.activity.BaseActivity;
import com.bus3.common.utils.BaseUtils;
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
	
	private Map<String, List<District>> districtsMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_content);

		tabInvHandler().setTitle(R.layout.more_title);

		initDistricts();
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
	
	private void initDistricts(){
		Spinner district1 = (Spinner) findViewById(R.id.district_level1);

		String json = BaseUtils.loadData("http://192.168.0.116/district.html");
		JSONObject jo = null;
		try {
			jo = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		districtsMap = new HashMap<String, List<District>>();

		for (Iterator<?> it = jo.keys(); it.hasNext();) {

			String id = null;
			String parentId = null;
			String name = null;

			try {

				id = (String) it.next();
				JSONArray array = jo.getJSONArray(id);
				name = array.getString(0);
				parentId = array.getString(1);

				List<District> districts = districtsMap.get(parentId);
				if (districts == null)
					districts = new ArrayList<District>();

				districts.add(new District(id, parentId, name));

				districtsMap.put(parentId, districts);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		List<District> list = districtsMap.get("1");

		BaseAdapter adapter = new DistrictArrayAdapter(this, list);
		district1.setAdapter(adapter);
		district1.setPrompt("Spinner测试");

		district1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				District district = (District) parent.getItemAtPosition(position);

				Toast.makeText(MoreActivity.this, district.getId() + "|" + district.getParentId() + "|" + district.getName(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
	}

}
