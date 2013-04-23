package com.huoli.openapi.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huoli.openapi.exception.ForbiddenException;
import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.service.OrderService;
import com.huoli.openapi.service.RoomService;
import com.huoli.openapi.util.Constant;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.result.BaseDataResult;
import com.huoli.openapi.vo.result.ChainResult;
import com.huoli.openapi.vo.result.CityListResult;
import com.huoli.openapi.vo.result.DetailResult;
import com.huoli.openapi.vo.result.HotelCollectionResult;
import com.huoli.openapi.vo.result.HotelRoomsResult;
import com.huoli.openapi.vo.result.OrderCreateResult;
import com.huoli.openapi.vo.result.OrderListResult;
import com.huoli.openapi.vo.result.OrderPrepeoccessResult;
import com.huoli.openapi.vo.result.SearchHotelsResult;
import com.huoli.openapi.vo.result.WapSearchHotelsResult;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * 
 * @项目名称：HotelOpenApi
 * @类名称：MainController
 * @类描述：
 * @创建人：dongjun
 * @创建时间：2012-10-31 下午12:13:35
 * @修改人：dongjun
 * @修改时间：2012-10-31 下午12:13:35
 * @修改备注：
 * @version v1.0
 * 
 */
@Controller
public class MainController {
	@Resource
	private RoomService roomService;
	@Resource
	private OrderService orderService;
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;

	/**
	 * 酒店搜索接口
	 * 
	 * @param request
	 * @param lat
	 *            经度
	 * @param lnt
	 *            纬度
	 * @param tudemode
	 *            经纬度类型 0－火星坐标 1－地球坐标 2－百度坐标
	 * @param hotelType
	 *            酒店类型
	 * @param radius
	 *            半径 默认25km
	 * @param date
	 *            日期
	 * @param count
	 *            返回数量
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(HttpServletRequest request, @RequestParam() String lat, @RequestParam() String lnt,
			@RequestParam(required = false) Integer tudemode, @RequestParam(required = false) String hotelType,
			@RequestParam(required = false) String radius, @RequestParam(required = false) String date,
			@RequestParam(required = false) Integer count,
			@RequestParam(value = "isbooking", required = false) Integer isbooking) {
		SearchHotelsResult result = roomService.doQueryConlinesHotels(request, lat, lnt, tudemode, hotelType, radius,
				date, count, isbooking);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 微信订酒店酒店搜索
	 * 
	 * @param request
	 * @param lat
	 * @param lnt
	 * @param tudemode
	 * @param hotelType
	 * @param radius
	 * @param date
	 * @param count
	 * @param placeName
	 * @param isbooking
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/wapsearch")
	public String searchByPlaceName(HttpServletRequest request, @RequestParam(required = false) String lat,
			@RequestParam(required = false) String lnt, @RequestParam(required = false) Integer tudemode,
			@RequestParam(required = false) String hotelType, @RequestParam(required = false) String radius,
			@RequestParam(required = false) String date, @RequestParam(required = false) Integer count,
			@RequestParam(required = false) String placeName, @RequestParam(required = false) String channelId,
			@RequestParam(value = "isbooking", required = false) Integer isbooking) throws UnsupportedEncodingException {
		placeName = new String(placeName.getBytes("8859_1"), "utf-8");
		WapSearchHotelsResult result = roomService.wapSearchHotels(request, lat, lnt, tudemode, hotelType, radius,
				date, count, isbooking, placeName);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 批量获取酒店状态
	 * 
	 * @param request
	 * @param checkinDate
	 * @param hotelIds
	 * @return
	 */
	@RequestMapping("/getRoomStatusBatch")
	public String getRoomsBatch(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String hotelIds) {
		SearchHotelsResult result = roomService.getHotelRoomStatusBatch(request, checkinDate, hotelIds);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 酒店多日房态查询,返回首日房态信息
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @return
	 */
	@RequestMapping(value = "/getRooms", method = RequestMethod.POST)
	public String getRooms(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate, @RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false, defaultValue = "1") String hasCache,
			@RequestParam(required = false, defaultValue = "0") String isOfficial) {
		hotelId = this.HotelIdChange(Long.valueOf(hotelId));
		HotelRoomsResult result = roomService.getHotelRoomStatusDetail(request, checkinDate, checkoutDate, hotelId,
				hotelType, hasCache,isOfficial);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 
	 * 获取价格计划，使用缓存数据
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @return
	 */
	@RequestMapping(value = "/hotelRatePlanByCache", method = RequestMethod.POST)
	public String getRoomsByCache(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate, @RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false, defaultValue = "0") String isOfficial) {
		hotelId = this.HotelIdChange(Long.valueOf(hotelId));
		String hasCache = "1";
		HotelRoomsResult result = roomService.getHotelRoomRatePlans(request, checkinDate, checkoutDate,
				hotelId, hotelType, hasCache, isOfficial);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 酒店多日房态查询,返回每日房态信息
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @param isOfficial
	 *            1:只取官网渠道数据，0:根据渠道优先级取有房房态
	 * @return
	 */
	@RequestMapping(value = "/hotelRatePlan", method = RequestMethod.POST)
	public String getHotelRatePlan(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate, @RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false, defaultValue = "0") String isOfficial) {
		hotelId = this.HotelIdChange(Long.valueOf(hotelId));
		String hasCache = "0";
		HotelRoomsResult result = roomService.getHotelRoomRatePlans(request, checkinDate, checkoutDate,
				hotelId, hotelType, hasCache,isOfficial);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @param nocache
	 * @return
	 */
	@RequestMapping(value = "/getRoomPrice", method = RequestMethod.POST)
	public String getCacheRoomsByDays(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate, @RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false, defaultValue = "1") String nocache) {
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @param nocache
	 * @return
	 */
	@RequestMapping(value = "/getRoomsByDays", method = RequestMethod.POST)
	public String getRoomsByDays(HttpServletRequest request, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate, @RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false, defaultValue = "1") String nocache) {
		return null;
	}

	/**
	 * 订单预提交处理，进行权限验证
	 * 
	 * @param request
	 * @param sName
	 * @param sMobile
	 * @param sEmail
	 * @param idCard
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @param roomId
	 * @param roomDesc
	 * @param pricesInfo
	 * @param roomCount
	 * @param extraInfo
	 * @return
	 */
	@RequestMapping(value = "/preprocess", method = RequestMethod.POST)
	public String preprocess(HttpServletRequest request, @RequestParam() String sName, @RequestParam() String sMobile,
			@RequestParam(required = false) String sEmail, @RequestParam() String idCard,
			@RequestParam() String checkinDate, @RequestParam() String checkoutDate, @RequestParam() String hotelId,
			@RequestParam() String hotelType, @RequestParam() String roomId, @RequestParam() String roomDesc,
			@RequestParam() String pricesInfo, @RequestParam(required = false) Integer roomCount,
			@RequestParam(required = false) String extraInfo) {
		hotelId = this.HotelIdChange(Long.valueOf(hotelId));
		if (roomId.indexOf("kj1") != -1) {
			roomId = roomId.replace("kj1", Constant.getSourceRoomDesc("kj1"));
		} else if (roomId.indexOf("kj2") != -1) {
			roomId = roomId.replace("kj2", Constant.getSourceRoomDesc("kj2"));
		}
		OrderPrepeoccessResult result = orderService.orderPreproccess(request, sName, sMobile, sEmail, idCard,
				checkinDate, checkoutDate, hotelId, hotelType, roomId, roomDesc, pricesInfo, roomCount, extraInfo);

		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 提交订单
	 * 
	 * @param request
	 * @param kid
	 * @param verify
	 * @return
	 */
	@RequestMapping(value = "/commit", method = RequestMethod.POST)
	public String commit(HttpServletRequest request, @RequestParam() String kid,
			@RequestParam(required = false) String verify) {
		OrderCreateResult result = orderService.orderCommit(request, kid, verify);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 再次发送验证码
	 * 
	 * @param request
	 * @param kid
	 * @return
	 */
	@RequestMapping(value = "/dSendVerify", method = RequestMethod.POST)
	public String againSendVerifyCode(HttpServletRequest request, @RequestParam() String kid) {
		BaseDataResult result = orderService.retransmissionVerifyCode(kid);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 获取城市列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCityList", method = RequestMethod.POST)
	public String getCityList(HttpServletRequest request) {
		CityListResult result = roomService.getCityList();
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 获取品牌列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChainList", method = RequestMethod.POST)
	public String getChainList(HttpServletRequest request) {
		ChainResult result = roomService.getChainList();
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 获取酒店列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getHotelList", method = RequestMethod.POST)
	String getHotelList(HttpServletRequest request) {
		Long channelId = (Long) request.getAttribute("channelId");
		HotelCollectionResult result = roomService.getHotelList(channelId);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 获取酒店列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getHotels", method = RequestMethod.POST)
	String getHotelLists(HttpServletRequest request) {
		Long channelId = (Long) request.getAttribute("channelId");
		HotelCollectionResult result = roomService.getHotelList(channelId);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 订单详情
	 * 
	 * @param request
	 * @param kid
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public String orderDetail2(HttpServletRequest request, @RequestParam() String kid) {
		DetailResult result = orderService.orderDetail((Long) request.getAttribute("channelId"), kid);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 订单详情
	 * 
	 * @param request
	 * @param kid
	 * @return
	 */
	@RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
	public String orderDetail(HttpServletRequest request, @RequestParam() String kid) {
		DetailResult result = orderService.orderDetail((Long) request.getAttribute("channelId"), kid);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 取消订单
	 * 
	 * @param request
	 * @param kid
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
	public String cancel(HttpServletRequest request, @RequestParam() String kid,
			@RequestParam(required = false) String reason) {
		BaseDataResult result = orderService.orderCancel((Long) request.getAttribute("channelId"), kid, reason);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 取消订单
	 * 
	 * @param request
	 * @param kid
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel2(HttpServletRequest request, @RequestParam() String kid,
			@RequestParam(required = false) String reason) {
		BaseDataResult result = orderService.orderCancel((Long) request.getAttribute("channelId"), kid, reason);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	/**
	 * 获取订单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderList", method = RequestMethod.POST)
	public String orderList(HttpServletRequest request, @RequestParam() String beginDate, @RequestParam() String endDate) {

		OrderListResult result = orderService.orderList((Long) request.getAttribute("channelId"), beginDate, endDate);
		request.setAttribute("msg", this.beanToXml(result));
		return "message";
	}

	public String beanToXml(Object object) {
		// 对象序列化
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		String xml = xstream.toXML(object);
		System.out.println(xml);
		return xml;
	}

	/**
	 * 酒店id转换
	 * 
	 * @param id
	 * @return
	 */
	private String HotelIdChange(Long id) {
		HotelInfoCollection hotel = hotelInfoCollectionMapper.selectById(id);
		if (hotel == null) {
			throw new ForbiddenException("酒店不存在");
		}
		return hotel.getHotelId();
	}
}
