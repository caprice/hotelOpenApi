package com.huoli.openapi.mapper;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.UserLoginInfo;

@Component
public interface UserLoginInfoMapper {

	int insert(UserLoginInfo record);

	UserLoginInfo selectByLoginname(String loginname);

	UserLoginInfo selectByUid(String uid);

	int updateByPrimaryKeySelective(UserLoginInfo record);

	int update(UserLoginInfo record);
}