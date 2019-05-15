package com.itheima.wechatpay;

/**
 * @创建者 keepon
 */
public class WeChatInfo {

	/**
	 * result : 0
	 * partnerId : 10000100
	 * prepayId : null
	 * nonceStr : 48c34cb86aa86816e112a44ef2bf4c30
	 * timestamp : 1490712175
	 * appkey : L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K
	 * packageValue : bank_type=WX&body&fee_type=1&input_charset=UTF-8&notify_url=http%3A%2F%2Fweixin.qq.com&out_trade_no=b0b07fecb2354efcdfc9671484b6eaa9&partner=10000100&spbill_create_ip=196.168.1.1&total_fee&sign=7221BF78D6DBCF0D2ED14EF02FA88832
	 * sign : 9f03074864015ed6aee65f9e97ef3fc07ba9d078
	 * bank_type : 1
	 */

	public int result;
	public String partnerId;
	public Object prepayId;
	public String nonceStr;
	public String timestamp;
	public String appkey;
	public String packageValue;
	public String sign;
	public String bank_type;

	@Override
	public String toString() {
		return "WeChatInfo{" +
				"result=" + result +
				", partnerId='" + partnerId + '\'' +
				", prepayId=" + prepayId +
				", nonceStr='" + nonceStr + '\'' +
				", timestamp='" + timestamp + '\'' +
				", appkey='" + appkey + '\'' +
				", packageValue='" + packageValue + '\'' +
				", sign='" + sign + '\'' +
				", bank_type='" + bank_type + '\'' +
				'}';
	}
}
