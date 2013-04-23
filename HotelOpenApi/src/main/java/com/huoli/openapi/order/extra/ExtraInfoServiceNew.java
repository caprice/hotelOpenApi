package com.huoli.openapi.order.extra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huoli.openapi.mapper.HotelRatePlanMapper;
import com.huoli.openapi.vo.data.HotelRatePlan;
import com.huoli.openapi.vo.data.RatePlanQuery;
import com.huoli.openapi.vo.data.UserLoginInfo;

@Component
public class ExtraInfoServiceNew implements OrderExtraInfo {
	@Autowired
	private HotelRatePlanMapper hotelRatePlanMapper;

	@Override
	public Map<String, String> getCreateOrderExtraInfo(String hotelType, String hotelId, Map<String,String> params, String roomId,String beginDate,String endDate) {
		Map<String, String> result = new HashMap<String, String>();
		RatePlanQuery query = new RatePlanQuery();
		query.setBeginDate(beginDate);
		query.setEndDate(endDate);
		query.setChannelType(hotelType);
		query.setHotelId(hotelId);
		query.setRoomType(roomId);
		List<HotelRatePlan> plans = hotelRatePlanMapper.selectByDates(query);
		result = this.getCreateOrderExtraInfo(hotelType, plans);
		if (result != null) {
			if(params!=null)
			result.putAll(params);
		} else {
			result = params;
		}
		return result;
	}

	/**
	 * 获取创建订单额外信息
	 * 
	 * @param hotelType
	 * @param plans
	 * @return
	 */
	private Map<String, String> getCreateOrderExtraInfo(String hotelType, List<HotelRatePlan> plans) {
		Map<String, String> result = new HashMap<String, String>();
		if (hotelType.equals("002")) {
			StringBuilder extras = new StringBuilder();
			for (HotelRatePlan plan : plans) {
				extras = extras.append(plan.getDate()).append(",").append(plan.getPrivceMember()).append(",")
						.append(plan.getExtraInfo()).append(";");
			}
			result.put("priceInfos", extras.toString());
		} else if (hotelType.equals("003")) {
			String multipleDaysRoomPrice = new String();
			for (HotelRatePlan plan : plans) {
				multipleDaysRoomPrice += (int) plan.getPrivceMember() + ",";
			}
			multipleDaysRoomPrice = multipleDaysRoomPrice.substring(0, multipleDaysRoomPrice.length() - 1);
			result.put("multipleDaysRoomPrice", multipleDaysRoomPrice);
		} else if (hotelType.equals("017")) {
			for (HotelRatePlan plan : plans) {
				JSONObject jsonSrc = JSONObject.fromObject(plan.getExtraInfo());
				Iterator iterator = jsonSrc.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = jsonSrc.getString(key);
					if (key.equals("dfxcommAmt")) {
						if (result.get(key) != null) {
							value = result.get(key) + "," + value;
						}
					} else if (key.equals("dcommAmt")) {
						if (result.get(key) != null) {
							value = result.get(key) + "," + value;
						}
					} else if (key.equals("everyDayAmt")) {
						if (result.get(key) != null) {
							value = result.get(key) + "," + value;
						}
					}
					result.put(key, value);
				}
			}
		} else if (hotelType.equals("020")) {
			String roomRates = new String();
			for (HotelRatePlan plan : plans) {
				roomRates += plan.getPrivceMember() + ",";
			}
			roomRates = roomRates.substring(0, roomRates.length() - 1);
			result.put("roomRates", roomRates);
		} else if (hotelType.equals("ctrip")) {
			for (HotelRatePlan plan : plans) {
				JSONObject jsonSrc = JSONObject.fromObject(plan.getExtraInfo());
				Iterator iterator = jsonSrc.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = jsonSrc.getString(key);
					result.put(key, value);
				}
				if (result.size() > 0) {
					break;
				}
			}

		} else if (hotelType.equals("elong")) {
			for (HotelRatePlan plan : plans) {
				JSONObject jsonSrc = JSONObject.fromObject(plan.getExtraInfo());
				Iterator iterator = jsonSrc.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = jsonSrc.getString(key);
					result.put(key, value);
				}
				if (result.size() > 0) {
					break;
				}
			}

		} else {

		}

		return result;
	}

	@Override
	public String hotelTypeReplace(String hotelType) {
		// TODO Auto-generated method stub
		return hotelType;
	}

}
