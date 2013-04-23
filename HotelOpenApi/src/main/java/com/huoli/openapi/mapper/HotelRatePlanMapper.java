package com.huoli.openapi.mapper;

import java.util.List;

import com.huoli.openapi.vo.data.HotelRatePlan;
import com.huoli.openapi.vo.data.RatePlan2Query;
import com.huoli.openapi.vo.data.RatePlanQuery;

public interface HotelRatePlanMapper {
	public List<HotelRatePlan> select(RatePlan2Query query);

	public List<HotelRatePlan> selectByDates(RatePlanQuery query);
}
