package com.imooc.dragviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ryg.chapter_3.R;


public class ScrollActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
    }

    public void btnView(View view) {
        startActivity(new Intent(this, DragViewTest.class));
    }

    public void btnViewGroup(View view) {
        startActivity(new Intent(this, DragViewGroupTest.class));
    }
}
