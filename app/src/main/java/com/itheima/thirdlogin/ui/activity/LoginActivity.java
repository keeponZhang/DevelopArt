package com.itheima.thirdlogin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.itheima.thirdlogin.R;
import com.itheima.thirdlogin.conf.Constants;
import com.itheima.thirdlogin.network.Api;
import com.itheima.thirdlogin.network.ZhangRetrofit;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

	@BindView(R.id.btnLogin)
	Button mBtnLogin;
	@BindView(R.id.btnPublishTweet)
	Button mBtnPublishTweet;
	@BindView(R.id.btnUpdateNote)
	Button mBtnUpdateNote;
	@BindView(R.id.btnLoginByQQ)
	Button mBtnLoginByQQ;
	@BindView(R.id.btnLoginBySina)
	Button mBtnLoginBySina;
	private Tencent mTencent;
	private IUiListener listener  = new BaseUiListener();
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		initTencent();
	}

	private void initTencent() {
		/*-------------------------qq 登录----------------------------*/
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。
		mTencent = Tencent.createInstance("1106025938", this.getApplicationContext());
		/*-------------------------sina 登录----------------------------*/
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
	}

	@OnClick({R.id.btnLogin, R.id.btnPublishTweet, R.id.btnUpdateNote, R.id.btnLoginByQQ, R.id.btnLoginBySina})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnLogin:
				break;
			case R.id.btnPublishTweet:
				break;
			case R.id.btnUpdateNote:
				break;
			case R.id.btnLoginByQQ:
				gotoQQLogin();
				break;
			case R.id.btnLoginBySina:
				gotoSinaLogin();
				break;
		}
	}

	private void gotoSinaLogin() {
		mSsoHandler = new SsoHandler( this, mAuthInfo);
		mSsoHandler.authorize(new AuthListener());
	}

	private void gotoQQLogin() {
		if (!mTencent.isSessionValid())
		{
			//发起授权请求
			mTencent.login(this, "all", listener);
		}
	}

/*-------------------------qq Login 收到结果----------------------------*/
private static final String TAG = "LoginActivity";
	//处理授权结果
    class BaseUiListener implements IUiListener {
		@Override
		public void onComplete( Object response) {
			//获取授权信息
			JSONObject jsonObject = (JSONObject) response;
			String result = jsonObject.toString();
			Log.d(TAG, "onComplete: "+result);
//			Toast.makeText(LoginActivity.this, "授权成功"+result, Toast.LENGTH_SHORT).show();
			//发起第三方登录的请求
			Api api = ZhangRetrofit.getInstance().getApi();
			api.loginQQ("qq",result).enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					try {
						String string = response.body().string();
						Log.d(TAG, "onResponse: "+string);
						Toast.makeText(LoginActivity.this, string, Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable t) {

				}
			});
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_LONG).show();

		}
		@Override
		public void onCancel() {
			Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_LONG).show();

		}
	}
/*-------------------------sina 登录收到结果----------------------------*/
 class AuthListener implements WeiboAuthListener {
	@Override
	public void onComplete(Bundle values) {
		Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
		if (mAccessToken.isSessionValid()) {
			String token = mAccessToken.getToken();//保存Token
			Log.e(TAG, "onComplete: token "+token);
			Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();
		} else {
			// 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
			String code = values.getString("code", "");
			Log.e(TAG, "onComplete: "+code);
			Toast.makeText(LoginActivity.this, code, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onWeiboException(WeiboException e) {
		Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onCancel() {
		Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

	}
}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
/*-------------------------tencent----------------------------*/
		Tencent.onActivityResultData(requestCode,resultCode,data,listener);

	/*-------------------------sina----------------------------*/
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
