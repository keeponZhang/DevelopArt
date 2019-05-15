package com.itheima.wechatpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.wxapi.Constants;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

//	private String url ="http://10.0.2.2:8080/itheima/pay";
	private String url ="http://192.168.1.102:8080/itheima/pay";
	private WeChatInfo mWeChatInfo;
	private IWXAPI     api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWechatApi();
	}
	/**
	 * 初始化微信支付api对象
	 */
	private void initWechatApi() {
		api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
	}
	//1.提交参数到服务器
	public void pay(View view){
		StringRequest request = new StringRequest(url, this, this);
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(request);
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {

	}

	private static final String TAG = "MainActivity";
	@Override
	public void onResponse(String s) {
		//2.服务器返回json,解析获得支付串码
		mWeChatInfo = JSON.parseObject(s, WeChatInfo.class);
		Log.d(TAG, "onResponse: "+mWeChatInfo.toString());

	}

	//3.调用微信支付sdk，传入"支付串码"
	public void sendPayRequest() {
		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		req.partnerId = mWeChatInfo.partnerId;
		req.prepayId = mWeChatInfo.prepayId+"";
		req.nonceStr = mWeChatInfo.nonceStr;
		req.timeStamp =mWeChatInfo.timestamp;
		req.packageValue = mWeChatInfo.packageValue;
		req.sign = mWeChatInfo.sign;
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		//3.调用微信支付sdk支付方法
		api.sendReq(req);

	}
}
