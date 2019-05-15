package com.itheima.thirdlogin.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.itheima.thirdlogin.R;
import com.itheima.thirdlogin.view.ShakeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_CODE = 100;
	public static final int GOTOSCAN = 300;
	@BindView(R.id.tv)
	TextView mTv;
	private ShakeListener mShakeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				//TODO 检测到摇一摇
				Toast.makeText(MainActivity.this, "检测到摇一摇", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void qqLogin(View v) {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
	}

	public void scan(View view) {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
			//进入这里表示没有权限
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, GOTOSCAN);
		} else {

			Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
		} 
		

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case GOTOSCAN:
				if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
					//用户同意授权
					Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
					startActivity(intent);
				}else{
					//用户拒绝授权
					Toast.makeText(this, "调取权限失败", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
		if (requestCode == REQUEST_CODE) {
			//处理扫描结果（在界面上显示）
			if (null != data) {
				Bundle bundle = data.getExtras();
				if (bundle == null) {
					return;
				}
				if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
					String result = bundle.getString(CodeUtils.RESULT_STRING);

					mTv.setText(result);
//					Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
				} else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
					Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
