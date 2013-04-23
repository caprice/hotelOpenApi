package com.huoli.openapi.orderService;

public class OrderDetailResult {
    public String resultCode;
    public String description;
    private OrderVo orderVo;

    public String getResultCode() {
        return resultCode;
    }

    public String getDescription() {
        return description;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderVo getOrderVo() {
        return orderVo;
    }

    public void setOrderVo(OrderVo orderVo) {
        this.orderVo = orderVo;
    }

}
