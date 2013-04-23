package com.huoli.openapi.mapper;

import org.springframework.stereotype.Component;

import com.huoli.openapi.vo.data.OrderVerify;

@Component
public interface OrderVerifyMapper {
	public OrderVerify selectOrderVerifyByKid(String kid);

	public int insert(OrderVerify order);

	public int updateStatus(OrderVerify order);

	public int updateVerify(OrderVerify order);
}
