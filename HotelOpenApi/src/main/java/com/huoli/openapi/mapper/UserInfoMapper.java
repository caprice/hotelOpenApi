package com.huoli.openapi.mapper;

import java.util.List;

import com.huoli.openapi.vo.data.UserInfo;

public interface UserInfoMapper {
  
    int deleteByPrimaryKey(Integer uid);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer uid);
    
    List<UserInfo> selectByUserinfo(UserInfo record);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}