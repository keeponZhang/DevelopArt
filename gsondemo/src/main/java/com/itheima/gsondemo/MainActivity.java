package com.itheima.gsondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		demo1();
//		demo2();
//		demo3();
//		demo4();
//		demo5();
//		demo6();
//		demo7();
		demo8();
	}

	private void demo8() {
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().serializeNulls().create();
		User user = new User("怪盗kidou",24);
		System.out.println(gson.toJson(user));
	}

	private void demo7() {
		Gson gson = new Gson();
/*	  List<String> datas = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			datas.add("item "+2);
		}*/
		String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
		List<String> o = gson.fromJson(jsonArray,new TypeToken<List<String>>() {}.getType());
	}

	private void demo6() {
		Gson gson = new Gson();
		String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
		String[] strings = gson.fromJson(jsonArray, String[].class);
		for (String string : strings) {
			Log.d(TAG, "demo6: "+string);
		}

	}

	private void demo5() {
		Gson gson = new Gson();
		String json = "{\"name\":\"怪盗kidou\",\"age\":24,\"emailAddress\":\"ikidou_1@example.com\",\"email\":\"ikidou_2@example.com\",\"email_address\":\"ikidou_3@example.com\"}";
		User user = gson.fromJson(json, User.class);
		System.out.println(user.emailAddress); // ikidou_3@example.com
	}

	private void demo4() {
		Gson gson = new Gson();
//		String jsonString = "{\"name\":\"怪盗kidou\",\"age\":24}";
//		String jsonString = "{ \"name\": \"怪盗kidou\",\"age\":24, \"email_address\":\"444@qq.com\"}";
		String jsonString = "{ \"name\": \"怪盗kidou\",\"age\":24, \"email_address\":444@qq.com}";
		User user = gson.fromJson(jsonString, User.class);
		Log.d(TAG, "demo4: "+user.emailAddress);

	}

	private void demo3() {
		Gson gson = new Gson();
		User user = new User("怪盗kidou",24,"4628@qq.com");
		String jsonObject = gson.toJson(user); // {"name":"怪盗kidou","age":24}
		Log.d(TAG, "demo3: "+jsonObject);
	}

	private void demo2() {
		Gson gson = new Gson();
		String jsonNumber = gson.toJson(100);       // 100
		String jsonBoolean = gson.toJson(false);    // false
		String jsonString = gson.toJson("String"); //"String"
	}


	private static final String TAG = "MainActivity";
	private void demo1() {
		Gson gson = new Gson();
		int i = gson.fromJson("100", int.class);              //100
		double d = gson.fromJson("\"99.99\"", double.class);  //99.99
		String s = gson.fromJson("\"99.99\"", String.class);  //99.99
		boolean b = gson.fromJson("true", boolean.class);     // true
		String str = gson.fromJson("String", String.class);   // String
		Log.d(TAG, "demo1: ");
	}
}
