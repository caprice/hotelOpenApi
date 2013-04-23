package com.huoli.openapi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.huoli.openapi.util.MD5;





public class Client {
	private int connection_timeout = 6000 * 10;
	private String channelId;
	private String token;
	private String apiUrl;

	public Client() {

	}

	public Client(String channelId, String token, String apiUrl) {
		this.channelId = channelId;
		this.token = token;
		this.apiUrl = apiUrl;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
//	public static void main(String[] args) throws IOException {
//		TestClient test = new TestClient();
//		Map<String, String> headerMap = new HashMap<String, String>();
//		headerMap.put("channelId", "100011307");
//		headerMap.put("timeStamp", "1357806088350");
//		headerMap.put("businessType", "getCityList");
//		// headerMap.put("signature", getSignature(channelId, TimeStamp,
//		// businessType, token));
//		headerMap.put("signature", "A712ABDF4645606CD2BB7DFF9486379D");
//
//		PostMethod method = test.executePost("http://221.235.53.169:8080/HotelOpenApi/getCityList", null, headerMap,
//				"utf-8");
//		String message = method.getResponseBodyAsString();
//		System.out.println(message);
//
//		Client client = new Client();
//
//	}

	public String search(String lat, String lnt, Integer tudemode, String hotelType, String radius, String date,
			Integer count, Integer isbooking) {
		return null;
	}

	public String getRoomStatusBatch() {
		return null;
	}

	public String getRooms() {
		return null;
	}

	public String preprocess() {
		return null;
	}

	public String commit() {
		return null;
	}

	public String dSendVerify() {
		return null;
	}

	public String getCityList() {
		return null;
	}

	public String getChainList() {
		return null;
	}

	public String getHotelList() {
		return null;
	}

	public String orderList() {

		return null;
	}

	public String orderDetail() {
		return null;
	}

	public String cancel() {
		return null;
	}

	public String getSignature(String channelId, String TimeStamp, String businessType, String token) {
		StringBuilder src = new StringBuilder();
		src.append(TimeStamp).append(channelId).append(MD5.md5(token)).append(businessType);
		return MD5.md5(src.toString());
	}

	public String getSignature(String TimeStamp, String businessType) {
		return this.getSignature(this.channelId, TimeStamp, businessType, this.token);
	}

	public String getSignature(String businessType) {
		return this.getSignature(this.channelId, String.valueOf(System.currentTimeMillis()), businessType, this.token);
	}

	public PostMethod executePost(String url, Map<String, String> paramMap, Map<String, String> headerMap,
			String encoding) {

		HttpClient client = new HttpClient();
		client.getParams().setContentCharset(encoding);
		BufferedReader reader = null;
		PostMethod method = null;
		try {
			method = new PostMethod(url);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connection_timeout);
			client.getHttpConnectionManager().getParams().setSoTimeout(connection_timeout);
			// 构造post方法参数
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			if (paramMap != null)
				for (String key : paramMap.keySet()) {
					NameValuePair pair = new NameValuePair(key, paramMap.get(key));
					paramList.add(pair);
				}
			method.setRequestBody(paramList.toArray(new NameValuePair[paramList.size()]));
			List<Header> headers = new ArrayList<Header>();
			for (String key : headerMap.keySet()) {
				headers.add(new Header(key, headerMap.get(key)));
				client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
			}

			client.executeMethod(method);
			return method;
		} catch (Exception e) {
		}
		return null;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

}
