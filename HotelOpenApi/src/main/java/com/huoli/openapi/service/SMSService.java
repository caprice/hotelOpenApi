package com.huoli.openapi.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.huoli.openapi.util.DateUtil;

public class SMSService {

	static Logger logger = Logger.getLogger(SMSService.class);

	public static final String allChar = "0123456789";

	public static final String letterChar = "0123456789";

	public static List<String> list;

	private int sendCount;

	private String tokenId;

	private String tokenPhone;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenPhone() {
		return tokenPhone;
	}

	public void setTokenPhone(String tokenPhone) {
		this.tokenPhone = tokenPhone;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public static boolean SendSMS(String mobile, String formatId,
			String content, String string, String sToken) {
		StringBuffer url = new StringBuffer();
		url.append("http://www.wemediacn.net/webservice/smsservice.asmx/SendSMS?");
		url.append("mobile=");
		url.append(mobile);
		url.append("&FormatID=");
		url.append(formatId);
		url.append("&Content=");
		url.append(content);
		url.append("&ScheduleDate=");
		url.append(string);
		url.append("&TokenID=");
		url.append(sToken);
		logger.info(url.toString());
		String resp = getRequest(url.toString());
		logger.info(resp);
		return true;
	}

	public static String SendSMS(String mobile, String verifyCode) {

		return SendSMSPost(mobile, "8", String.format("快捷酒店管家验证码:%s",verifyCode),
				DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD),
				"7100518830323341");
	}

	public static String SendSMSPost(String mobile, String formatId,
			String content, String string, String sToken) {
		HttpPost httppost = null;
		String b = "";
		try {
			String url = "http://h.133.cn:7992/webservice/smsservice.asmx/SendSMS";
			httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile", mobile));
			params.add(new BasicNameValuePair("FormatID", formatId));
			params.add(new BasicNameValuePair("Content", content));
			params.add(new BasicNameValuePair("ScheduleDate", string));
			params.add(new BasicNameValuePair("TokenID", sToken));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httppost.addHeader("Host", "h.133.cn:7992");
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			HttpResponse response = new DefaultHttpClient().execute(httppost);
			String result = new String(readStream(response.getEntity()
					.getContent()));
			if (!"".equals(delNull(result)) && result.indexOf("ERROR") >= 0) {
				throw new Exception(result);
			}
			if (!"".equals(delNull(result)) && result.indexOf("OK") > -1) {
				if (result.indexOf("[") > -1 && result.indexOf("]") > -1) {
					String[] a = result.split("\\[");
					String[] arr = a[1].split("\\]");
					b = arr[0];
				}

			}
			logger.info("SendSMSPost:" + result);
		} catch (Exception e) {
			logger.error("SendSMSPost Error!", e);
		} finally {
			// httppost.abort();
		}
		return b;
	}

	/**
	 * 删除参数中的null
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String delNull(String str) {
		if (str == null) {
			return "";
		} else if (str.trim().equals("null") || str.trim().indexOf("null") > 0) {
			return "";
		} else {
			return str.trim();
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public static String getRequest(String url) {
		String resp = "";
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		try {
			httpClient.executeMethod(get);
			resp = get.getResponseBodyAsString();
			logger.info("getRequest:" + resp);
			get.releaseConnection();
			if (resp.contains("OK:")) {
				logger.info("SendSMS  success");
			} else {
				logger.info(resp);
			}
		} catch (HttpException e) {
			logger.info(e.getMessage());
			return getRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
			return getRequest(url);
		} finally {
			get.releaseConnection();
		}
		return resp;
	}

	public static String getRequestQuerySMSUp(String url) {
		String resp = "";
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		try {
			httpClient.executeMethod(get);
			resp = get.getResponseBodyAsString();
			get.releaseConnection();
			// resp = code + "," + resp;
		} catch (HttpException e) {
			logger.info(e.getMessage());
			return getRequest(url);
		} catch (IOException e) {
			logger.info(e.getMessage());
			return getRequest(url);
		} finally {
			get.releaseConnection();
		}
		return resp;
	}

	public static String generateMixString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(letterChar.length())));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		SendSMS("18064088729","2344");

	}
}
