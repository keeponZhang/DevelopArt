package com.itheima.alipaydemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
	private static final int SDK_PAY_FLAG = 1;
	//4.处理支付结果
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					Log.e("TAG", "MainActivity handleMessage:" +payResult.getResultStatus());
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

						} else if (TextUtils.equals(resultStatus, "6001")){
							Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_SHORT).show();

						}
						else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

						}
					}
					break;
				}
				default:
					break;
			}
		};
	};


	private  String url = "http://192.168.1.102:8080/HeiMaPay/Pay?goodId=111&count=1&price=0.01";//支付宝支付协议接口
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void pay(View view){
		//1.提交参数到服务器
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, this, this);
		requestQueue.add(request);

	}

	@Override
	public void onResponse(String s) {
		//2.解析服务器返回的数据，得到"支付串码"
		Gson gson = new Gson();
		final AliPayInfo aliPayInfo = gson.fromJson(s, AliPayInfo.class);
		Log.e("TAG", "MainActivity onResponse:"+aliPayInfo );

		//3.调取第三方支付sdk的支付方法，传入支付串码
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(MainActivity.this);
				String result = alipay.pay(aliPayInfo.payInfo,true);
				Log.e("TAG", "MainActivity run:" +result);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}




	@Override
	public void onErrorResponse(VolleyError volleyError) {

	}
}
