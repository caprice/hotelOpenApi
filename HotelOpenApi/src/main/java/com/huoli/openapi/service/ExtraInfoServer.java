package com.huoli.openapi.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.order.extra.ExtraInfoServiceNew;
import com.huoli.openapi.roomStatus.NewHotelStatusClient;
import com.huoli.openapi.roomStatus.RoomStatus;
import com.huoli.openapi.roomStatus.RoomStatusResult;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.data.HotelInfoCollection;
import com.huoli.openapi.vo.data.OrderVerify;

@Component
public class ExtraInfoServer {
	@Resource
	private NewHotelStatusClient hotelStatusClient;
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;
	@Autowired
	private ExtraInfoServiceNew orderExtraInfo;
	private Logger logger = LoggerFactory.getLogger(ExtraInfoServer.class);

	

	public Map<String, String> getCreateOrderExtraInfo(OrderVerify order) {
		Map<String, String> result = new HashMap();
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
		result  = orderExtraInfo.getCreateOrderExtraInfo(hotelType, hotelId, null, roomId,order.getCheckinDate(),order.getCheckoutDate());
		return result;
	}

	public Map<String, String> sevenExtraInfo(String hotelType, String hotelId, String startDate, String endDate,
			String rateType, String roomId) {
		Map<String, String> result = new HashMap();
		try {
			RoomStatusResult roomStatus = hotelStatusClient.searchRoomPriceByDays(hotelType, hotelId, startDate,
					endDate, rateType, roomId);
			if (roomStatus != null && "1".equals(roomStatus.getResult()) && roomStatus.getData() != null) {
				List<RoomStatus> rooms = roomStatus.getData().get(0).getRooms();
				return rooms.get(0).getExtra();
			}
		} catch (Exception e) {
			logger.info(Tools.exceptionMsg(e));
		}
		return result;
	}

	public Map<String, String> cssExtraInfo(String hotelType, String hotelId, String startDate, String endDate,
			String rateType, String roomId) {
		Map<String, String> result = new HashMap();
		try {
			RoomStatusResult roomStatus = hotelStatusClient.searchRoomPriceByDays(hotelType, hotelId, startDate,
					endDate, rateType, roomId);
			if (roomStatus != null && "1".equals(roomStatus.getResult()) && roomStatus.getData() != null) {
				List<RoomStatus> rooms = roomStatus.getData().get(0).getRooms();
				return rooms.get(0).getExtra();
			}
		} catch (Exception e) {
			logger.info(Tools.exceptionMsg(e));
		}
		return result;
	}

	public Map<String, String> ninetynineExtraInfo(String idCard) {
		Map<String, String> result = new HashMap();
		result.put("idCard", idCard);
		return result;
	}

	public Map<String, String> jinjiangExtraInfo(String hotelType, String hotelId, String startDate, String endDate,
			String rateType, String roomId) {
		Map<String, String> result = new HashMap();
		String multipleDaysRoomPrice = "";
		try {

			RoomStatusResult roomStatus = hotelStatusClient.searchRoomPriceByDays(hotelType, hotelId, startDate,
					endDate, rateType, roomId);
			if (roomStatus != null && "1".equals(roomStatus.getResult()) && roomStatus.getData() != null) {
				List<RoomStatus> rooms = roomStatus.getData().get(0).getRooms();
				for (RoomStatus room : rooms) {
					multipleDaysRoomPrice += room.getMemberPrice() + ",";
				}
				multipleDaysRoomPrice = multipleDaysRoomPrice.substring(0, multipleDaysRoomPrice.length() - 1);
				result.put("multipleDaysRoomPrice", multipleDaysRoomPrice);
			}
			return result;
		} catch (Exception e) {
			logger.info(Tools.exceptionMsg(e));
		}
		return result;
	}

	public Map<String, String> htinnsExtraInfo(String hotelType, String hotelId, String startDate, String endDate,
			String rateType, String roomId) {
		Map<String, String> result = new HashMap();
		try {
			RoomStatusResult roomStatus = hotelStatusClient.searchRoomPriceByDays(hotelType, hotelId, startDate,
					endDate, rateType, roomId);
			if (roomStatus != null && "1".equals(roomStatus.getResult()) && roomStatus.getData() != null) {
				List<RoomStatus> list = roomStatus.getData().get(0).getRooms();
				result = list.get(0).getExtra();
			}
		} catch (Exception e) {
			logger.info(Tools.exceptionMsg(e));
		}
		String retention = this.getSpecialPeriodTimeInfo(hotelType, hotelId);
		if (!StringUtils.isBlank(retention)) {
			if (result == null) {
				result = new HashMap();
			}
			result.put("retention", retention);
		}
		return result;
	}

	private String getSpecialPeriodTimeInfo(String hotelType, String hotelId) {
		HotelInfoCollection query = new HotelInfoCollection();
		query.setHotelType(hotelType);
		query.setHotelId(hotelId);
		HotelInfoCollection hotel = hotelInfoCollectionMapper.selectByHotelId(query);
		String result = "";
		if (hotel != null && hotel.getExtraInfo().indexOf("{") != -1) {
			String jsonSrc = hotel.getExtraInfo();

			JSONObject json = JSONObject.fromObject(jsonSrc);
			JSONArray jsonItems = json.getJSONArray("retention");
			for (int i = 0; i < jsonItems.size(); i++) {
				JSONObject item = jsonItems.getJSONObject(i);
				Iterator<String> it = item.keys();
				String values = "";
				while (it.hasNext()) {
					String key = it.next();
					String value = item.getString(key);
					if (!StringUtils.isBlank(value)) {
						if (!StringUtils.isBlank(values)) {
							values = values + "|";
						}
						if (key.equals("ltime"))
							key = "Itime";
						values = values + key + "=" + value;
					}
				}

				if (!StringUtils.isBlank(values)) {
					if (!StringUtils.isBlank(result)) {
						result = result + ",";
					}
					result = result + values;
				}

			}
		}
		logger.info("get htinns hotel retention info ,hotelid=" + hotelId + ",info=" + result);
		return result;

	}
}
