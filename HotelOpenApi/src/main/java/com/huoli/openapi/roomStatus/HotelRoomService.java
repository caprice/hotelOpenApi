package com.huoli.openapi.roomStatus;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huoli.openapi.cache.HotelCachedClient;
import com.huoli.openapi.dao.HotelRatePlanDao;
import com.huoli.openapi.mapper.HotelChainChannelMappingMapper;
import com.huoli.openapi.mapper.HotelInfoMappingMapper;
import com.huoli.openapi.mapper.HotelRatePlanMapper;
import com.huoli.openapi.util.DateUtil;
import com.huoli.openapi.util.JsonBinder;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.data.HotelChainChannelMapping;
import com.huoli.openapi.vo.data.HotelInfoMapping;
import com.huoli.openapi.vo.data.HotelRatePlan;
import com.huoli.openapi.vo.data.RatePlan2Query;
import com.huoli.openapi.vo.data.RatePlanQuery;

@Component
public class HotelRoomService {
	@Resource
	private HotelChainChannelMappingMapper hotelChainChannelMappingMapper;
	@Resource
	private HotelInfoMappingMapper hotelInfoMappingMapper;
	@Resource
	private HotelRatePlanMapper hotelRatePlanMapper;
	@Autowired
	private HotelRatePlanDao hotelRatePlanDao;
	@Autowired
	private NewHotelStatusClient hotelStatusClient;
	@Autowired
	private HotelCachedClient memcachedClient;
	private Logger logger = LoggerFactory.getLogger(HotelRoomService.class);

	/**
	 * 根据日期查询具体房型信息,多日返回汇总统计后的首日展现房态
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param noCache
	 * @param timeOut
	 * @return
	 */
	public RoomStatusResult searchHotelByDays(String hotelType, String hotelId, String beginDate, String endDate,
			boolean hasCache, boolean isOfficial,String timeOut) {
		logger.info("--__--start searchHotelByDays----[hotelType=" + hotelType + ",hotelId=" + hotelId);
		RoomStatusResult result = new RoomStatusResult();
		List<HotelRooms> data = new LinkedList<HotelRooms>();
		HotelRooms room = new HotelRooms();
		room.setHotelId(hotelId);
		room.setHotelType(hotelType);
		List<RoomStatus> rooms = new LinkedList<RoomStatus>();
		int days = DateUtil.getDifferenceDays(endDate, beginDate);
		List<HotelRatePlan> plans = this.getHotelRatePlanByDates(hotelType, hotelId, beginDate, endDate, hasCache,isOfficial,
				timeOut);

		List<HotelRatePlan> firstDayPlans  = new LinkedList<HotelRatePlan>();
		Map<String ,List<HotelRatePlan>> roomsMap = new HashMap<String ,List<HotelRatePlan>>();
		if(days>1){
			for(HotelRatePlan item:plans){
				if(item.getDate().equals(beginDate)){
					firstDayPlans.add(item);
				}
				if(roomsMap.get(item.getRoomType())!=null){
					roomsMap.get(item.getRoomType()).add(item);
				}else{
					List<HotelRatePlan> iPlans  = new LinkedList<HotelRatePlan>();
					iPlans.add(item);
					roomsMap.put(item.getRoomType(), iPlans);
				}
			}
		}else{
			firstDayPlans = plans;
		}
		
		
		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotelType=%s hotelId=%s,  beginDate=%s,endDate=%s return = |%s|", hotelType,
					hotelId, beginDate, endDate, plans));
		}
		if (firstDayPlans != null && !firstDayPlans.isEmpty()) {
			for (HotelRatePlan item : firstDayPlans) {
				if (item.getPrivceMember() < 0) {
					logger.error("hotel rate plan price is error,plan is [{}]", item);
					continue;
				}
				if ("未知房型".equals(item.getRoomTypeDesc())) {
					logger.error("hotel rate plan roomDesc is error,plan is [{}]", item);
					continue;
				}
				if ("016".equals(item.getChannelType()) && item.getRoomTypeDesc().indexOf("119专享") != -1) {
					logger.error("hotel rate plan roomDesc is error,plan is [{}]", item);
					continue;
				}
				String prices = String.valueOf(item.getPrivceMember());
				if(days>1){
					List<HotelRatePlan> iRoomPlans = roomsMap.get(item.getRoomType());
					if(iRoomPlans.size()!=days){
						continue;
					}
					if(iRoomPlans!=null&&!iRoomPlans.isEmpty()){
						prices="";
						for(HotelRatePlan planItem:iRoomPlans){
							prices=prices+planItem.getPrivceMember()+",";
							if(planItem.getStatus()==0){
								item.setStatus(0);
							}
						}
						if(prices.length()>1){
							prices = prices.substring(0,prices.length()-1);
						}
					}
				}
				RoomStatus cRoom = this.convert(item, hotelId, hotelType);
				cRoom.setPriceCollect(prices);
				rooms.add(cRoom);
			}
		}
		if (rooms.isEmpty()) {
			room.setRooms(rooms);
			result.setResult("0");
			result.setMsg("查询失败");
		} else {
			room.setRooms(rooms);
			room.setHotelId(hotelId);
			room.setHotelType(hotelType);
			room.setRooms(rooms);
			data.add(room);
			result.setResult("1");
			result.setMsg("查询成功");
			result.setData(data);
		}
		logger.info("--__--end searchHotelByDays----[hotelType=" + hotelType + ",hotelId=" + hotelId + ",result="
				+ result);
		return result;
	}

	
	

	/**
	 * 根据日期查询具体房型信息,多日返回汇总统计后的首日展现房态
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param noCache
	 * @param timeOut
	 * @return
	 */
	public RoomStatusResult searchRatePlans(String hotelType, String hotelId, String beginDate, String endDate,
			boolean hasCache,boolean isOfficial, String timeOut) {
		logger.info("--__--start searchHotelByDays----[hotelType=" + hotelType + ",hotelId=" + hotelId);
		RoomStatusResult result = new RoomStatusResult();
		List<HotelRooms> data = new LinkedList<HotelRooms>();
		HotelRooms room = new HotelRooms();
		room.setHotelId(hotelId);
		room.setHotelType(hotelType);
		List<RoomStatus> rooms = new LinkedList<RoomStatus>();
		List<HotelRatePlan> plans = this.getHotelRatePlanByDates(hotelType, hotelId, beginDate, endDate, hasCache,isOfficial,
				timeOut);

	
		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotelType=%s hotelId=%s,  beginDate=%s,endDate=%s return = |%s|", hotelType,
					hotelId, beginDate, endDate, plans));
		}
		if (plans != null && !plans.isEmpty()) {
			for (HotelRatePlan item : plans) {
				if (item.getPrivceMember() < 0) {
					logger.error("hotel rate plan price is error,plan is [{}]", item);
					continue;
				}
				if ("未知房型".equals(item.getRoomTypeDesc())) {
					logger.error("hotel rate plan roomDesc is error,plan is [{}]", item);
					continue;
				}
				if ("016".equals(item.getChannelType()) && item.getRoomTypeDesc().indexOf("119专享") != -1) {
					logger.error("hotel rate plan roomDesc is error,plan is [{}]", item);
					continue;
				}
				
				RoomStatus cRoom = this.convert(item, hotelId, hotelType);
				cRoom.setPriceCollect(String.valueOf(item.getPrivceMember()));
				rooms.add(cRoom);
			}
		}
		if (rooms.isEmpty()) {
			room.setRooms(rooms);
			result.setResult("0");
			result.setMsg("查询失败");
		} else {
			room.setRooms(rooms);
			room.setHotelId(hotelId);
			room.setHotelType(hotelType);
			room.setRooms(rooms);
			data.add(room);
			result.setResult("1");
			result.setMsg("查询成功");
			result.setData(data);
		}
		logger.info("--__--end searchHotelByDays----[hotelType=" + hotelType + ",hotelId=" + hotelId + ",result="
				+ result);
		return result;
	}

	

	/**
	 * 查询某一天房态基本信息（仅限查询最近7天内某天）批量查询接口
	 * 
	 * @param query
	 * @param date
	 * @return
	 */
	public RoomSimpleStatusResult searchHotelByDate(String query, String date) {
		RoomSimpleStatusResult result = new RoomSimpleStatusResult();
		List<RoomSimpleStatus> data = new LinkedList<RoomSimpleStatus>();
		// 如果 0-4点则查询前一天房态,取缓存直接返回
		// String nowString = DateUtil.format(new Date(),
		// DateUtil.C_DATE_PATTON_DEFAULT);
		// if (nowString.equals(date)) {
		// Calendar calendar = Calendar.getInstance();
		// int hours = calendar.get(Calendar.HOUR_OF_DAY);
		// if (hours >= 0 && hours <= 4) {
		// date = DateUtil.format(DateUtil.addDays(new Date(), -1),
		// DateUtil.C_DATE_PATTON_DEFAULT);
		// }
		// }

		String[] hotels = query.split(",");
		for (String item : hotels) {
			String hotelType = item.substring(0, 3);
			String hotelId = item.substring(3);
			RoomSimpleStatus simpleStatus = this.searchRoomSimpleStatus(hotelType, hotelId, date);
			data.add(simpleStatus);
		}
		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotels=%s ,date=%s,return = |%s|", query, date, data.toString()));
		}
		result.setData(data);
		if (data != null && !data.isEmpty()) {
			result.setMsg("搜索成功");
			result.setResult("1");
		} else {
			result.setMsg("搜索失败");
			result.setResult("0");
		}
		return result;
	}

	/**
	 * 查询某一天房态基本信息（仅限查询最近7天内某天）
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param date
	 * @return
	 */
	public RoomSimpleStatus searchRoomSimpleStatus(String hotelType, String hotelId, String date) {
		RoomSimpleStatus simpleStatus = null;
		String key = "simple_" + hotelType + "_" + hotelId + "_" + date;
		Object value = memcachedClient.get(key);
		if (value == null) {
			List<HotelRatePlan> plans = this.getHotelRatePlanByDatesByChannelPrior(hotelType, hotelId, date);

			simpleStatus = this.convert(plans);
			simpleStatus.setHotelId(hotelId);
			simpleStatus.setHotelType(hotelType);
			memcachedClient.set(key, 30 * 60, simpleStatus);
			return simpleStatus;
		} else {
			if (value instanceof String) {
				JsonBinder binder = JsonBinder.buildNonDefaultBinder();
				try {
					simpleStatus = binder.getMapper().readValue((String) value, new TypeReference<RoomSimpleStatus>() {
					});
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(Tools.exceptionMsg(e));
				}
			}
			return simpleStatus;
		}

	}

	/**
	 * 从缓存获取房态数据
	 * 
	 * @param query
	 * @param date
	 * @return
	 */
	public RoomStatusResult searchHotelByDateFromCache(String query, String date) {
		RoomStatusResult result = new RoomStatusResult();
		List<HotelRooms> data = new LinkedList<HotelRooms>();

		String[] hotels = query.split(",");
		for (String item : hotels) {
			String hotelType = item.substring(0, 3);
			String hotelId = item.substring(3);
			HotelRooms hr = new HotelRooms();
			hr.setHotelId(hotelId);
			hr.setHotelType(hotelType);
			List<RoomStatus> rooms = new LinkedList<RoomStatus>();
			List<HotelRatePlan> plans = this.getHotelRatePlanByDatesByChannelPrior(hotelType, hotelId, date);
			if (plans != null) {
				for (HotelRatePlan plan : plans) {
					RoomStatus roomStatus = this.convert(plan, hotelId, hotelType);
					rooms.add(roomStatus);
				}
			}

			data.add(hr);
		}
		if (logger.isInfoEnabled()) {
			logger.info(String.format("hotels=%s ,date=%s,return = |%s|", query, date, data.toString()));
		}
		result.setData(data);
		if (data != null && !data.isEmpty()) {
			result.setMsg("搜索成功");
			result.setResult("1");
		} else {
			result.setMsg("搜索失败");
			result.setResult("0");
		}
		return result;
	}

	
	/**
	 * 价格计划转换为房态信息
	 * 
	 * @param plans
	 * @return
	 */
	public RoomStatus convert(HotelRatePlan plans, String hotelId, String hotelType) {
		RoomStatus status = new RoomStatus();
		status.setDate(plans.getDate());
		status.setDateTime(DateUtil.format(plans.getStatusUpdateTime(), DateUtil.C_TIME_PATTON_DEFAULT));
		status.setMarketPrice((int) plans.getPriceMarket());
		status.setMemberPrice((int) plans.getPrivceMember());
		status.setStatus(plans.getStatus());
		status.setType(plans.getRoomTypeDesc());
		String extraInfo = plans.getExtraInfo();
		Map<String, String> extraMap = new HashMap<String, String>();
		if (extraInfo != null) {
			if (!extraInfo.startsWith("{")) {
				extraMap.put("extra", extraInfo);
			} else {
				JSONObject jsonSrc = JSONObject.fromObject(extraInfo);
				Iterator iterator = jsonSrc.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = jsonSrc.getString(key);
					extraMap.put(key, value);
				}
			}

			status.setExtra(extraMap);
		}
		if (hotelType.equals(plans.getChannelType())) {
			status.setTypeid(plans.getRoomType());
		} else {
			if (plans.getChannelType().equals("elong")) {
				status.setTypeid("kj1" + "|" + plans.getHotelId() + "|" + plans.getRoomType());
			} else if (plans.getChannelType().equals("ctrip")) {
				status.setTypeid("kj2" + "|" + plans.getHotelId() + "|" + plans.getRoomType());
			}

		}

		return status;
	}

	/**
	 * 查询某个酒店某个房型某段时间内价格计划
	 * 
	 * @param channelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param userRank
	 * @param roomType
	 * @return
	 */
	public RoomStatusResult searchRoomPriceByDays(String channelType, String hotelId, String beginDate, String endDate,
			String userRank, String roomType) {
		RoomStatusResult roomsResult = new RoomStatusResult();
		List<HotelRooms> data = new LinkedList();
		HotelRooms hotelRooms = new HotelRooms();
		RatePlanQuery query = new RatePlanQuery();
		query.setBeginDate(beginDate);
		query.setEndDate(endDate);
		query.setChannelType(channelType);
		query.setHotelId(hotelId);
		query.setRoomType(roomType);
		List<HotelRatePlan> result = hotelRatePlanDao.selectByDates(query);
		List<RoomStatus> rooms = new LinkedList();
		for (HotelRatePlan plan : result) {
			RoomStatus room = this.convert(plan, hotelId, channelType);
			rooms.add(room);
		}
		hotelRooms.setHotelId(hotelId);
		hotelRooms.setHotelType(channelType);
		hotelRooms.setRooms(rooms);
		data.add(hotelRooms);
		roomsResult.setData(data);
		if (rooms != null && !rooms.isEmpty()) {
			roomsResult.setMsg("succeed");
			roomsResult.setResult("1");
		} else {
			roomsResult.setMsg("fail");
			roomsResult.setResult("0");
		}
		return roomsResult;
	}

	
	public RoomSimpleStatus convert(List<HotelRatePlan> plans) {
		RoomSimpleStatus simpleStatus = new RoomSimpleStatus();
		if (plans == null || plans.isEmpty()) {
			simpleStatus.setDateTime(DateUtil.getCurrentDateTimeStr());
			simpleStatus.setPrice(0);
			simpleStatus.setStatus(2);
			return simpleStatus;
		}
		Collections.sort(plans, new RatePlanPriceComparator());
		HotelRatePlan plan = plans.get(0);
		int i = 0;
		while (plan.getPrivceMember() <= 0) {
			if (i < plans.size() - 1) {
				i++;
			} else {
				break;
			}
			plan = plans.get(i);
		}
		simpleStatus.setDateTime(DateUtil.getCurrentDateTimeStr());
		simpleStatus.setPrice((int) plan.getPrivceMember());
		simpleStatus.setStatus(this.hasRoom(plans) ? 1 : 0);
		return simpleStatus;
	}

	/**
	 * 根据房价计划判断是否有房
	 * 
	 * @param plans
	 * @return
	 */
	private boolean hasRoom(List<HotelRatePlan> plans) {
		for (HotelRatePlan plan : plans) {
			if (plan.getStatus() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析判断是否需要担保
	 * 
	 * @param vouchSetContent
	 * @return
	 */
	private int resolveVouchSetInfo(String vouchSetContent) {
		return 0;
	}

	public Map<String, List<HotelRatePlan>> getHotelRatePlanOrderByDate(String hotelType, String hotelId,
			String startDate, String endDate) {

		return null;
	}

	/**
	 * 根据地图展示优先级渠道，返回优先级最高渠道酒店房态数据
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param date
	 * @return
	 */
	private List<HotelRatePlan> getHotelRatePlanByDaysByMapPrior(String hotelType, String hotelId, String date,
			String endDate) {
		List<HotelChainChannelMapping> channels = hotelChainChannelMappingMapper.select(hotelType);
		HotelInfoMapping query = new HotelInfoMapping();
		query.setHotelId(hotelId);
		query.setHotelType(hotelType);
		List<HotelInfoMapping> hotelMappings = hotelInfoMappingMapper.select(query);
		HotelChainChannelMapping channelMapping = null;
		for (HotelChainChannelMapping item : channels) {
			if (item.getFullflag() == 1) {
				channelMapping = item;
				break;
			}
		}
		try {
			if (hotelMappings != null && !hotelMappings.isEmpty() && channelMapping != null) {
				for (HotelInfoMapping item : hotelMappings) {
					if (item.getOutSrc().equals(channelMapping.getChannelId())) {
						RatePlan2Query planQuery = new RatePlan2Query();
						planQuery.setChannelType(item.getOutSrc());
						planQuery.setHotelId(item.getOutId());
						planQuery.setDate(date);
						planQuery.setEndDate(endDate);
						List<HotelRatePlan> ratePlans = hotelRatePlanMapper.select(planQuery);
						return ratePlans;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	/**
	 * 获取价格计划根据渠道优先级
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param date
	 * @return
	 */
	public List<HotelRatePlan> getHotelRatePlanByDatesByChannelPrior(String hotelType, String hotelId, String date) {
		List<HotelChainChannelMapping> channels = hotelChainChannelMappingMapper.select(hotelType);
		HotelInfoMapping query = new HotelInfoMapping();
		query.setHotelId(hotelId);
		query.setHotelType(hotelType);
		List<HotelInfoMapping> hotelMappings = hotelInfoMappingMapper.select(query);
		HotelChainChannelMapping channelMapping = null;
		List<HotelRatePlan> fullDefaultPlans = null;
		for (HotelChainChannelMapping citem : channels) {
			channelMapping = citem;
			try {
				if (hotelMappings != null && !hotelMappings.isEmpty() && channelMapping != null) {
					for (HotelInfoMapping item : hotelMappings) {
						if (item.getOutSrc().equals(channelMapping.getChannelId())) {
							RatePlan2Query planQuery = new RatePlan2Query();
							planQuery.setChannelType(item.getOutSrc());
							planQuery.setHotelId(item.getOutId());
							planQuery.setDate(date);
							List<HotelRatePlan> ratePlans = hotelRatePlanDao.select(planQuery);
							if (citem.getFullflag() == 1) {
								fullDefaultPlans = ratePlans;
							}
							if (ratePlans != null) {
								for (HotelRatePlan plan : ratePlans) {
									if (plan.getChannelType().equals("elong") || plan.getChannelType().equals("ctrip")) {
										if (this.isVouchSet(plan, date)) {
											plan.setStatus(0);
										}
									}
									if (plan.getStatus() == 1) {
										return ratePlans;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return fullDefaultPlans;
	}

	/**
	 * 多渠道路由选择返回j
	 * 
	 * @param hotelType
	 * @param hotelId
	 * @param beginDate
	 * @param endDate
	 * @param noCache
	 * @param timeOut
	 * @return
	 */
	public List<HotelRatePlan> getHotelRatePlanByDates(String hotelType, String hotelId, String beginDate,
			String endDate, boolean hasCache, boolean isOfficial,String timeOut) {
		List<HotelChainChannelMapping> channels = hotelChainChannelMappingMapper.select(hotelType);
		HotelInfoMapping query = new HotelInfoMapping();
		query.setHotelId(hotelId);
		query.setHotelType(hotelType);
		List<HotelInfoMapping> hotelMappings = hotelInfoMappingMapper.select(query);
		List<HotelRatePlan> ratePlansResult = null;
		List<HotelRatePlan> ratePlansTemp = null;
		int days = DateUtil.getDifferenceDays(endDate, beginDate) ;
		
		for (HotelChainChannelMapping channelMapping : channels) {
			if(isOfficial){
				if(!channelMapping.getChannelId().equals(hotelType)){
					continue;
				}
			}
			boolean flag = false;
			for (HotelInfoMapping item : hotelMappings) {
				if (item.getOutSrc().equals(channelMapping.getChannelId())) {
					List<HotelRatePlan> ratePlans = null;
					RatePlan2Query planQuery = new RatePlan2Query();
					planQuery.setChannelType(item.getOutSrc());
					planQuery.setHotelId(item.getOutId());
					planQuery.setDate(beginDate);
					planQuery.setEndDate(endDate);
					String refuFlag = null;
					if (days> 1) {// 多天调用刷新接口
						if (channelMapping.getFullflag() != 1) {
							continue;//多天查询返回地图展现渠道
						}
						logger.info("---get rooms from interface:beginDate=" + beginDate + ",endDate=" + endDate
								+ ",hotelType=" + item.getOutSrc() + ",hotelId=" + item.getOutId());
						try {
							refuFlag = hotelStatusClient.refurbishByHotelId(item.getOutSrc(), item.getOutId(),
									beginDate, endDate);
						} catch (Exception e) {
							logger.error("refurbishByHotelId is faill,planQuery=" + planQuery + ",exception="
									+ Tools.exceptionMsg(e));
						}
						logger.info("---interface rooms:d" + refuFlag);
					} else if (hasCache) {
						logger.info("---noCache get rooms from interface:beginDate=" + beginDate + ",endDate="
								+ endDate + ",hotelType=" + item.getOutSrc() + ",hotelId=" + item.getOutId());
						try {
							refuFlag = hotelStatusClient.refurbishByHotelId(item.getOutSrc(), item.getOutId(),
									beginDate, endDate);
							hotelRatePlanDao.delKeyObject(planQuery);
						} catch (Exception e) {
							logger.error("refurbishByHotelId is faill,planQuery=" + planQuery + ",exception="
									+ Tools.exceptionMsg(e));
						}
						logger.info("---interface rooms:d" + refuFlag);
					} else {
						ratePlans = hotelRatePlanDao.select(planQuery);
						if (ratePlans == null || ratePlans.isEmpty()) {// 单天数据库没有数据调用刷新接口
							logger.info("---get rooms from interface:beginDate=" + beginDate + ",endDate=" + endDate
									+ ",hotelType=" + item.getOutSrc() + ",hotelId=" + item.getOutId());
							try {
								refuFlag = hotelStatusClient.refurbishByHotelId(item.getOutSrc(), item.getOutId(),
										beginDate, endDate);
							} catch (Exception e) {
								logger.error("refurbishByHotelId is faill,planQuery=" + planQuery + ",exception="
										+ Tools.exceptionMsg(e));
							}
							logger.info("---interface rooms:" + refuFlag);

						} else {
							boolean isTimeOut = this.isTimeOut(ratePlans);
							if (isTimeOut) {// 数据库有数据但过期，调用刷新接口
								try {
									refuFlag = hotelStatusClient.refurbishByHotelId(item.getOutSrc(), item.getOutId(),
											beginDate, endDate);
								} catch (Exception e) {
									logger.error("refurbishByHotelId is faill,planQuery=" + planQuery + ",exception="
											+ Tools.exceptionMsg(e));
								}
								hotelRatePlanDao.delKeyObject(planQuery);
								ratePlans = null;
							}
						}
					}
					if (ratePlans == null || ratePlans.isEmpty()) {
						ratePlans = hotelRatePlanDao.select(planQuery);
					}
					if (channelMapping.getFullflag() == 1) {
						ratePlansTemp = ratePlans;// 保存地图展示房态信息
					}
					ratePlansResult = ratePlans;
					break;
				}
			}
			if (ratePlansResult != null && !ratePlansResult.isEmpty())
				for (HotelRatePlan item : ratePlansResult) {
					if (!item.getChannelType().equals("002")) {
						if (item.getStatusUpdateTime() == null
								|| new Date().getTime() - item.getStatusUpdateTime().getTime() > 1000 * 60 * 480) {
							item.setStatus(0);
							continue;
						}
						if (item.getChannelType().equals("elong") || item.getChannelType().equals("ctrip")) {
							if (this.isVouchSet(item, beginDate)) {
								item.setStatus(0);
							}
						}
					}

				}
			flag = this.isCanOrder(ratePlansResult);
			if (flag) {
				break;
			}

		}
		if (ratePlansResult == null || ratePlansResult.isEmpty()) {
			ratePlansResult = ratePlansTemp;
		}
		return ratePlansResult;
	}

	private boolean isCanOrder(List<HotelRatePlan> ratePlans) {
		if (ratePlans == null) {
			return false;
		}
		for (HotelRatePlan plan : ratePlans) {
			if (plan.getStatus() == 1) {
				return true;
			}
		}
		return false;
	}


	private boolean isTimeOut(List<HotelRatePlan> plans) {
		if (plans == null || plans.isEmpty()) {
			return true;
		}
		if ("002".equals(plans.get(0).getChannelType()) || plans.get(0).getStatusUpdateTime() == null) {
			logger.info("plan info is error,plan={}", plans.get(0));
			return false;
		}
		long time = new Date().getTime() - plans.get(0).getStatusUpdateTime().getTime();
		if (time > 1000 * 60 * 30 || time < 0) {
			return true;
		}
		return false;
	}

	public SimpleRoomStatusResult getCurrentRoomStatus(String beginDate, String endDate, String hotelType,
			String hotelId) {
		SimpleRoomStatusResult result = null;
		try {
			result = hotelStatusClient.searchCurrentTimeHotelStatus(hotelType, hotelId, beginDate, endDate);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private HotelRatePlan getMaxHotelRatePlan(List<HotelRatePlan> plans) {
		Collections.sort(plans, new RatePlanComparator());
		return plans.get(0);
	}

	/**
	 * 判断ota来的房型能否订房
	 * 
	 * @param list
	 * @return
	 */
	private boolean isVouchSet(List<RoomStatus> list, String beginDate) {
		if (list != null) {
			for (RoomStatus hotelRoom : list) {
				String vouchSet = hotelRoom.getExtra().get("vouchSet");
				if ("1".equals(vouchSet)) {
					if (hotelRoom.getExtra() != null && hotelRoom.getExtra().containsKey("vouchTime")
							&& !isVouchTime(beginDate, hotelRoom.getExtra().get("vouchTime"))) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断ota来的房型能否订房
	 * 
	 * @param list
	 * @return
	 */
	private boolean isVouchSet(RoomStatus hotelRoom, String beginDate) {
		if (hotelRoom != null) {
			String vouchSet = hotelRoom.getExtra().get("vouchSet");
			if ("1".equals(vouchSet)) {
				if (hotelRoom.getExtra() != null && hotelRoom.getExtra().containsKey("vouchTime")
						&& !isVouchTime(beginDate, hotelRoom.getExtra().get("vouchTime"))) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断ota来的房型能否订房
	 * 
	 * @param list
	 * @return
	 */
	private boolean isVouchSet(HotelRatePlan plan, String beginDate) {
		if (plan != null && plan.getExtraInfo() != null) {
			JSONObject jsonSrc = JSONObject.fromObject(plan.getExtraInfo());
			Iterator iterator = jsonSrc.keys();
			String vouchSet = null;
			String vouchTime = null;
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = jsonSrc.getString(key);
				if ("vouchSet".equals(key)) {
					vouchSet = value;
				} else if ("vouchTime".equals(key)) {
					vouchTime = value;
				}
			}
			if ("1".equals(vouchSet)) {
				if (vouchTime != null && !isVouchTime(beginDate, vouchTime)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否担保时间 // 18:00-24:00
	 * 
	 * @param vouchTime
	 * @return
	 */
	public boolean isVouchTime(String queryDate, String vouchTime) {
		if ("00:00-24:00".equals(vouchTime)) {
			return true;
		}
		if (StringUtils.isBlank(vouchTime) || DateUtil.compareDate(queryDate, DateUtil.getCurDay()) > 0) {
			return false;
		}
		Date nowTime = new Date();
		String sTime = DateUtil.format(nowTime, "HH:mm");
		if (vouchTime.indexOf("-") != -1) {
			String[] vouchTimes = vouchTime.split("-");
			if (vouchTimes.length >= 2) {
				if (sTime.compareTo(vouchTimes[0]) > 0 && sTime.compareTo(vouchTimes[1]) < 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		HotelRoomService s = new HotelRoomService();
		// boolean r = s.isVouchTime("2013-02-27", "00:00-24:00");
		// System.out.println(r);

		HotelRatePlan plan = new HotelRatePlan();
		plan.setExtraInfo("{'vouchSet':'1','totlePrice':'178.0','vouchTime':'00:00-24:00'}");
		System.out.println(s.isVouchSet(plan, "2013-02-27"));
	}

	public class RatePlanComparator implements Comparator<HotelRatePlan> {
		@Override
		public int compare(HotelRatePlan plan1, HotelRatePlan plan2) {
			return plan1.getStatusUpdateTime().getTime() > plan1.getStatusUpdateTime().getTime() ? 1 : (plan1
					.getStatusUpdateTime().getTime() == plan1.getStatusUpdateTime().getTime() ? 0 : -1);
		}
	}

	public class RatePlanPriceComparator implements Comparator<HotelRatePlan> {
		@Override
		public int compare(HotelRatePlan plan1, HotelRatePlan plan2) {
			return plan1.getPrivceMember() > plan2.getPrivceMember() ? 1 : (plan1.getPrivceMember() == plan2
					.getPrivceMember() ? 0 : -1);
		}
	}

}
