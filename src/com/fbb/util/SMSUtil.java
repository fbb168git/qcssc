package com.fbb.util;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMSUtil {
//	正式环境	http://gw.api.taobao.com/router/rest	https://eco.taobao.com/router/rest
//		沙箱环境	http://gw.api.tbsandbox.com/router/rest	https://gw.api.tbsandbox.com/router/rest
	static boolean openSms = true;
	private static String AppKey = "23765545";
	private static String secret = "5c03b64cc3fde46f93b2e873b63621e6";
	public static void sendSms(String content, String numbers, String time, String phones) {
		if(!openSms) return;
//		openSms = false;
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", AppKey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType( "normal" );
		req.setSmsFreeSignName( "冯贝" );
//		req.setSmsParamString( "{name:'0421084期后2出现2连大',goods:'1，4，6，8，9',time:'12:34:09'}" );
		req.setSmsParamString( "{name:'"+content+"',goods:'"+numbers+"',time:'"+time+"'}" );
		req.setRecNum( phones );
		req.setSmsTemplateCode( "SMS_62890141" );
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		LogUtil.d(rsp.getBody());
	}
	
}
