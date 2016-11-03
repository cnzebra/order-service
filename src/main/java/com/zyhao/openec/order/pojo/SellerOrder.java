package com.zyhao.openec.order.pojo;

import java.util.List;

import com.zyhao.openec.order.entity.OrderItem;

public class SellerOrder {
	
	private String sellerName;
	private Long sellerId;
	private Integer goodsCount;
	
	private List<OrderItem> OrderItems;

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	public List<OrderItem> getOrderItems() {
		return OrderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		OrderItems = orderItems;
	}

	@Override
	public String toString() {
		return "SellerOrder [sellerName=" + sellerName + ", sellerId=" + sellerId + ", goodsCount=" + goodsCount
				+ ", OrderItems=" + OrderItems + "]";
	}

	
}
