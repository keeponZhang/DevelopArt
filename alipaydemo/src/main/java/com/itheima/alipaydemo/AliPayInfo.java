package com.itheima.alipaydemo;

/**
 * @创建者 keepon
 */
public class AliPayInfo {

	@Override
	public String toString() {
		return "AliPayInfo{" +
				"payInfo='" + payInfo + '\'' +
				", errMsg='" + errMsg + '\'' +
				", errCode='" + errCode + '\'' +
				", payType='" + payType + '\'' +
				'}';
	}

	/**
	 * payInfo : partner="2088221626451032"&seller_id="3393900637@qq.com"&out_trade_no="032321525314048"&subject="Nexus 5x"&body="小米(MI) 小米5 全网通4G手机 双卡双待 白色 标准版(3G RAM+32G ROM) 标配"&total_fee="0.01"&notify_url="http://notify.msp.hk/notify.htm"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&return_url="m.alipay.com"&sign="w5lt1aLBN2FI4%2FY8D7XzK7EeSgHclMpeaP%2F5LrybS9DQ0qy%2BlpKhLz0D4udZyMDPoRjKy7RcyRq7UOvFhRn061OKjKsqJp6WcECVfWssNtEWrbAvh5A3KOV2RYjytEuzcbcK3Zj6s5f2T1FTYGlEyyGK1immvNQY9ijkDbf26QA%3D"&sign_type="RSA"
	 * errMsg : 请求成功
	 * errCode : 0
	 * payType : 1
	 */

	public String payInfo;
	public String errMsg;
	public String errCode;
	public String payType;
}
