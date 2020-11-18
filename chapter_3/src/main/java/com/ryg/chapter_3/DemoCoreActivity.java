package com.ryg.chapter_3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DemoCoreActivity extends AppCompatActivity implements StickyLayout.OnGiveUpTouchEventListener {
    private StickyLayout stickyLayout;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_core);
        initView();
        LinearLayout ll = (LinearLayout) findViewById(R.id.sticky_header);
        stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
        stickyLayout.setOnGiveUpTouchEventListener(this);

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(DemoCoreActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        //如果位置为0，获取0位置的view
        if (listView.getFirstVisiblePosition() == 0) {
            View view = listView.getChildAt(0);
            //view在顶部位置单位像素
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }
}