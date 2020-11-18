package com.itheima.unionpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class MainActivity extends Activity implements Response.ErrorListener,
        Response.Listener<String> {

    private String url = "http://101.231.204.84:8091/sim/getacptn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pay(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(url, this, this);
        requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(String s) {

        String mode = "01";
        String mTrnum = s;
        UPPayAssistEx.startPayByJAR(MainActivity.this, PayActivity.class, null, null, mTrnum, mode);
    }

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
