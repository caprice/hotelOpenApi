package com.huoli.openapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoli.openapi.exception.OrderException;

public class GeoMapConvert {
	public static final int timeout = 8000;

	public static Logger logger = LoggerFactory.getLogger(GeoMapConvert.class);

	public static String[] placeNameConvertGeoLocation(String placeName) {
		if (placeName != null&&placeName.indexOf("%")==-1) {
			try {
				placeName = URLEncoder.encode(placeName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] point = new String[2];
		String result = null;
		try {
			result = GeoMapConvert.executeMethod("http://api.map.qq.com/?l=11&wd=" + placeName
					+ "&pn=0&rn=9&qt=poi&output=jsonp&fr=mapapi&cb=aaa&t=" + System.currentTimeMillis());
			String ss = result.split("\\(")[0];
			String s = result.replace(ss, "");
			s = s.trim().substring(1, s.length() - 1);
			JSONObject src = JSONObject.fromObject(s);
			JSONObject info = src.getJSONObject("info");
			String error = info.getString("error");
			if ("0".equals(error)) {
				JSONObject detail = src.getJSONObject("detail");
				JSONArray pois = null;
				Object poiss =null;
				if (detail != null) {
					 poiss = detail.get("pois");
				}
				if (poiss != null && poiss instanceof JSONArray) {
					pois = (JSONArray) poiss;
				}

				if (pois != null && pois.size() > 0) {
					JSONObject item = (JSONObject) pois.get(0);
					point[0] = item.getString("pointx");
					point[1] = item.getString("pointy");
				} else {
					JSONObject city = detail.getJSONObject("city");
					if (city != null) {
						System.out.println("city");
						point[0] = city.getString("pointx");
						point[1] = city.getString("pointy");
					}
				}
			}
			System.out.println(point[0] + "----" + point[1]);
		} catch (Exception e) {
			logger.error("placename convert geolocation is error,exception=[{}],geo server return is [{}]",
					Tools.exceptionMsg(e), result);
		}
		if("0.0".equals(point[0])||"0.0".equals(point[1])){
			return null;
		}
		return point;
	}

	/**
	 * 远程http请求
	 * 
	 * @param host
	 * @param port
	 * @param url
	 * @return
	 * @throws Exception
	 * @throws NoAuthorizedException
	 */
	private static String executeMethod(String url) throws OrderException {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(url);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		BufferedReader reader = null;
		HttpMethod method = null;
		try {
			method = new GetMethod(url);
			client.executeMethod(method);
			int statusCode = method.getStatusCode();
			String response = new String(method.getResponseBodyAsString().getBytes(), "utf-8");
			if (200 != statusCode) {
				throw new OrderException("geo server is error,statusCode=" + statusCode + "----------,url=" + url);
			}
			if (-1 != response.indexOf("\"code\":\"401\"")) {
				throw new Exception(response);
			}
			return response;
		} catch (Exception e) {
			throw new OrderException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
			if (null != method) {
				method.releaseConnection();
			}
		}
	}

	public static void main(String[] args) {
		String[] point = new String[2];
		point = GeoMapConvert.placeNameConvertGeoLocation("火车站");
		System.out.println(point[0] + "----" + point[1]);

	}
}
