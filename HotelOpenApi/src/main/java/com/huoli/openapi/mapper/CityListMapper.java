package com.huoli.openapi.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.result.City;

@Component
public interface CityListMapper {

	public List<City> selectCityList();
}
