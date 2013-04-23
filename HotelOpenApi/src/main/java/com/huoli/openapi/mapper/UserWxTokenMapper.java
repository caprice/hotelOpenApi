package com.huoli.openapi.mapper;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.UserWxToken;

@Component
public interface UserWxTokenMapper {
	public UserWxToken selectByToken(String token);
}
