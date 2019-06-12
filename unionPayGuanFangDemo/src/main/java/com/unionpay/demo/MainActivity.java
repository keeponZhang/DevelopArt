
/*
 * 黑马程序员 版权所有Copyright 1999-2015, CSDN.NET, All Rights Reserved
 *
 */
package com.unionpay.demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 银联支付Demo，经黑马老师简化后的版本
 * @author itheima
 * @since 2015/11/25
 * @see <a href='https://open.unionpay.com/ajweb/help/file/toDetailPage?id=196&flag=1'>银联支付sdk</a>
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	private ProgressBar progressBar;
	private String mTransalationNum;
	//mode："00"启动银联正式环境 ,"01"连接银联测试环境（可以使用测试账号，测试账号参阅文档）
	private String mode = "01";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btnPay).setOnClickListener(this);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}

	@Override
	public void onClick(View v) {
		unionPay();
	}

	/**
	 * 银联支付四部曲
	 * <li>1.post商品信息到服务器，服务器返回交易流水号</li>
	 * <li>2.解析服务器返回的交易流水号</li>
	 * <li>3.调用银联支付sdk，传入交易流水号</li>
	 * <li>4.处理支付结果 (在{@link #onActivityResult(int, int, android.content.Intent)}方法中处理)
	 */
	private void unionPay() {
		new PrePayTask().execute();//第一步
	}
	
	//1.post商品信息到服务器，服务器返回交易流水号
	class PrePayTask extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
		}
		
		/**
		 * 1.post商品信息到服务器，服务器返回交易流水号
		 */
		@Override
		protected String doInBackground(Void... params) {
			String result = null;
			//真实开发需要post相关信息（商品信息、用户信息、支付方式）到自己服务器，然后服务器调用银联接口，生成交易流水号，返回给客户端
			String url = "http://101.231.204.84:8091/sim/getacptn";
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				HttpResponse response = client.execute(post);
				result = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			//2.解析服务器返回的交易流水号，这里银联直接返回，真实开发应该是自己服务器返回字段，比如{success:"ok",pay_code:772738}
			return result;//返回交易流水号
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mTransalationNum = result;
			hideProgressBar();
			//3.调用银联支付sdk，传入交易流水号
			new PayTask().execute();
		}
	}
	
	/**支付异步任务*/
	class PayTask extends AsyncTask<Void, Void, Void>{

		//3.调用银联支付sdk，传入交易流水号
		@Override
		protected Void doInBackground(Void... params) {
			/**
			 * tranNum:交易流水号
			 * mode："00"启动银联正式环境 ,"01"连接银联测试环境（可以使用测试账号，测试账号参阅文档）
			 */
			UPPayAssistEx.startPayByJAR(MainActivity.this, PayActivity.class, null, null,mTransalationNum, mode);
			return null;
		}
	}
	
	private void showProgressBar(){
		progressBar.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar(){
		progressBar.setVisibility(View.GONE);
	}
	
	//4.处理支付结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String msg = null;
		/** 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消*/
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}


}
