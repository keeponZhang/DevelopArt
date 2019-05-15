package com.itheima.thirdlogin.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @创建者 keepon
 */
public interface Api {
	@POST("action/api/openid_login")
	@FormUrlEncoded
	Call<ResponseBody> loginQQ(@Field("catalog")String catalog, @Field("openid_info")String openid_info);
}
