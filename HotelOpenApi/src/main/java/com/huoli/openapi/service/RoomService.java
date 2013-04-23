package com.huoli.openapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huoli.openapi.exception.HotelRoomsServiceException;
import com.huoli.openapi.mapper.ChannelUserMapper;
import com.huoli.openapi.mapper.CityListMapper;
import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.mapper.HotelRatePlanMapper;
import com.huoli.openapi.roomStatus.HotelRooms;
import com.huoli.openapi.roomStatus.NewHotelStatusClient;
import com.huoli.openapi.roomStatus.RoomSimpleStatus;
import com.huoli.openapi.roomStatus.RoomSimpleStatusResult;
import com.huoli.openapi.roomStatus.RoomStatus;
import com.huoli.openapi.roomStatus.RoomStatusResult;
import com.huoli.openapi.util.Constant;
import com.huoli.openapi.util.DateUtil;
import com.huoli.openapi.util.GeoMapConvert;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.data.GpsQuery;
import com.huoli.openapi.vo.data.HotelBaseInfo;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.HotelInfoCollectionSimple;
import com.huoli.openapi.vo.result.ChainData;
import com.huoli.openapi.vo.result.ChainInfo;
import com.huoli.openapi.vo.result.ChainResult;
import com.huoli.openapi.vo.result.City;
import com.huoli.openapi.vo.result.CityData;
import com.huoli.openapi.vo.result.CityListResult;
import com.huoli.openapi.vo.result.HotelCollectionResult;
import com.huoli.openapi.vo.result.HotelData;
import com.huoli.openapi.vo.result.HotelRoomsData;
import com.huoli.openapi.vo.result.HotelRoomsResult;
import com.huoli.openapi.vo.result.RoomStatusData;
import com.huoli.openapi.vo.result.SearchHotelsData;
import com.huoli.openapi.vo.result.SearchHotelsResult;
import com.huoli.openapi.vo.result.WapRoomSimpleStatus;
import com.huoli.openapi.vo.result.WapSearchHotelsData;
import com.huoli.openapi.vo.result.WapSearchHotelsResult;

/**
 * 
 * 
 * @项目名称：HotelOpenApi
 * @类名称：RoomService
 * @类描述：
 * @创建人：dongjun
 * @创建时间：2012-10-31 下午4:13:55
 * @修改人：dongjun
 * @修改时间：2012-10-31 下午4:13:55
 * @修改备注：
 * @version v1.0
 * 
 */
@Component
public class RoomService {
	/**
	 * 最大搜索半径
	 */
	private static final double MAX_RADIUS = 25;
	/**
	 * 最多返回数
	 */
	private static final int MAX_HOTEL_NUM = 100;
	private static final int MAX_TIMEOUT = 10000;
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;
	@Resource
	private ChannelUserMapper channelUserMapper;
	@Resource
	private CityListMapper cityListMapper;
	@Autowired
	private NewHotelStatusClient hotelStatusClient;
	@Resource
	private Constant constant;
	@Resource
	private HotelRatePlanMapper hotelRatePlanMapper;

	private Logger logger = LoggerFactory.getLogger(RoomService.class);

	/**
	 * 查找附近酒店，默认返回 10个酒店，并且不转换经纬度
	 * 
	 * @param request
	 * @param lat
	 * @param lnt
	 * @param tudemode
	 *            经纬度模式 0 火星坐标，1 地球坐标 ，默认0
	 * @param hotelType
	 * @param radius
	 * @param date
	 * @param resultcount
	 *            返回结果数量 默认10
	 * @return
	 */
	@SuppressWarnings("null")
	public SearchHotelsResult doQueryConlinesHotels(HttpServletRequest request, String lat, String lnt,
			Integer tudemode, String hotelType, String radius, String date, Integer resultcount, Integer isbooking) {
		SearchHotelsResult result = new SearchHotelsResult();
		if (tudemode == null) {
			tudemode = 0;
		}
		if (radius == null) {
			radius = "25";
		}
		if (resultcount == null) {
			resultcount = 10;
		}
		if (isbooking == null) {
			isbooking = 0;
		}
		if (StringUtils.isBlank(date)) {
			date = DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD);
		} else {
			if (DateUtil.getDateDiff(DateUtil.toDate(date, DateUtil.YYYY_MM_DD), new Date()) < 0) {
				result.setMsg("时间不正确！");
				result.setCode("03");
				result.setStatus("fail");
				return result;
			}
		}
		if (!isDouble(lat) || !isDouble(lnt)) {
			result.setMsg("经纬度参数不正确！");
			result.setCode("01");
			result.setStatus("fail");
			return result;
		}
		double fRadius = MAX_RADIUS;

		if (StringUtils.isNotBlank(radius) && !isDouble(radius)) {
			result.setMsg("半径参数不正确！");
			result.setCode("02");
			result.setStatus("fail");
			return result;
		}

		if (isDouble(radius)) {
			fRadius = Double.valueOf(radius);
		}

		double fLat = Double.valueOf(lat);
		double fLnt = Double.valueOf(lnt);
		List<HotelInfoCollectionSimple> hotels = this.searchNearBy(fLat, fLnt, hotelType, fRadius, resultcount,
				tudemode);
		List<HotelInfoCollectionSimple> hotelList = new LinkedList<HotelInfoCollectionSimple>();
		Map<String, HotelInfoCollectionSimple> hotelTemp = new HashMap<String, HotelInfoCollectionSimple>();
		for (HotelInfoCollectionSimple s : hotels) {
			if (this.isJoint(s.getHotelType())) {
				hotelTemp.put(s.getHotelId() + "|" + s.getHotelType(), s);
				hotelList.add(s);
			}

		}
		List<RoomSimpleStatus> hotelRooms = this.getRoomSimpleStatusByCache(hotelList, date);
		if (hotelRooms != null) {
			for (RoomSimpleStatus rss : hotelRooms) {
				HotelInfoCollectionSimple hs = hotelTemp.get(rss.getHotelId() + "|" + rss.getHotelType());
				if (hs == null) {
					continue;
				} else {
					rss.setDistance(hs.getRadius());
				}
				String outHotelId = Constant.HOTELMAP.get(rss.getHotelId() + "|" + rss.getHotelType());
				if (outHotelId == null) {
					constant.init();
					outHotelId = Constant.HOTELMAP.get(rss.getHotelId() + "|" + rss.getHotelType());
				}
				if (outHotelId != null) {
					rss.setHotelId(outHotelId);
				} else {
					logger.info("get hotel is null,hotelId={},hotelType={}", rss.getHotelId(), rss.getHotelType());
				}

			}
		}

		SearchHotelsData data = new SearchHotelsData();
		data.setHotels(hotelRooms);
		result.setCode("00");
		result.setData(data);
		result.setMsg("搜索成功");
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		result.setStatus(Constant.RESULT_STATUS_SUCCESS);
		return result;
	}

	public HotelInfoCollection getHotelInfoById(String id) {
		return hotelInfoCollectionMapper.selectById(Long.valueOf(id));
	}

	/**
	 * wap版本搜索
	 * 
	 * @param request
	 * @param lat
	 * @param lnt
	 * @param tudemode
	 * @param hotelType
	 * @param radius
	 * @param date
	 * @param resultcount
	 * @param isbooking
	 * @param placeName
	 * @return
	 */
	public WapSearchHotelsResult wapSearchHotels(HttpServletRequest request, String lat, String lnt, Integer tudemode,
			String hotelType, String radius, String date, Integer resultcount, Integer isbooking, String placeName) {

		String[] geos = null;
		if (placeName != null && !placeName.isEmpty()) {
			geos = GeoMapConvert.placeNameConvertGeoLocation(placeName);
			lat = geos[1];
			lnt = geos[0];
		}

		SearchHotelsResult sr = this.doQueryConlinesHotels(request, lat, lnt, tudemode, hotelType, radius, date,
				resultcount, isbooking);
		WapSearchHotelsResult result = new WapSearchHotelsResult();
		result.setCode(sr.getCode());
		result.setMsg(sr.getMsg());
		result.setReferenceID(sr.getReferenceID());
		result.setStatus(sr.getStatus());
		WapSearchHotelsData data = new WapSearchHotelsData();
		LinkedList<WapRoomSimpleStatus> roomList = new LinkedList<WapRoomSimpleStatus>();
		if (sr.getData() != null && sr.getData().getHotels() != null && sr.getData().getHotels().size() > 0) {
			for (RoomSimpleStatus item : sr.getData().getHotels()) {
				WapRoomSimpleStatus room = new WapRoomSimpleStatus(item);
				HotelInfoCollection hotelInfo = hotelInfoCollectionMapper.selectById(Long.valueOf(item.getHotelId()));
				room.setHotelName(hotelInfo.getHotelPrefix() + "-" + hotelInfo.getName());
				String logoPic = hotelInfoCollectionMapper.selectChannelLogo(hotelInfo.getHotelType());
				room.setPic(logoPic);
				room.setTel(hotelInfo.getTel());
				room.setUrl(constant.getWapEntryPage() + "?hotelId=" + room.getHotelId() + "&hotelType="
						+ room.getHotelType());
				roomList.add(room);
			}
		}
		data.setHotels(roomList);
		result.setData(data);
		return result;
	}

	private boolean isJoint(String hotelType) {
		// String channels =
		// "001,002,003,005,015,010,011,014,016,900,017,018,019,020";
		// if (hotelType != null && channels.indexOf(hotelType) != -1)
		// return true;
		// return false;
		return true;
	}

	/**
	 * 从数据库中取得最近的酒店范围
	 * 
	 * @param lat
	 * @param lnt
	 * @param hotelType
	 * @return
	 */
	private List<HotelInfoCollectionSimple> searchNearBy(double lat, double lnt, String hotelType, double radius,
			int limit, Integer tudemode) {
		GpsQuery query = new GpsQuery();
		query.setLat(lat);
		query.setLnt(lnt);
		query.setRadius(radius);
		query.setHotelType(hotelType);
		query.setLimit(limit);
		List<HotelInfoCollectionSimple> hotels = null;
		if (tudemode != null && tudemode == 2) {
			hotels = hotelInfoCollectionMapper.querySimpleNearByOfBigConfinesBaidu(query);
		} else {
			hotels = hotelInfoCollectionMapper.querySimpleNearByOfBigConfines(query);
		}
		return hotels;
	}

	/**
	 * 获取酒店的简单房态信息只包括有房无房，最低房价
	 * 
	 * @param hotels
	 * @return
	 * 
	 */
	private List<RoomSimpleStatus> getRoomSimpleStatusByCache(List<HotelInfoCollectionSimple> hotels, String date) {
		StringBuilder query = new StringBuilder();

		List<RoomSimpleStatus> result = new ArrayList<RoomSimpleStatus>();
		for (HotelInfoCollectionSimple hotel : hotels) {
			query.append(hotel.getHotelType()).append(hotel.getHotelId()).append(",");
			RoomSimpleStatus status = new RoomSimpleStatus();
			status.setDateTime(date);
			status.setHotelId(hotel.getHotelId());
			status.setHotelType(hotel.getHotelType());
			status.setPrice(0);
			status.setStatus(2);// 未知房态
			result.add(status);
		}

		if (query.length() > 0) {
			query.deleteCharAt(query.length() - 1);
		}

		try {
			RoomSimpleStatusResult rooms = hotelStatusClient.searchHotelByCache(query.toString(), date);
			if (rooms.getData() != null && rooms.getData().size() > 0) {
				result = rooms.getData();
			} else {
			}
		} catch (Exception e) {
			logger.error("hotelStatusClient.searchHotelByCachee  fail!", e);
		}

		return result;
	}

	/**
	 * 获取品牌列表
	 * 
	 * @return
	 */
	public ChainResult getChainList() {
		List<ChainInfo> chainList = channelUserMapper.selectChainList();
		ChainData data = new ChainData();
		data.setChains(chainList);
		ChainResult result = new ChainResult();
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		result.setData(data);
		return result;
	}

	/**
	 * 获取城市列表
	 * 
	 * @return
	 */
	public CityListResult getCityList() {
		List<City> cityList = cityListMapper.selectCityList();
		CityData data = new CityData();
		data.setCitys(cityList);
		CityListResult result = new CityListResult();
		result.setData(data);
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		return result;
	}

	/**
	 * 获取酒店列表
	 * 
	 * @return
	 */
	public HotelCollectionResult getHotelList(Long channelId) {
		HotelCollectionResult result = new HotelCollectionResult();
		List<HotelInfoCollection> hotels = hotelInfoCollectionMapper.selectOfficialOrderHotels(String
				.valueOf(channelId));
		HotelData data = new HotelData();
		data.setHotels(hotels);
		result.setHotelData(data);
		result.setCode(Constant.RESULT_CODE_SUCCESS);
		result.setMsg(Constant.RESULT_MSG_SUCCESS);
		result.setReferenceID(String.valueOf(System.currentTimeMillis()));
		return result;
	}

	/**
	 * 查询房态详情
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @return
	 */
	public SearchHotelsResult getHotelRoomStatusBatch(HttpServletRequest request, String checkinDate, String hotelIds) {
		SearchHotelsResult result = new SearchHotelsResult();

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) < 0) {
			result.setCode("36");
			result.setMsg("入住时间不能小于当前时间");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) > 7) {
			result.setCode("37");
			result.setMsg("只能查询7天的房态");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (hotelIds == null || hotelIds.isEmpty() || hotelIds.split(",").length > 201) {
			result.setCode("39");
			result.setMsg("酒店id为空，或是酒店数量大于200");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		try {
			String[] hotels = hotelIds.split(",");
			List<String> hotelIdList = new ArrayList<String>();
			for (String id : hotels) {
				hotelIdList.add(id);
			}
			List<HotelBaseInfo> hotelBaseInfos = hotelInfoCollectionMapper.selectHotelsBaseInfo(hotelIdList);
			Map<String, String> hotelMap = new HashMap<String, String>();
			String hotelParams = "";
			for (HotelBaseInfo item : hotelBaseInfos) {
				String innerHotelId = item.getHotelType() + item.getHotelId();
				hotelMap.put(innerHotelId, String.valueOf(item.getId()));
				if (hotelParams != null && !"".equals(hotelParams)) {
					hotelParams = hotelParams + ",";
				}
				hotelParams = hotelParams + innerHotelId;
			}

			RoomSimpleStatusResult roomsResult = hotelStatusClient.searchHotelByCache(hotelParams, checkinDate);
			if (roomsResult.getResult().equals("1")) {
				List<RoomSimpleStatus> rooms = roomsResult.getData();
				if (rooms != null && !rooms.isEmpty())
					for (RoomSimpleStatus item : rooms) {
						item.setDistance(0d);
						if (item.getPrice() == null) {
							item.setPrice(0);
						}
						item.setHotelId(hotelMap.get(item.getHotelType() + item.getHotelId()));
					}
				SearchHotelsData data = new SearchHotelsData();
				data.setHotels(rooms);
				result.setData(data);
				result.setCode(Constant.RESULT_CODE_SUCCESS);
				result.setMsg(Constant.RESULT_MSG_SUCCESS);
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_SUCCESS);
			} else {
				result.setCode("32");
				result.setMsg(roomsResult.getMsg());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (Exception e) {
			throw new HotelRoomsServiceException("hotelRooms service is error", e);
		}
		return result;
	}

	public HotelRoomsResult getHotelRoomRatePlans(HttpServletRequest request, String checkinDate,
			String checkoutDate, String hotelId, String hotelType, String hasCache, String isOfficial) {
		HotelRoomsResult result = new HotelRoomsResult();

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) < 0) {
			result.setCode("36");
			result.setMsg("入住时间不能小于当前时间");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) > 30) {
			result.setCode("37");
			result.setMsg("只能查询30天之内的房态");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, checkoutDate) >= 0) {
			result.setCode("38");
			result.setMsg("时间错误");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkoutDate, checkinDate) > 31) {
			result.setCode("39");
			result.setMsg("只能查询30天的房态");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		try {
			boolean hasCacheFlag = false;
			if ("1".equals(hasCache)) {
				hasCacheFlag = true;
			}
			boolean isOfficialFlag = false;
			if ("1".equals(isOfficial)) {
				isOfficialFlag = true;
			}
			RoomStatusResult roomsResult = hotelStatusClient.searchTotalRoomsByDays(hotelType, hotelId, checkinDate,
					checkoutDate, hasCacheFlag,isOfficialFlag, String.valueOf(MAX_TIMEOUT));
			if (roomsResult.getResult().equals("1")) {
				List<HotelRooms> rooms = roomsResult.getData();
				if (rooms != null && !rooms.isEmpty()) {
					List<RoomStatusData> roomList = new LinkedList<RoomStatusData>();
					HotelRooms hotelRoom = rooms.get(0);
					List<RoomStatus> roomStatus = hotelRoom.getRooms();
					Map<String, List<RoomStatus>> roomsMap = new HashMap<String, List<RoomStatus>>();
					for (RoomStatus item : roomStatus) {
						if (roomsMap.get(item.getDate()) != null) {
							roomsMap.get(item.getDate()).add(item);
						} else {
							List<RoomStatus> rs = new LinkedList<RoomStatus>();
							rs.add(item);
							roomsMap.put(item.getDate(), rs);
						}

					}
					for (String sDate : roomsMap.keySet()) {
						RoomStatusData roomsData = new RoomStatusData();
						List<RoomStatus> rd = new LinkedList<RoomStatus>();
						roomsData.setRooms(roomsMap.get(sDate));
						roomsData.setDate(sDate);
						roomsData.setEndDate(DateUtil.getNextDay(sDate));
						roomList.add(roomsData);
					}

					HotelRoomsData data = new HotelRoomsData();
					data.setRooms(roomList);
					result.setData(data);
					result.setCode(Constant.RESULT_CODE_SUCCESS);
					result.setMsg(Constant.RESULT_MSG_SUCCESS);
					result.setReferenceID(String.valueOf(System.currentTimeMillis()));
					result.setStatus(Constant.RESULT_STATUS_SUCCESS);
				} else {
					result.setCode("31");
					result.setMsg("not found hotel rooms!");
					result.setReferenceID(String.valueOf(System.currentTimeMillis()));
					result.setStatus(Constant.RESULT_STATUS_UNKNOWN);
				}
			} else {
				result.setCode("32");
				result.setMsg(roomsResult.getMsg());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (Exception e) {
			throw new HotelRoomsServiceException("hotelRooms service is error", e);
		}
		return result;
	}

	/**
	 * 查询房态详情
	 * 
	 * @param request
	 * @param checkinDate
	 * @param checkoutDate
	 * @param hotelId
	 * @param hotelType
	 * @return
	 */
	public HotelRoomsResult getHotelRoomStatusDetail(HttpServletRequest request, String checkinDate,
			String checkoutDate, String hotelId, String hotelType, String hasCache,String isOfficial) {
		HotelRoomsResult result = new HotelRoomsResult();

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) < 0) {
			result.setCode("36");
			result.setMsg("入住时间不能小于当前时间");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD)) > 30) {
			result.setCode("37");
			result.setMsg("只能查询30天之内的房态");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, checkoutDate) >= 0) {
			result.setCode("38");
			result.setMsg("时间错误");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		if (DateUtil.getDifferenceDays(checkinDate, checkoutDate) > 7) {
			result.setCode("39");
			result.setMsg("只能查询7天的房态");
			result.setReferenceID(String.valueOf(System.currentTimeMillis()));
			result.setStatus(Constant.RESULT_STATUS_FAIL);
			return result;
		}

		try {
			boolean hasCacheFlag = false;
			if ("1".equals(hasCache)) {
				hasCacheFlag = true;
			}
			boolean isOfficialFlag = false;
			if ("1".equals(isOfficial)) {
				isOfficialFlag = true;
			}
			RoomStatusResult roomsResult = hotelStatusClient.searchShowRoomByDays(hotelType, hotelId, checkinDate,
					checkoutDate, hasCacheFlag,isOfficialFlag, String.valueOf(MAX_TIMEOUT));
			if (roomsResult.getResult().equals("1")) {
				List<HotelRooms> rooms = roomsResult.getData();
				if (rooms != null && !rooms.isEmpty()) {
					List<RoomStatusData> roomList = new LinkedList<RoomStatusData>();
					for (HotelRooms hotelRoom : rooms) {
						List<RoomStatus> roomStatus = hotelRoom.getRooms();
						RoomStatusData roomsData = new RoomStatusData();
						roomsData.setRooms(roomStatus);
						roomsData.setDate(checkinDate);
						roomsData.setEndDate(checkoutDate);
						roomList.add(roomsData);
					}

					HotelRoomsData data = new HotelRoomsData();
					data.setRooms(roomList);
					result.setData(data);
					result.setCode(Constant.RESULT_CODE_SUCCESS);
					result.setMsg(Constant.RESULT_MSG_SUCCESS);
					result.setReferenceID(String.valueOf(System.currentTimeMillis()));
					result.setStatus(Constant.RESULT_STATUS_SUCCESS);
				} else {
					result.setCode("31");
					result.setMsg("not found hotel rooms!");
					result.setReferenceID(String.valueOf(System.currentTimeMillis()));
					result.setStatus(Constant.RESULT_STATUS_UNKNOWN);
				}
			} else {
				result.setCode("32");
				result.setMsg(roomsResult.getMsg());
				result.setReferenceID(String.valueOf(System.currentTimeMillis()));
				result.setStatus(Constant.RESULT_STATUS_FAIL);
			}
		} catch (Exception e) {
			throw new HotelRoomsServiceException("hotelRooms service is error", e);
		}
		return result;
	}

	/**
	 * 判断字符串是否double型
	 * 
	 * @author Alvise
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (str == null || StringUtils.isBlank(str)) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if ('.' == str.charAt(i)) {
				continue;
			}
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
