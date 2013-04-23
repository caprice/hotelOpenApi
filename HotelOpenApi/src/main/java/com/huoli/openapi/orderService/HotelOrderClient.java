package com.huoli.openapi.orderService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.exception.NotFindRequiredParamException;
import com.huoli.openapi.exception.OrderException;
import com.huoli.openapi.help.PropertyConfig;

/**
 * 
 * @ClassName: HotelOrderClient
 * @Description: hotel order system client
 * @author dongj@huoli.com
 * @date 2012-2-3 上午10:14:34
 * 
 */
@Component
public class HotelOrderClient {
	@PropertyConfig("hotel.order.service")
	private String host;
	@PropertyConfig("hotel.order.service.port")
	private int port;
	private int connection_timeout = 60000;
	private String createOrderUrl = "/order/create";
	private String cencelOrderUrl = "/order/cancel";
	private String detailOrderUrl = "/order/detail";
	private String orderListUrl = "/order/show";
	private String deleteOrderurl = "/order/delete";
	private String orderListByDateUrl = "/order/queryByCreateTime";

	private Logger logger = LoggerFactory.getLogger(HotelOrderClient.class);

	public HotelOrderClient(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public HotelOrderClient(String host, int port, int connection_time) {
		this.port = port;
		this.host = host;
		this.connection_timeout = connection_time;
	}

	public HotelOrderClient() {

	}

	/**
	 * 创建订单
	 * 
	 * @param params
	 *            参数格式 json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ResultVo createOrder(Long uid, String hotelType, String hotelId,
			Map<String, String> extra, String params) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		JSONObject jsonObj = JSONObject.fromObject(params);
		Iterator<String> iterator = jsonObj.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			paramMap.put(key, jsonObj.getString(key));
		}
		paramMap.put("jsonSrc", params);
		if (uid == null || hotelType == null || hotelId == null) {
			throw new NotFindRequiredParamException("uid=" + uid
					+ ",hotelType=" + hotelType + ",hotelId=" + hotelId);
		}
		paramMap.put("uid", uid.toString());
		paramMap.put("hotelType", hotelType.toString());
		paramMap.put("hotelId", hotelId.toString());
		// 转换json字符串
		if (extra != null) {
			paramMap.put("extra", JSONObject.fromObject(extra).toString());
		}
		String rsp = this.executePostMethod(this.getCreateOrderUrl(), paramMap);
		if (logger.isInfoEnabled()) {
			logger.info(String
					.format("uid=%d ,  hotelType=%s ,hotelId = %s , params =%s ,extra= %s,return = %s ",
							uid, hotelType, hotelId, params,
							paramMap.get("extra"), rsp));
		}
		ResultVo result = (ResultVo) JSONObject.toBean(
				JSONObject.fromObject(rsp), ResultVo.class);
		return result;
	}
public static void main(String[] args) {
	String rsp = "{'description':'预订成功','orderId':'k130221015912257','outDesc':'','outOId':'6558686','proInfo':null,'resultCode':'00','status':'0'}";
	ResultVo result = (ResultVo) JSONObject.toBean(
			JSONObject.fromObject(rsp), ResultVo.class);
	System.out.println(result);
}
	/**
	 * 取消订单
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public ResultVo cancelOrder(Long uid, String orderId)
			throws UnsupportedEncodingException {
		if (uid == null) {
			throw new NotFindRequiredParamException(String.format(
					"uid=%d ,orderId=%s", uid, orderId));
		}
		StringBuffer paramss = new StringBuffer();
		paramss.append("uid=").append(uid.toString()).append("&orderId=")
				.append(orderId);
		String url = this.getCencelOrderUrl() + "?" + paramss.toString();
		String rsp = this.executeMethod(url);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("uid=%d ,  orderId=%s ,return = %s", uid,
					orderId, rsp));
		}
		ResultVo result = (ResultVo) JSONObject.toBean(
				JSONObject.fromObject(rsp), ResultVo.class);
		return result;
	}

	/**
	 * 订单列表
	 * 
	 * @param params
	 * @return
	 */
	public OrderListVo orderListBySinceId(Long uid, String sinceId, int pageSize) {
		OrderListVo orders = new OrderListVo();
		if (uid == null) {
			throw new NotFindRequiredParamException("uid=" + uid);
		}
		StringBuffer paramss = new StringBuffer();
		paramss.append("uid=").append(uid.toString()).append("&sinceId=")
				.append(sinceId).append("&pageSize=").append(pageSize);

		String url = this.getOrderListUrl() + "?" + paramss.toString();
		String rsp = this.executeMethod(url);
		List<OrderVo> result = new LinkedList<OrderVo>();
		JSONArray array = null;
		try {
			array = JSONObject.fromObject(rsp).getJSONArray("orders");
		} catch (Exception e) {
			throw new OrderException(
					"resolve json String is fail,character string of resolve is: ["
							+ rsp + "]");
		}

		for (int i = 0; i < array.size(); i++) {
			JSONObject orderJson = (JSONObject) array.get(i);
			if (orderJson != null && orderJson.toString().length() > 0) {
				OrderVo orderVo = (OrderVo) JSONObject.toBean(
						JSONObject.fromObject(orderJson), OrderVo.class);
				result.add(orderVo);
			}
		}
		String reSinceId = JSONObject.fromObject(rsp).getString("sinceId");
		orders.setOrders(result);
		orders.setSinceId(reSinceId);
		return orders;
	}

	/**
	 * 订单列表
	 * 
	 * @param params
	 * @return
	 */
	public List<OrderVo>  orderListByDate(String begin, String end, String appname) {
		

		StringBuffer paramss = new StringBuffer();
		paramss.append("beginDate=").append(begin).append("&endDate=")
				.append(end).append("&appname=").append(appname);

		String url = this.orderListByDateUrl + "?" + paramss.toString();
		String rsp = this.executeMethod(url);
		List<OrderVo> result = new LinkedList<OrderVo>();
		JSONArray array = null;
		try {
			array = JSONArray.fromObject(rsp);
		} catch (Exception e) {
			throw new OrderException(
					"resolve json String is fail,character string of resolve is: ["
							+ rsp + "]");
		}

		for (int i = 0; i < array.size(); i++) {
			JSONObject orderJson = (JSONObject) array.get(i);
			if (orderJson != null && orderJson.toString().length() > 0) {
				OrderVo orderVo = (OrderVo) JSONObject.toBean(
						JSONObject.fromObject(orderJson), OrderVo.class);
				result.add(orderVo);
			}
		}
	
		return result;
	}

	/**
	 * 获取订单详情
	 * 
	 * @param params
	 * @return
	 */
	public OrderDetailResult detailOrder(Long uid, String orderId) {
		if (uid == null) {
			throw new NotFindRequiredParamException(String.format(
					"uid=%d ,orderId=%s", uid, orderId));
		}
		StringBuffer paramss = new StringBuffer();
		paramss.append("uid=").append(uid.toString()).append("&orderId=")
				.append(orderId);
		String url = this.getDetailOrderUrl() + "?" + paramss.toString();
		System.out.println(url);
		String rsp = this.executeMethod(url);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("uid=%d ,  orderId= %s,return = %s", uid,
					orderId, rsp));
		}

		JSONObject jo = JSONObject.fromObject(rsp);
		OrderDetailResult result = new OrderDetailResult();
		result.setResultCode(jo.getString("stat"));
		result.setDescription(jo.getString("msg"));
		JSONObject orderJson = (JSONObject) jo.get("order");
		if (orderJson != null) {
			OrderVo vo = (OrderVo) JSONObject.toBean(
					JSONObject.fromObject(orderJson), OrderVo.class);
			result.setOrderVo(vo);
		}
		return result;
	}

	/**
	 * 删除订单
	 * 
	 * @param uid
	 * @param orderId
	 * @return
	 */
	public ResultVo deleteOrder(Long uid, String orderId) {
		if (uid == null) {
			throw new NotFindRequiredParamException(String.format(
					"uid=%d ,orderId=%s", uid, orderId));
		}
		StringBuffer paramss = new StringBuffer();
		paramss.append("uid=").append(uid.toString()).append("&orderId=")
				.append(orderId);
		String url = this.getDeleteOrderurl() + "?" + paramss.toString();
		String rsp = this.executeMethod(url);
		if (logger.isInfoEnabled()) {
			logger.info(String.format("uid=%d ,  orderId=%s ,return = %s", uid,
					orderId, rsp));
		}
		ResultVo result = (ResultVo) JSONObject.toBean(
				JSONObject.fromObject(rsp), ResultVo.class);
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
		logger.info("url=[{}]", url);
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, "http");
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(connection_timeout);
		client.getHttpConnectionManager().getParams()
				.setSoTimeout(connection_timeout);
		BufferedReader reader = null;
		HttpMethod method = null;
		try {
			method = new GetMethod(url);
			client.executeMethod(method);
			int statusCode = method.getStatusCode();
			String response = new String(method.getResponseBodyAsString()
					.getBytes(), "utf-8");
			if (200 != statusCode) {
				logger.error("hotel order system RETURN INFO ERROR!{} {}",
						method.getStatusCode(), response);
				throw new OrderException(
						"hotel order system request is fail,statusCode="
								+ statusCode);
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

	/**
	 * post方法的调用
	 * 
	 * @param url
	 * @param paramMap
	 *            post列表
	 * @return
	 * @throws NoAuthorizedException
	 */
	private String executePostMethod(String url, Map<String, String> paramMap)
			throws OrderException {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, "http");
		client.getParams().setContentCharset("utf-8");
		BufferedReader reader = null;
		PostMethod method = null;
		try {
			method = new PostMethod(url);
			method.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.getParams().setContentCharset("utf-8");
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(connection_timeout);
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(connection_timeout);

			// 构造post方法参数
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : paramMap.keySet()) {
				NameValuePair pair = new NameValuePair(key, paramMap.get(key));
				paramList.add(pair);
			}
			method.setRequestBody(paramList.toArray(new NameValuePair[paramList
					.size()]));
			client.executeMethod(method);
			String response = new String(method.getResponseBodyAsString()
					.getBytes("utf-8"), "utf-8");
			// response = new String(response.getBytes(), "utf-8");

			if (HttpServletResponse.SC_OK != method.getStatusCode()) {
				throw new OrderException(new StringBuilder()
						.append(method.getStatusCode()).append("|")
						.append(response).toString());
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
				throw new OrderException(e);
			}
			if (null != method) {
				method.releaseConnection();
			}
		}
	}

	

	public String getCreateOrderUrl() {
		return createOrderUrl;
	}

	public void setCreateOrderUrl(String createOrderUrl) {
		this.createOrderUrl = createOrderUrl;
	}

	public String getCencelOrderUrl() {
		return cencelOrderUrl;
	}

	public void setCencelOrderUrl(String cencelOrderUrl) {
		this.cencelOrderUrl = cencelOrderUrl;
	}

	public String getDetailOrderUrl() {
		return detailOrderUrl;
	}

	public void setDetailOrderUrl(String detailOrderUrl) {
		this.detailOrderUrl = detailOrderUrl;
	}

	public String getOrderListUrl() {
		return orderListUrl;
	}

	public void setOrderListUrl(String orderListUrl) {
		this.orderListUrl = orderListUrl;
	}

	public String getDeleteOrderurl() {
		return deleteOrderurl;
	}

	public void setDeleteOrderurl(String deleteOrderurl) {
		this.deleteOrderurl = deleteOrderurl;

	}
}