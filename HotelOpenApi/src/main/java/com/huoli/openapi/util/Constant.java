package com.huoli.openapi.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.huoli.openapi.help.PropertyConfig;
import com.huoli.openapi.mapper.HotelInfoCollectionMapper;
import com.huoli.openapi.vo.data.HotelBaseInfo;

@Component
public class Constant {
	public static final String RESULT_STATUS_SUCCESS = "success";
	public static final String RESULT_STATUS_FAIL = "fail";
	public static final String RESULT_STATUS_ERROR = "error";
	public static final String RESULT_STATUS_UNKNOWN = "unknown	";
	public static final String RESULT_STATUS_FORBIDDEN = "forbidden";

	public static final String RESULT_MSG_SUCCESS = "ok";

	public static final String RESULT_CODE_SUCCESS = "00";

	public static final String ELONG_ROOM_DESC_ALIAS_NAME = "kj1";

	public static final String CTRIP_ROOM_DESC_ALIAS_NAME = "kj2";

	public static final int MC_EXPIRE_TIME_DEDAUTLT = 1000 * 60 * 60 * 24;
	public static Map<String, String> HOTELMAP = new HashMap<String, String>();
	@Resource
	private HotelInfoCollectionMapper hotelInfoCollectionMapper;
	private Logger logger = org.slf4j.LoggerFactory.getLogger(Constant.class);
	@PropertyConfig("client_ip_white_list")
	private String clientWhiteList;
	@PropertyConfig("service_status")
	private String serviceStatus = "1";
	@PropertyConfig("wap_entry_page")
	private String wapEntryPage;

	public static String getSourceRoomDesc(String aliasName) {
		if ("kj1".equals(aliasName)) {
			return "elong";
		} else if ("kj2".equals(aliasName)) {
			return "ctrip";
		} else {
			return aliasName;
		}
	}

	public String getWapEntryPage() {
		return wapEntryPage;
	}

	public void setWapEntryPage(String wapEntryPage) {
		this.wapEntryPage = wapEntryPage;
	}

	public String getClientWhiteList() {
		return clientWhiteList;
	}

	@PostConstruct
	public void init() {
		List<HotelBaseInfo> hotels = hotelInfoCollectionMapper.selectAllHotelBaseInfo();
		Map<String, String> hotelMap = new HashMap<String, String>();
		logger.info("get hotels count is :{}", hotels.size());
		for (HotelBaseInfo hotel : hotels) {
			hotelMap.put(hotel.getHotelId() + "|" + hotel.getHotelType(), String.valueOf(hotel.getId()));
		}
		HOTELMAP = hotelMap;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
}
