package com.huoli.openapi.mapper;

import java.util.List;

import com.huoli.openapi.vo.data.HotelInfoMapping;

public interface HotelInfoMappingMapper {
	List<HotelInfoMapping> select(HotelInfoMapping query);
}
