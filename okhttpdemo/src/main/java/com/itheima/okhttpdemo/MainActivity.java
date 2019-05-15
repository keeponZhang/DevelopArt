package com.itheima.okhttpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

//	private String  url = "http://www.baidu.com";
	private String  url = "http://www.oschina.net/action/api/login_validate";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		doGet();

//		doPost();

//		test();
	}





	private void doPost() {
		OkHttpClient okHttpClient = new OkHttpClient();
		FormBody formBody = new FormBody.Builder().add("username","itheima123@126.com")
				.add("pwd","itheima123")
				.add("keep_login","1").build();
		Request request = new Request.Builder().url(url).post(formBody).build();

		loadFormNet(okHttpClient, request);

	}

	private void doGet() {
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url( url).build();
		loadFormNet(okHttpClient, request);
	}

	private void loadFormNet(OkHttpClient okHttpClient, Request request) {
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				final String string = response.body().string();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}
