package com.itheima.thirdlogin.network;

import retrofit2.Retrofit;

/**
 * @创建者 keepon
 */
public class ZhangRetrofit {

	public Api getApi() {
		return mApi;
	}

	private Api mApi;

	private ZhangRetrofit() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.oschina.net/").build();
		mApi = retrofit.create(Api.class);

	}

	private static ZhangRetrofit sZhangRetrofit;

	public static ZhangRetrofit getInstance() {
		if (sZhangRetrofit == null) {
			synchronized (ZhangRetrofit.class) {
				if (sZhangRetrofit == null) {
					sZhangRetrofit = new ZhangRetrofit();
				}
			}
		}

		return sZhangRetrofit;
	}

}
