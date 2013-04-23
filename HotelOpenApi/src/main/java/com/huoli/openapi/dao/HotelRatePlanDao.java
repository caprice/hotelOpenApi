package com.huoli.openapi.dao;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huoli.openapi.cache.HotelCachedClient;
import com.huoli.openapi.mapper.HotelRatePlanMapper;
import com.huoli.openapi.util.DateUtil;
import com.huoli.openapi.util.JsonBinder;
import com.huoli.openapi.util.Tools;
import com.huoli.openapi.vo.data.HotelRatePlan;
import com.huoli.openapi.vo.data.RatePlan2Query;
import com.huoli.openapi.vo.data.RatePlanQuery;

@Component
public class HotelRatePlanDao {
	@Autowired
	private HotelRatePlanMapper hotelRatePlanMapper;
	@Autowired
	private HotelCachedClient memcachedClient;
	private Logger logger = LoggerFactory.getLogger(HotelRatePlanDao.class);

	private int defaultExp = 60 * 60 * 24;

	public List<HotelRatePlan> select(RatePlan2Query query) {
		if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
			int days = DateUtil.getDifferenceDays(query.getEndDate(), query.getDate());
			if (days > 1) {
				return hotelRatePlanMapper.select(query);
			}
		}
		List<HotelRatePlan> result = null;
		String key = this.getMemcachedKey(query);
		Object value = memcachedClient.get(key);
		if (value instanceof String) {
			JsonBinder binder = JsonBinder.buildNonDefaultBinder();
			try {
				result = binder.getMapper().readValue((String) value, new TypeReference<List<HotelRatePlan>>() {
				});
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(Tools.exceptionMsg(e));
			}

		}
		if (result == null || result.isEmpty()) {
			result = hotelRatePlanMapper.select(query);
			if (result != null && !result.isEmpty()) {
				memcachedClient.set(key, defaultExp, result);
			}
		}
		return result;
	}

	public List<HotelRatePlan> selectByDates(RatePlanQuery query) {
		long b = System.currentTimeMillis();
		List<HotelRatePlan> result = null;
		String key = this.getMemcachedKey(query);
		result = memcachedClient.get(key);
		if (result == null) {
			result = hotelRatePlanMapper.selectByDates(query);
			if (result != null) {
				memcachedClient.set(key, 60 * 30, result);
			}
		}

		return result;
	}

	public boolean delKeyObject(RatePlan2Query query) {
		logger.info("detele memcached:query=" + query);
		return memcachedClient.delete(this.getMemcachedKey(query));
	}

	private String getMemcachedKey(RatePlan2Query query) {
		return query.getChannelType() + "_" + query.getHotelId() + "_" + query.getDate();
	}

	private String getMemcachedKey(RatePlanQuery query) {
		return query.getChannelType() + "_" + query.getHotelId() + "_" + query.getRoomType() + "_"
				+ query.getBeginDate() + "_" + query.getEndDate();
	}
}