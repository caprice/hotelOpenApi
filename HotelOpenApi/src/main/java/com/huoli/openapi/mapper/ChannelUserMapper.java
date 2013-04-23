package com.huoli.openapi.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.ChannelUser;
import com.huoli.openapi.vo.result.ChainInfo;

@Component
public interface ChannelUserMapper {
	public ChannelUser selectChannelUserInfo(Long channelId);

	public List<ChannelUser> selectAllChannelUserInfo();

	public List<ChainInfo> selectChainList();
}
