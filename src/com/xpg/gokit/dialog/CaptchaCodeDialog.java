package com.xpg.gokit.dialog;

import java.io.InputStream;
import java.net.URL;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.activity.BaseActivity;
import com.xpg.gokit.activity.CaptchaCodeActivity;
import com.xpg.gokit.activity.RegisterActivity;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

public class CaptchaCodeDialog extends BaseActivity implements OnClickListener {

	ImageView ivGetCaptchaCode;
	Button btnReGetCaptchaCode, next_passButton;
	EditText etInputCaptchaCode;
	/** The dialog. */
	ProgressDialog dialog;

	protected static final int CaptchaCode = 0;
	protected static final int TOAST1 = 1;
	protected static final int TOAST2 = 2;
	protected static final int FINISH = 3;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CaptchaCode:
				XPGWifiSDK.sharedInstance().getCaptchaCode("dc5b945db45f427c97ec9ae881850623");
				break;
			case TOAST1:
				Toast.makeText(CaptchaCodeDialog.this, "图片验证码错误", Toast.LENGTH_LONG).show();
				finish();
				break;
			case TOAST2:
				Toast.makeText(CaptchaCodeDialog.this, "操作频繁，请等待！", Toast.LENGTH_LONG).show();
				finish();
				break;
			case FINISH:
				Toast.makeText(CaptchaCodeDialog.this, "发送成功", Toast.LENGTH_LONG).show();
				finish();
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
		setContentView(R.layout.dialog_capthacode);
		handler.sendEmptyMessage(CaptchaCode);
		initView();
		initEvent();
	}

	public void initView() {
		btnReGetCaptchaCode = (Button) findViewById(R.id.btnReGetCaptchaCode_dialog);
		next_passButton = (Button) findViewById(R.id.next_passButton_dialog);
		etInputCaptchaCode = (EditText) findViewById(R.id.etInputCaptchaCode_dialog);
		dialog = new ProgressDialog(this);
		dialog.setMessage("处理中，请稍候...");
	}

	public void initEvent() {
		btnReGetCaptchaCode.setOnClickListener(this);
		next_passButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnReGetCaptchaCode_dialog:
			handler.sendEmptyMessage(CaptchaCode);
			break;
		case R.id.next_passButton_dialog:
			String captchacodeString = etInputCaptchaCode.getText().toString();
			String phoneString = RegisterActivity.phoneString;
			mCenter.cRequestSendVerifyCode(tokenString, captchaidString, captchacodeString, phoneString);

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
			ivGetCaptchaCode = (ImageView) findViewById(R.id.ivGetCaptchaCode_dialog);
			ivGetCaptchaCode.setImageDrawable(result);
		}

	}

	protected void didRequestSendPhoneSMSCode(int result, java.lang.String errorMessage) {
		Log.e("AppTest", result + ", " + errorMessage);
		if (result == 0) {// 发送成功
			handler.sendEmptyMessage(FINISH);
		} else if (result == 9037) {
			handler.sendEmptyMessage(TOAST2);

		} else if (result == 9015) {// 发送失败
			handler.sendEmptyMessage(TOAST1);
			handler.sendEmptyMessage(CaptchaCode);
		}
	}

}
