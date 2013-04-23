package com.huoli.openapi.mapper;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.UserWxInfo;
@Component
public interface UserWxInfoMapper {
	public int insert(UserWxInfo user);
	public UserWxInfo select(String wxUser);
}
