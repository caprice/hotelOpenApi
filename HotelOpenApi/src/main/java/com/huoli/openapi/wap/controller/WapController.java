package com.huoli.openapi.wap.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huoli.openapi.exception.ForbiddenException;
import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.mapper.OrderVerifyMapper;
import com.huoli.openapi.mapper.UserLoginInfoMapper;
import com.huoli.openapi.mapper.UserWxInfoMapper;
import com.huoli.openapi.mapper.UserWxTokenMapper;
import com.huoli.openapi.roomStatus.RoomStatus;
import com.huoli.openapi.service.OrderService;
import com.huoli.openapi.service.RoomService;
import com.huoli.openapi.util.DateUtil;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.OrderVerify;
import com.huoli.openapi.vo.data.UserLoginInfo;
import com.huoli.openapi.vo.data.UserWxInfo;
import com.huoli.openapi.vo.data.UserWxToken;
import com.huoli.openapi.vo.result.HotelRoomsData;
import com.huoli.openapi.vo.result.HotelRoomsResult;
import com.huoli.openapi.vo.result.OrderCreateResult;
import com.huoli.openapi.vo.result.OrderPrepeoccessResult;
import com.huoli.openapi.vo.result.OrderPreproccessData;
import com.huoli.openapi.vo.result.RoomStatusData;
import com.huoli.openapi.wap.vo.WapRoomStatus;

@Controller
public class WapController {
	@Resource
	private RoomService roomService;
	@Resource
	private OrderService orderService;
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;
	@Resource
	private UserWxTokenMapper userWxTokenMapper;
	@Resource
	private UserLoginInfoMapper userLoginInfoMapper;
	@Resource
	private OrderVerifyMapper orderVerifyMapper;
	@Resource
	private UserWxInfoMapper userWxInfoMapper;

	private Logger logger = LoggerFactory.getLogger(WapController.class);

	@RequestMapping("/wap/showroom")
	public String roomShow(HttpServletRequest request,
			@RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam(required = false) String date,@RequestParam(required = false,defaultValue="0") String nocache) {
		request.getSession().setAttribute("hotelType", hotelType);
		request.getSession().setAttribute("hotelId", hotelId);
		HotelInfoCollection hotelBase = roomService.getHotelInfoById(hotelId);
		if (hotelBase == null) {
			request.setAttribute("msg", " 酒店不存在");
			return "error";
		}

		String begin = DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD);
		if (date != null && !date.isEmpty()
				&& DateUtil.getDifferenceDays(date, begin) > 0) {
			begin = date;
		}
		String end = DateUtil.formatDate(DateUtil.addDay(new Date(), 1),
				DateUtil.YYYY_MM_DD);
		HotelRoomsResult result = roomService
				.getHotelRoomStatusDetail(request, begin, end,
						hotelBase.getHotelId(), hotelType,nocache,"0");
		HotelRoomsData data = result.getData();
		if (data == null) {
			logger.info(result.toString());
		}
		List<RoomStatusData> rooms = data.getRooms();
		RoomStatusData roomsData = null;
		if (rooms != null && !rooms.isEmpty()) {
			roomsData = rooms.get(0);
		}

		List<RoomStatus> roomList = new LinkedList<RoomStatus>();
		if (roomsData == null || roomsData.getRooms() == null
				|| roomsData.getRooms().isEmpty()) {
			request.setAttribute("msg", " 无房态");
			return "error";
		}
		for (RoomStatus rs : roomsData.getRooms()) {
			if (rs.getStatus() == 1)
				roomList.add(rs);
		}
		if (roomList.isEmpty()) {
			request.setAttribute("msg", " 无房态");
			return "error";
		}

		WapRoomStatus hotelRoom = new WapRoomStatus();
		hotelRoom.setHotelName(hotelBase.getName());
		hotelRoom.setHotelBrand(hotelBase.getHotelPrefix());
		if (hotelBase.getAddress() != null) {
			String add = hotelBase.getAddress().split("\\(")[0];
			add = hotelBase.getAddress().split("\\（")[0];
			hotelRoom.setAdd(add);
		} else {
			hotelRoom.setAdd(hotelBase.getAddress());
		}

		hotelRoom.setStartDate(begin);
		hotelRoom.setEndDate(end);
		hotelRoom.setHotelId(hotelId);
		hotelRoom.setHotelType(hotelType);
		String beginDate = DateUtil.formatDate(
				DateUtil.toDate(begin, DateUtil.YYYY_MM_DD), "MM/dd");
		String endDate = DateUtil.formatDate(
				DateUtil.toDate(end, DateUtil.YYYY_MM_DD), "MM/dd");
		hotelRoom.setRooms(roomList);
		request.setAttribute("hotelRoom", hotelRoom);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		String uid = (String) request.getSession().getAttribute("uid");
		if (uid != null && !uid.isEmpty()) {
			UserLoginInfo user = userLoginInfoMapper.selectByUid(uid);
			if (user != null) {
				request.setAttribute("tel", user.getLoginname());
				request.setAttribute("nick", user.getNick());
				request.setAttribute("idcard", user.getIdcard());
			}
		}
		Long channelId = (Long) request.getSession().getAttribute("channelId");
		if (channelId == null) {
			channelId = 10001205l;
		}
		request.setAttribute("channelId", channelId);
		request.getSession().setAttribute("channelId", channelId);
		return "roomchoose";
	}

	/**
	 * 
	 * @param request
	 * @param token
	 * @return
	 */
	@RequestMapping("/wap/book")
	public String bookEntry(HttpServletRequest request,
			@RequestParam() String token) {
		UserWxToken wxUser = userWxTokenMapper.selectByToken(token);
		if (wxUser == null) {
			return "error";
		}
		request.getSession().setAttribute("wxUser", wxUser);
		String data = wxUser.getReqdata();
		JSONObject src = JSONObject.fromObject(data);
		Map<String, String> params = new HashMap();
		Iterator<String> iterator = src.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			params.put(key, src.getString(key));
		}
		String hotelId = params.get("hid");
		String hotelType = params.get("htype");
		String channelid = params.get("channelid");
		String uid = params.get("loginid");
		String date = params.get("date");
		if (date == null) {
			date = "";
		}
		if (channelid != null && !channelid.isEmpty()) {
			Long channelId = Long.valueOf(channelid);
			request.getSession().setAttribute("channelId", channelId);
		}
		request.getSession().removeAttribute("uid");
		if (uid != null && !uid.isEmpty()) {
			request.getSession().setAttribute("uid", uid);
		}

		return "redirect:/wap/showroom?hotelId=" + hotelId + "&hotelType="
				+ hotelType + "&date=" + date;
	}

	@RequestMapping(value="/wap/preprocess",method=RequestMethod.POST)
	public String preprocess(HttpServletRequest request,
			@RequestParam() String name, @RequestParam() String mobile,
			@RequestParam(required = false) String email,
			@RequestParam() String idCard, @RequestParam() String checkinDate,
			@RequestParam() String checkoutDate,
			@RequestParam() String hotelId, @RequestParam() String hotelType,
			@RequestParam() String roomInfo,
			@RequestParam(required = false) Integer roomCount,
			@RequestParam(required = false) String extraInfo) {
		Long channelId = (Long) request.getSession().getAttribute("channelId");
		request.setAttribute("channelId", channelId);
//		try {
//			roomInfo = new String(roomInfo.getBytes("ISO-8859-1"), "utf-8");
//			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			request.setAttribute("msg", "价格参数错误");
//			return "error";
//		}
		String roomId = null;
		String roomDesc = null;
		String pricesInfo = null;
		if (roomInfo != null && roomInfo.split(",").length == 3) {
			String[] rinfo = roomInfo.split(",");
			roomId = rinfo[0];
			roomDesc = rinfo[1];
			pricesInfo = rinfo[2];
		} else {
			request.setAttribute("msg", "价格参数错误");
			return "error";
		}
		HotelInfoCollection hotelBase = roomService.getHotelInfoById(hotelId);
		OrderPrepeoccessResult result = orderService.orderPreproccess(request,
				name, mobile, email, idCard, checkinDate, checkoutDate,
				hotelBase.getHotelId(), hotelType, roomId, roomDesc,
				pricesInfo, roomCount, extraInfo);
		if (result.getCode().equals("00")) {
			OrderPreproccessData data = result.getData();
			if (data.getStatus() == 1) {

				request.setAttribute("hotelName", hotelBase.getName());
				request.setAttribute("hotelBrand", hotelBase.getHotelPrefix());
				if (hotelBase.getAddress() != null) {
					String add = hotelBase.getAddress().split("\\(")[0];
					add = hotelBase.getAddress().split("\\（")[0];
					request.setAttribute("add", add);
				} else {
					request.setAttribute("add", hotelBase.getAddress());
				}
				request.setAttribute("kid", data.getKid());
				request.setAttribute("hotelType", hotelType);
				request.setAttribute("hotelId", hotelId);
				request.getSession().setAttribute("kid", data.getKid());
				return "verify";
			} else {
				request.getSession().setAttribute("kid", data.getKid());
				return "redirect:/wap/commit?kid=" + data.getKid()+"&hotelType="+hotelType+"&hotelId="+hotelId;
			}
		} else {
			request.setAttribute("msg", result.getMsg());
			return "error";
		}

	}

	@RequestMapping(value="/wap/commit",method=RequestMethod.GET)
	public String commit(HttpServletRequest request,
			@RequestParam() String kid,
			@RequestParam(required = false) String verify,@RequestParam(required = false) String hotelType,@RequestParam(required = false) String hotelId) {
		Long channelId = (Long) request.getSession().getAttribute("channelId");
		request.setAttribute("channelId", channelId);
		String skid = (String) request.getSession().getAttribute("kid");
		if(!kid.equals(skid)){
			if(hotelType!=null&&hotelId!=null){
				return "redirect:/wap/showroom?hotelId=" + hotelId + "&hotelType="
						+ hotelType;
			}
			return "error";
		}
		OrderCreateResult result = orderService.orderCommit(request, kid,
				verify);
		if (result.getCode().equals("00")) {
			String uid = (String) request.getSession().getAttribute("uid");
			logger.info("commit succeed,uid="+uid);
			if (uid == null) {
				UserWxInfo user = new UserWxInfo();
				UserWxToken token = (UserWxToken) request.getSession()
						.getAttribute("wxUser");
				if (token != null) {
					OrderVerify order = orderVerifyMapper
							.selectOrderVerifyByKid(kid);
					user.setWxuser(token.getFromUser());
					user.setBusitype("wxwap");
					user.setLastusedtime(new Date());
					user.setPhone(order.getMobile());
					user.setStatus(1);
					user.setUserlevel(0);
					user.setBusidata("");
					user.setRegtime(DateUtil.getCurrentDate());
				UserWxInfo u=	userWxInfoMapper.select(token.getFromUser());
				if(u==null)
					userWxInfoMapper.insert(user);
				}
			}
		}
		request.getSession().removeAttribute("kid");
		request.setAttribute("status", result.getCode());
		request.setAttribute("msg", result.getMsg());
		return "commit";
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
