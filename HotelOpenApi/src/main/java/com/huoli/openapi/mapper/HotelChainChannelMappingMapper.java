package com.huoli.openapi.mapper;

import java.util.List;

import com.huoli.openapi.vo.data.HotelChainChannelMapping;

public interface HotelChainChannelMappingMapper {
	List<HotelChainChannelMapping> select(String hotelType);
}
