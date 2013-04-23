package com.huoli.openapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huoli.openapi.cache.MemcachedClient;
import com.huoli.openapi.exception.OpenApiServiceException;
import com.huoli.openapi.exception.OrderException;
import com.huoli.openapi.exception.ParamTypeException;
import com.huoli.openapi.mapper.ChannelUserMapper;
import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.mapper.OrderVerifyMapper;
import com.huoli.openapi.mapper.UserInfoMapper;
import com.huoli.openapi.mapper.UserLoginInfoMapper;
import com.huoli.openapi.orderService.HotelOrderClient;
import com.huoli.openapi.orderService.OrderDetailResult;
import com.huoli.openapi.orderService.OrderVo;
import com.huoli.openapi.orderService.ResultVo;
import com.huoli.openapi.util.Constant;
import com.huoli.openapi.util.DateUtil;
import com.huoli.openapi.util.HLMD5;
import com.huoli.openapi.util.IDCard;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.data.ChannelUser;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.OrderVerify;
import com.huoli.openapi.vo.data.UserLoginInfo;
import com.huoli.openapi.vo.result.BaseDataResult;
import com.huoli.openapi.vo.result.DetailResult;
import com.huoli.openapi.vo.result.OrderCreateData;
import com.huoli.openapi.vo.result.OrderCreateResult;
import com.huoli.openapi.vo.result.OrderCreateVo;
import com.huoli.openapi.vo.result.OrderDetail;
import com.huoli.openapi.vo.result.OrderDetailData;
import com.huoli.openapi.vo.result.OrderListData;
import com.huoli.openapi.vo.result.OrderListResult;
import com.huoli.openapi.vo.result.OrderPrepeoccessResult;
import com.huoli.openapi.vo.result.OrderPreproccessData;

/**
 * 
 * 
 * @项目名称：HotelOpenApi
 * @类名称：OrderService
 * @类描述：
 * @创建人：dongjun
 * @创建时间：2012-11-2 下午7:53:21
 * @修改人：dongjun
 * @修改时间：2012-11-2 下午7:53:21
 * @修改备注：
 * @version v1.0
 * 
 */
@Component
public class OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderService.class);
	@Resource
	private UserLoginInfoMapper userLoginInfoMapper;
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;
	@Resource
	private OrderVerifyMapper orderVerifyMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private HotelOrderClient hotelOrderClient;
	@Resource
	private ChannelUserMapper channelUserMapper;
	@Resource
	private MemcachedClient memCachedClient;
	@Resource
	private ExtraInfoServer extraInfoServer;
	@Resource
	private Constant constant;

	/**
	 * 订单创建预提交，检测用户权限，如是快捷的老用户，直接通过，否则需要短信验证
	 * 
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
	 */
	public OrderPrepeoccessResult orderPreproccess(HttpServletRequest request,
			String sName, String sMobile, String sEmail, String idCard,
			String checkinDate, String checkoutDate, String hotelId,
			String hotelType, String roomId, String roomDesc,
			String pricesInfo, Integer roomCount, String extraInfo) {
		OrderPrepeoccessResult result = new OrderPrepeoccessResult();
		if(constant.getServiceStatus().equals("0")){
			if(!hotelType.equals("900")){
				result.setCode("49");
				result.setStatus(Constant.RESULT_STATUS_ERROR);
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setMsg("当前为测试环境，只能预定云端快捷品牌酒店!");
				return result;
			}
		}
		
		if (DateUtil.getDifferenceDays(checkinDate,
				DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) < 0) {
			result.setCode("46");
			result.setStatus(Constant.RESULT_STATUS_ERROR);
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setMsg("入住日期不能在今天之前!");
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate,
				DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) > 30) {
			result.setCode("47");
			result.setMsg("只能预定30天内的房间");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkoutDate,
				DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) > 30) {
			result.setCode("48");
			result.setMsg("只能预定30天内的房间");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (StringUtils.isBlank(sName) || !Tools.checkNick(sName)) {
			result.setCode("41");
			result.setStatus(Constant.RESULT_STATUS_ERROR);
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setMsg("姓名是2-4位汉字\r\n请检查输入是否正确!");
			return result;
		}

		if (!Tools.phoneNumberCheck(sMobile)) {
			result.setCode("42");
			result.setStatus(Constant.RESULT_STATUS_ERROR);
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setMsg(" 亲，你输入的电话号码不对~");
			return result;
		}

		IDCard cc = new IDCard();
		if (!cc.IDCardValidate(idCard)) {
			result.setCode("43");
			result.setStatus(Constant.RESULT_STATUS_ERROR);
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setMsg("亲，你输入的身份证号不对~");
			return result;
		}

		HotelInfoCollection query = new HotelInfoCollection();
		query.setHotelId(hotelId);
		query.setHotelType(hotelType);
		HotelInfoCollection hotel = hotelInfoCollectionMapper
				.selectByHotelId(query);
		if (hotel == null) {
			result.setCode("44");
			result.setStatus(Constant.RESULT_STATUS_ERROR);
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setMsg("酒店不存在");
			return result;
		}
		if (roomCount == null) {
			roomCount = 1;
		}
		Long channelId = (Long) request.getAttribute("channelId");

		UserLoginInfo userLoginInfo = userLoginInfoMapper
				.selectByLoginname(sMobile);

		OrderPreproccessData data = new OrderPreproccessData();
		OrderVerify order = new OrderVerify(sName, sMobile, sEmail, idCard,
				checkinDate, checkoutDate, hotelId, hotelType, roomId,
				roomDesc, pricesInfo, roomCount, extraInfo, channelId);

		if (userLoginInfo == null || !userLoginInfo.getIdcard().equals(idCard)
				|| !userLoginInfo.getNick().equals(sName)) {
			order.setUserStatus(1);
			String verifyCode = Tools.getVerifyCode();
			order.setStatus(1);
			order.setUserStatus(0);
			order.setVerify(verifyCode);
		} else {
			order.setStatus(0);
			order.setUserStatus(2);
		}
		if (userLoginInfo == null) {
			order.setUserStatus(0);
		}
		String kid = "k" + Tools.makeOrderId();
		order.setKid(kid);
		if (order.getVerify() != null) {
			String smsResult = SMSService.SendSMS(order.getMobile(),
					order.getVerify());
			if (!"".equals(smsResult)) {
				order.setVerifyStatus("1");
				logger.info(
						"succeed ,send message for user[{}],return is [{}]",
						order.getMobile(), smsResult);
			} else {
				order.setVerifyStatus("0");
				logger.info("fail,send message for user[{}],return is [{}]",
						order.getMobile(), smsResult);
			}
		}

		orderVerifyMapper.insert(order);
		data.setKid(kid);
		data.setStatus(order.getStatus());
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setData(data);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		result.setStatus(Constant.RESULT_STATUS_SUCCESS);
		return result;
	}

	/**
	 * 重发验证码
	 * 
	 * @param kid
	 * @return
	 */
	public BaseDataResult retransmissionVerifyCode(String kid) {
		BaseDataResult result = new BaseDataResult();
		OrderVerify order = orderVerifyMapper.selectOrderVerifyByKid(kid);
		if (order == null) {
			result.setCode("70");
			result.setMsg("订单不存在");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}
		if (order.getCreateTime().getTime() + 1000 * 60 * 30 < System
				.currentTimeMillis()) {
			result.setCode("71");
			result.setMsg("订单已过期");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}
		if (order.getStatus() == 2) {
			result.setCode("74");
			result.setMsg("此订单已完成");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}
		if (order.getVerify() != null) {
			String[] verifys = order.getVerify().split(",");
			if (verifys.length > 4) {
				result.setCode("75");
				result.setMsg("验证次数以达上限");
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
				return result;
			}
		}
		if (order.getVerify() == null) {
			result.setCode("72");
			result.setMsg("订单不需要验证");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		} else {
			String verifyCode = Tools.getVerifyCode();
			String smsResult = SMSService
					.SendSMS(order.getMobile(), verifyCode);
			if (!"".equals(smsResult)) {
				order.setVerifyStatus("1");
				logger.info(
						"succeed ,send message for user[{}],return is [{}]",
						order.getMobile(), smsResult);
				order.setVerify(order.getVerify() + "," + verifyCode);
				orderVerifyMapper.updateVerify(order);
			} else {
				order.setVerifyStatus("0");
				logger.info("fail,send message for user[{}],return is [{}]",
						order.getMobile(), smsResult);
			}
		}
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		result.setStatus(Constant.RESULT_STATUS_SUCCESS);
		return result;
	}

	/**
	 * 订单提交
	 * 
	 * @param kid
	 * @param verifyCode
	 * @return
	 */
	public OrderCreateResult orderCommit(HttpServletRequest request,
			String kid, String verifyCode) {
		OrderCreateResult result = new OrderCreateResult();
		OrderVerify order = orderVerifyMapper.selectOrderVerifyByKid(kid);
		
		if (order == null) {
			result.setCode("10");
			result.setMsg("订单不存在");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}
		if (order.getCreateTime().getTime() + 1000 * 60 * 30 < System
				.currentTimeMillis()) {
			result.setCode("11");
			result.setMsg("订单已过期");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (order.getStatus() == 2) {
			result.setCode("13");
			result.setMsg("订单已经生成，不能重复提交");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (order.getStatus() == 1) {
			if (StringUtils.isBlank(verifyCode)
					|| order.getVerify().indexOf(verifyCode) == -1) {
				result.setCode("12");
				result.setMsg("验证码错误");
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
				return result;
			}
		}
		result = this.createOrderToService(order);

		if (Constant.RESULT_CODE_SUCCESS.equals(result.getCode())) {
			order.setStatus(2);
			orderVerifyMapper.updateStatus(order);
		 ChannelUser cu = 	channelUserMapper.selectChannelUserInfo(order.getChannelId());
			UserLoginInfo newUser = this.getUserLoginInfo(request,
					order.getLinkman(), order.getMobile(), order.getEmail(),
					order.getIdcard(), cu.getName());
			UserLoginInfo oldUser = userLoginInfoMapper.selectByLoginname(order
					.getMobile());
			if (oldUser == null) {
				userLoginInfoMapper.insert(newUser);
			} else if (!newUser.getNick().equals(oldUser.getNick())
					|| !newUser.getIdcard().equals(oldUser.getIdcard())) {
				oldUser.setIdcard(newUser.getIdcard());
				oldUser.setNick(newUser.getNick());
				userLoginInfoMapper.update(oldUser);
			}
		}

		return result;
	}

	/**
	 * 取消订单
	 * 
	 * @param channelId
	 * @param kid
	 * @param reason
	 * @return
	 */
	public BaseDataResult orderCancel(Long channelId, String kid, String reason) {
		BaseDataResult result = new BaseDataResult();
		try {
			ResultVo sResult = hotelOrderClient.cancelOrder(
					Long.valueOf(channelId), kid);
			if (sResult.getResultCode().equals(Constant.RESULT_CODE_SUCCESS)) {
				result.setCode(Constant.RESULT_CODE_SUCCESS);
				result.setMsg(Constant.RESULT_MSG_SUCCESS);
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_SUCCESS);
			} else {
				result.setCode("41");
				result.setMsg(sResult.getDescription());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (NumberFormatException e) {
			throw new OpenApiServiceException();
		} catch (Exception e) {
			throw new OpenApiServiceException();
		}
		return result;
	}

	/**
	 * 获取订单详情
	 * 
	 * @param channelId
	 * @param kid
	 * @return
	 */
	public DetailResult orderDetail(Long channelId, String kid) {
		DetailResult result = new DetailResult();
		try {
			OrderDetailResult sResult = hotelOrderClient.detailOrder(channelId,
					kid);
			if (sResult.getResultCode().equals(Constant.RESULT_CODE_SUCCESS)) {
				OrderDetail order = new OrderDetail(sResult.getOrderVo());
				order.setHotelId(Constant.HOTELMAP.get(order.getHotelId() + "|"
						+ order.getHotelType()));
				result.setCode(Constant.RESULT_CODE_SUCCESS);
				result.setMsg(Constant.RESULT_MSG_SUCCESS);
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_SUCCESS);
				OrderDetailData data = new OrderDetailData();
				data.setOrder(order);
				result.setData(data);
			} else {
				result.setCode("41");
				result.setMsg(sResult.getDescription());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (NumberFormatException e) {
			throw new OpenApiServiceException();
		} catch (Exception e) {
			throw new OpenApiServiceException();
		}
		return result;
	}

	public OrderListResult orderList(Long channelId, String beginDate,
			String endDate) {
		OrderListResult result = new OrderListResult();
		List<OrderDetail> orders = new ArrayList<OrderDetail>();
		ChannelUser channel = channelUserMapper
				.selectChannelUserInfo(channelId);
		if (channel != null && channel.getName() != null) {
			List<OrderVo> orderResult = hotelOrderClient.orderListByDate(
					beginDate, endDate, channel.getName());
			if (orderResult != null && orderResult.size() > 0) {
				for (OrderVo item : orderResult) {
					OrderDetail order = new OrderDetail(item);
					orders.add(order);
				}
			}

		}
		OrderListData data = new OrderListData();
		if (orders != null && orders.size() > 0)
			data.setOrders(orders);
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setData(data);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		result.setStatus(Constant.RESULT_STATUS_SUCCESS);
		return result;
	}

	private Map<String, String> getExtraInfo(OrderVerify order) {
		return extraInfoServer.getCreateOrderExtraInfo(order);
	}
	
	

	/**
	 * 调用订单服务器生成订单
	 * 
	 * @param order
	 */
	private OrderCreateResult createOrderToService(OrderVerify order) {
		OrderCreateResult result = new OrderCreateResult();
		HotelInfoCollection query = new HotelInfoCollection();
		query.setHotelId(order.getHotelId());
		query.setHotelType(order.getHotelType());
		HotelInfoCollection hotel =hotelInfoCollectionMapper.selectByHotelId(query);
		
		Map<String, String> extraParams = new HashMap<String, String>();
		String hotelType=order.getHotelType();
		String hotelId = order.getHotelId();
		String roomId = order.getRoomType();
		if(order.getRoomType().indexOf("|")!=-1){
			String roomTypeInfos[] = order.getRoomType().split("\\|");
			if(roomTypeInfos.length==3){
				hotelType = roomTypeInfos[0];
				hotelId = roomTypeInfos[1];
				roomId = roomTypeInfos[2];
			}
		}
		OrderCreateVo ocvo = new OrderCreateVo(order);
		if(hotel!=null){
			ocvo.setHotelName(hotel.getHotelPrefix()+"-"+hotel.getName());
		}
		ocvo.setRmType(roomId);
		JSONObject jsonObj = JSONObject.fromObject(ocvo);
		extraParams.put("otaHotelId", hotelId);
		extraParams.put("otaHotelType", hotelType);
		extraParams.put("oid", order.getKid());
		extraParams.put("idcard", order.getIdcard());
		Map<String,String> extraMap = this.getExtraInfo(order);
		if(extraMap!=null&&!extraMap.isEmpty()){
			extraParams.putAll(extraMap);	
		}
		ChannelUser user = channelUserMapper.selectChannelUserInfo(order
				.getChannelId());
		if (user != null) {
			extraParams.put("appname", user.getName());
		}
		try {
			ResultVo orderResult = hotelOrderClient.createOrder(
					Long.valueOf(order.getChannelId()), order.getHotelType(),
					order.getHotelId(), extraParams, jsonObj.toString());
			if (Constant.RESULT_CODE_SUCCESS
					.equals(orderResult.getResultCode())) {
				result.setCode(Constant.RESULT_CODE_SUCCESS);
				result.setMsg(Constant.RESULT_MSG_SUCCESS);
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_SUCCESS);
				OrderCreateData data = new OrderCreateData();
				data.setKid(order.getKid());
				data.setOid(orderResult.getOutOId());
				data.setStatus(orderResult.getStatus());
				result.setData(data);
			} else {
				result.setCode(orderResult.getResultCode());
				result.setMsg(orderResult.getDescription());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (NumberFormatException e) {
			throw new ParamTypeException("channelId is error!", e);
		} catch (Exception e) {
			throw new OrderException(e);
		}

		return result;

	}

	public static void main(String[] args) {
		OrderService os = new OrderService();
		OrderVerify order = new OrderVerify("dongj", "121", "email@163.om",
				"421081812", "2012-1-1", "2012", "2", "2323", "dd", "big bed",
				"43", 1, "dfdsf", 11l);
		// String s = os.createOrderToService(order);
		// System.out.println(s);
	}
	private  String getMd5Password(String loginname) {
        return HLMD5.getMD5(HLMD5.getMD5(loginname) + loginname);
    }
	private UserLoginInfo getUserLoginInfo(HttpServletRequest request,
			String sName, String sMobile, String sEmail, String idCard,
			String source) {
		UserLoginInfo user = new UserLoginInfo();
		user.setAppname(source);
		user.setEmail(sEmail);
		user.setIdcard(idCard);
		user.setLoginname(sMobile);
		user.setSource(source);
		user.setNick(sName);
		user.setPassword(this.getMd5Password(sMobile));
		user.setRegTime(new Date());
		Object imei = request.getAttribute("imei");
		Object platForm = request.getAttribute("platForm");
		Object ptype = request.getAttribute("ptype");

		if (imei != null) {
			user.setImei((String) imei);
		} else {
			user.setImei(sMobile);
		}
		if (platForm != null) {
			user.setPlatform((String) platForm);
		} else {
			user.setPlatform("");
		}

		if (ptype != null) {
			user.setPtype((String) ptype);
		} else {
			user.setPtype("");
		}

		return user;
	}

	// private UserInfo getUserInfo(HttpServletRequest request) {
	// UserInfo user = new UserInfo();
	// user.setCtype(this.SOURCE);
	// user.setRegTime(new Date());
	// user.setSource(this.SOURCE);
	// Object imei = request.getAttribute("imei");
	// Object platForm = request.getAttribute("platForm");
	// Object ptype = request.getAttribute("ptype");
	// if (imei != null) {
	// user.setImei((String) imei);
	// }
	// if (platForm != null) {
	// user.setPlatform((String) platForm);
	// }
	//
	// if (ptype != null) {
	// user.setPtype((String) ptype);
	// }
	// return user;
	// }
}
