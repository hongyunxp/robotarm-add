package com.bus3.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private final String TAG = getClass().getSimpleName();
	private static final String APP_ID = "4f62ce1a";
	private static final String APP = "appid=" + APP_ID;
	private static final String temp="中华人民共和国";

	private String text;
	private String grammar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_content);

		tabInvHandler().setTitle(R.layout.more_title);

		Button b = (Button) findViewById(R.id.more_button);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				tabInvHandler().startSubActivity(R.id.main_tools_more, MoreSecondActivity.class);
			}

		});

	}

	// 语音听写示例
	public void recognizerDialog(final View view) {
		RecognizerDialog isrDialog = new RecognizerDialog(this, APP);
		isrDialog.setEngine("sms", null, null);

		isrDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onEnd(SpeechError error) {
				if (error != null)
					Log.e(TAG, error.getErrorDesc());

				Button b = (Button) view;

				if (text != null && text.length() > 0)
					Toast.makeText(MoreActivity.this, b.getText() + ": " + text, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(MoreActivity.this, b.getText() + ": " + "未识别", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
				if (isLast) {
					StringBuilder textBuilder = new StringBuilder();

					for (RecognizerResult result : results)
						textBuilder.append(result.text);

					text = textBuilder.toString();

				}

			}

		});

		isrDialog.show();
	}

	// 语音识别示例
	public void uploadDialog(final View view) throws UnsupportedEncodingException {
		// 创建上传对话框
		UploadDialog uploadDialog = new UploadDialog(this, APP);
		String keys = "张三，李四 ，Andy";
		uploadDialog.setContent(keys.getBytes("UTF-8"), "dt=keylist", "contact");

		uploadDialog.setListener(new UploadDialogListener() {

			@Override
			public void onDataUploaded(String contentID, String extendID) {
				grammar = extendID;
				Log.d(TAG, grammar);
			}

			@Override
			public void onEnd(SpeechError arg0) {

			}

		});

		uploadDialog.show();

		// 创建识别对话框
		RecognizerDialog isrDialog = new RecognizerDialog(this, APP);
		isrDialog.setEngine(null, null, grammar);
		isrDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onEnd(SpeechError error) {
			}

			@Override
			public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
			}

		});
		isrDialog.show();

	}
	
	//语音合成示例
	public void synthesizerDialog(View view) {
		SynthesizerDialog synDialog = new SynthesizerDialog(this, APP);
		synDialog.setText(temp, "dtt=Keylist");
		synDialog.setListener(new SynthesizerDialogListener() {

			@Override
			public void onEnd(SpeechError arg0) {
			}
		});
		synDialog.show();
	}
	
	//语音合成示例-后台模式
	public void synthesizerPlayer(View view){
		SynthesizerPlayer player=SynthesizerPlayer.createSynthesizerPlayer(this, APP); 
		player.setVoiceName("vivixiaomei");
		player.playText(temp, "ent=vivi21,bft=5", null);
		Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
	}

}
