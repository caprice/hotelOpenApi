package com.huoli.openapi.roomStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.exception.HotelStatusException;
import com.huoli.openapi.exception.OrderException;
import com.huoli.openapi.help.PropertyConfig;
import com.huoli.openapi.util.Tools;

@Component
public class NewHotelStatusClient {
	@PropertyConfig("hotel.roomstatus.service")
	private String host;
	@PropertyConfig("hotel.roomstatus.service.port")
	private int port;
	@PropertyConfig("hotel.roomstatus.service.timeout")
	private int timeout;
	private String searchHotelByCache = "/hotelSource/roomStatus";
	private String refurbishByHotelId = "/hotelSource/refurbishByHotelId";
	@Resource
	private HotelRoomService hotelRoomService;
	private static final Logger logger = LoggerFactory.getLogger(NewHotelStatusClient.class);

	public NewHotelStatusClient(String host, int port, int timeout) {
		this.port = port;
		this.host = host;
		this.timeout = timeout;
	}

	public NewHotelStatusClient() {
	}

	/**
	 * 查询某个酒店房态信息，返回首日房态用于直接显示
	 * @param hotelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param hasCache
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public RoomStatusResult searchShowRoomByDays(String hotelType, String hotelId, String beginDate, String endDate,
			boolean hasCache, boolean isOfficial,String timeOut) throws Exception {
		long b = System.currentTimeMillis();
		RoomStatusResult result = hotelRoomService.searchHotelByDays(hotelType, hotelId, beginDate, endDate, hasCache,
				isOfficial,timeOut);
		logger.info("----->searchHotelByDays take time:" + (System.currentTimeMillis() - b));
		return result;
	}

	/**
	 * 查询某个酒店房态信息，返回每天房态
	 * @param hotelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param noCache
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public RoomStatusResult searchTotalRoomsByDays( String hotelType, String hotelId,
			String beginDate, String endDate, boolean hasCache, boolean isOfficial,String timeOut) throws Exception {
		long b = System.currentTimeMillis();
		RoomStatusResult result = hotelRoomService.searchRatePlans(hotelType, hotelId, beginDate, endDate,
				hasCache, isOfficial,timeOut);
		logger.info("----->searchHotelByDays take time:" + (System.currentTimeMillis() - b));
		return result;
	}
	
	public RoomStatusResult searchRoomPriceByDays(String channelType, String hotelId, String beginDate, String endDate,
			String userRank, String roomType) throws Exception {
		long b = System.currentTimeMillis();
		RoomStatusResult result = hotelRoomService.searchRoomPriceByDays(channelType, hotelId, beginDate, endDate,
				userRank, roomType);
		logger.info("----->searchRoomPriceByDays take time:" + (System.currentTimeMillis() - b));
		return result;
	}

	public RoomSimpleStatusResult searchHotelByCache(String query, String date) throws Exception {
		long b = System.currentTimeMillis();
		RoomSimpleStatusResult result = hotelRoomService.searchHotelByDate(query, date);
		logger.info("----->searchHotelByCache take time:" + (System.currentTimeMillis() - b));
		return result;
	}

	public String refurbishByHotelId(String hotelType, String hotelId, String beginDate, String endDate)
			throws JsonParseException, JsonMappingException, Exception {
		long b = System.currentTimeMillis();
		String url = this.refurbishByHotelId + "?" + "&hotelIds=" + hotelType + "_" + hotelId + "&startDate="
				+ beginDate + "&endDate=" + endDate;
		String rsp = this.executeMethod(url);

		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotelType=%s hotelId=%s,  beginDate=%s ,endDate =%s,return = |%s|", hotelType,
					hotelId, beginDate, endDate, rsp));
		}

		logger.info("----->searchCurrentTimeHotelStatus take time:" + (System.currentTimeMillis() - b));
		return rsp;
	}

	public SimpleRoomStatusResult searchCurrentTimeHotelStatus(String hotelType, String hotelId, String beginDate,
			String endDate) throws JsonParseException, JsonMappingException, Exception {
		long b = System.currentTimeMillis();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("hotelType", hotelType);
		paramMap.put("hotelId", hotelId);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		String url = this.searchHotelByCache + "?" + "hotelType=" + hotelType + "&hotelId=" + hotelId + "&beginDate="
				+ beginDate + "&endDate=" + endDate;
		String rsp = this.executeMethod(url);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotelType=%s hotelId=%s,  beginDate=%s ,endDate =%s,return = |%s|", hotelType,
					hotelId, beginDate, endDate, rsp));
		}
		SimpleRoomStatusResult result = SimpleRoomStatusResult.formJsonStr(rsp);
		logger.info("----->searchCurrentTimeHotelStatus take time:" + (System.currentTimeMillis() - b));
		return result;
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
	private String executeMethod(String url) throws OrderException {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, "http");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(this.timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(this.timeout);
		BufferedReader reader = null;
		HttpMethod method = null;
		try {
			method = new GetMethod(url);
			client.executeMethod(method);
			int statusCode = method.getStatusCode();
			String response = new String(method.getResponseBodyAsString().getBytes(), "utf-8");
			if (200 != statusCode) {
				throw new OrderException("hotel room system request is fail,statusCode=" + statusCode
						+ "----------,url=" + url);
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
				logger.error("{}", e);
			}
			if (null != method) {
				method.releaseConnection();
			}
		}
	}

	private String executePostMethod(String url, Map<String, String> paramMap) {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, "http");
		client.getParams().setIntParameter("http.socket.timeout", timeout);
		BufferedReader reader = null;
		PostMethod method = null;
		try {
			method = new PostMethod(url);
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			client.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : paramMap.keySet()) {
				NameValuePair pair = new NameValuePair(key, paramMap.get(key));
				paramList.add(pair);
			}
			method.setRequestBody(paramList.toArray(new NameValuePair[paramList.size()]));
			client.executeMethod(method);
			String response = new String(method.getResponseBodyAsString());
			response = new String(response.getBytes("utf-8"), "utf-8");
			return response;
		} catch (UnsupportedEncodingException e) {
			throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
					.append(" response translate fail ").append(e.toString()).toString());
		} catch (HttpException e) {
			throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
					.append(" HttpException ").append(e.toString()).toString());
		} catch (IOException e) {
			throw new HotelStatusException(new StringBuilder().append("executePostMethod can't link").append(url)
					.append(" IOException ").append(e.toString()).toString());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

			} catch (Exception e) {
				logger.error(Tools.exceptionMsg(e));
			}
			if (null != method) {
				method.releaseConnection();
			}
		}
	}

}
