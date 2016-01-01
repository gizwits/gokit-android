package com.xpg.gokit.activity;

import java.io.InputStream;
import java.net.URL;

import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptchaCodeActivity extends BaseActivity implements OnClickListener {
	ImageView ivGetCaptchaCode;
	Button btnReGetCaptchaCode;
	Button next_passButton;
	EditText edt_phone;
	EditText etInputCaptchaCode;

	String captchacodeString, phoneString;
	/** The dialog. */
	ProgressDialog dialog;

	Intent it = new Intent();

	protected static final int CaptchaCode = 0;
	protected static final int TOAST1 = 1;
	protected static final int TOAST2 = 2;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CaptchaCode:
				XPGWifiSDK.sharedInstance().getCaptchaCode("dc5b945db45f427c97ec9ae881850623");
				break;
			case TOAST1:
				Toast.makeText(CaptchaCodeActivity.this, "图片验证码错误", Toast.LENGTH_LONG).show();
				dialog.cancel();
				break;
			case TOAST2:
				Toast.makeText(CaptchaCodeActivity.this, "操作频繁，请等待！", Toast.LENGTH_LONG).show();
				dialog.cancel();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_captchacode);
		handler.sendEmptyMessage(CaptchaCode);
		initView();
		initEvent();
		initData();
	}

	public void initView() {
		btnReGetCaptchaCode = (Button) findViewById(R.id.btnReGetCaptchaCode);
		next_passButton = (Button) findViewById(R.id.next_passButton);
		edt_phone = (EditText) findViewById(R.id.edt_phone);
		etInputCaptchaCode = (EditText) findViewById(R.id.etInputCaptchaCode);
		dialog = new ProgressDialog(this);
		dialog.setMessage("处理中，请稍候...");
	}

	public void initEvent() {
		btnReGetCaptchaCode.setOnClickListener(this);
		next_passButton.setOnClickListener(this);
	}

	public void initData() {
		captchacodeString = etInputCaptchaCode.getText().toString();
		phoneString = edt_phone.getText().toString();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnReGetCaptchaCode:
			handler.sendEmptyMessage(CaptchaCode);
			break;
		case R.id.next_passButton:
			initData();
			if (phoneString.length() == 11) {
				if (!captchacodeString.isEmpty()) {
					dialog.show();
					mCenter.cRequestSendVerifyCode(tokenString, captchaidString, captchacodeString, phoneString);
				} else {
					Toast.makeText(CaptchaCodeActivity.this, "请输入图片验证码", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(CaptchaCodeActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 图片验证码回调
	 */
	private String tokenString, captchaidString, captcthishaURL_String;

	protected void didGetCaptchaCode(int result, java.lang.String errorMessage, java.lang.String token,
			java.lang.String captchaId, java.lang.String captcthishaURL) {
		Log.e("AppTest",
				"图片验证码回调" + result + ", " + errorMessage + ", " + token + ", " + captchaId + ", " + captcthishaURL);
		tokenString = token;
		captchaidString = captchaId;
		captcthishaURL_String = captcthishaURL;
		new load_image().execute(captcthishaURL_String);
	}

	class load_image extends AsyncTask<String, Void, Drawable> {

		/**
		 * 加载网络图片
		 * 
		 * @param url
		 * @return
		 */
		private Drawable LoadImageFromWebOperations(String url) {
			InputStream is = null;
			Drawable d = null;
			try {
				is = (InputStream) new URL(url).getContent();
				d = Drawable.createFromStream(is, "src name");
				return d;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable drawable = LoadImageFromWebOperations(params[0]);
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			ivGetCaptchaCode = (ImageView) findViewById(R.id.ivGetCaptchaCode);
			ivGetCaptchaCode.setImageDrawable(result);
		}

	}

	protected void didRequestSendPhoneSMSCode(int result, java.lang.String errorMessage) {
		Log.e("AppTest", result + ", " + errorMessage);
		if (result == 0) {// 发送成功
			// 发送手机验证码
			it.setClass(this, RegisterActivity.class);
			String param = phoneString;
			it.putExtra("PHONE", param);
			startActivity(it);
		} else if (result == 9037) {
			handler.sendEmptyMessage(TOAST2);

		} else if (result == 9015) {// 发送失败
			handler.sendEmptyMessage(TOAST1);
			handler.sendEmptyMessage(CaptchaCode);

		}
	}
}
