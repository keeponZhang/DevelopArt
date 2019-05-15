package com.itheima.thirdlogin.verticalLayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.itheima.thirdlogin.R;


public class VerticalActivity extends AppCompatActivity
{
	private VerticalLinearLayout mMianLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertical);

		mMianLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
		mMianLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener()
		{
			@Override
			public void onPageChange(int currentPage)
			{
//				mMianLayout.getChildAt(currentPage);
				Toast.makeText(VerticalActivity.this, "第"+(currentPage+1)+"页", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
