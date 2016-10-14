package com.zyhao.openec.order.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class of 订单表.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Orders implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** id. */
	private Long id;

	/** 订单号 . */
	private Long orderCode;

	/** 收货人. */
	private String consignee;

	/** 买家地址. */
	private String address;

	/** 联系电话. */
	private String contactTel;

	/** 订单状态. */
	private String status;

	/** 订单提交时间. */
	private Date createTime;

	/** 最后变更时间. */
	private Date modifyTime;

	/** 商户名称. */
	private String sellerName;

	/** 商户id. */
	private Long sellerId;

	/** 渠道. */
	private String channelId;

	/** 用户. */
	private Long memberId;

	/** 商品件数. */
	private Long goodsCount;

	/** 订单金额(分). */
	private Integer realSellPrice;

	/** 发票抬头. */
	private String invoiceHeader;

	/** 发票内容. */
	private String invoiceContent;

	/** 促销信息(JSON). */
	private String promotion;

	/** 物流信息(JSON). */
	private String logistics;

	/** 运费(分). */
	private Long fareFee;

	/** 商户订单号. */
	private String outTradeNo;

	/** 支付状态. */
	private String payStatus;

	/**
	 * Constructor.
	 */
	public Orders() {
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the id.
	 * 
	 * @return id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the 订单号 .
	 * 
	 * @param orderCode
	 *            订单号 
	 */
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * Get the 订单号 .
	 * 
	 * @return 订单号 
	 */
	public Long getOrderCode() {
		return this.orderCode;
	}

	/**
	 * Set the 收货人.
	 * 
	 * @param consignee
	 *            收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * Get the 收货人.
	 * 
	 * @return 收货人
	 */
	public String getConsignee() {
		return this.consignee;
	}

	/**
	 * Set the 买家地址.
	 * 
	 * @param address
	 *            买家地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Get the 买家地址.
	 * 
	 * @return 买家地址
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Set the 联系电话.
	 * 
	 * @param contactTel
	 *            联系电话
	 */
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	/**
	 * Get the 联系电话.
	 * 
	 * @return 联系电话
	 */
	public String getContactTel() {
		return this.contactTel;
	}

	/**
	 * Set the 订单状态.
	 * 
	 * @param status
	 *            订单状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get the 订单状态.
	 * 
	 * @return 订单状态
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Set the 订单提交时间.
	 * 
	 * @param createTime
	 *            订单提交时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Get the 订单提交时间.
	 * 
	 * @return 订单提交时间
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * Set the 最后变更时间.
	 * 
	 * @param modifyTime
	 *            最后变更时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * Get the 最后变更时间.
	 * 
	 * @return 最后变更时间
	 */
	public Date getModifyTime() {
		return this.modifyTime;
	}

	/**
	 * Set the 商户名称.
	 * 
	 * @param sellerName
	 *            商户名称
	 */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	/**
	 * Get the 商户名称.
	 * 
	 * @return 商户名称
	 */
	public String getSellerName() {
		return this.sellerName;
	}

	/**
	 * Set the 商户id.
	 * 
	 * @param sellerId
	 *            商户id
	 */
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * Get the 商户id.
	 * 
	 * @return 商户id
	 */
	public Long getSellerId() {
		return this.sellerId;
	}

	/**
	 * Set the 渠道.
	 * 
	 * @param channelId
	 *            渠道
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * Get the 渠道.
	 * 
	 * @return 渠道
	 */
	public String getChannelId() {
		return this.channelId;
	}

	/**
	 * Set the 用户.
	 * 
	 * @param memberId
	 *            用户
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * Get the 用户.
	 * 
	 * @return 用户
	 */
	public Long getMemberId() {
		return this.memberId;
	}

	/**
	 * Set the 商品件数.
	 * 
	 * @param goodsCount
	 *            商品件数
	 */
	public void setGoodsCount(Long goodsCount) {
		this.goodsCount = goodsCount;
	}

	/**
	 * Get the 商品件数.
	 * 
	 * @return 商品件数
	 */
	public Long getGoodsCount() {
		return this.goodsCount;
	}

	/**
	 * Set the 订单金额(分).
	 * 
	 * @param realSellPrice
	 *            订单金额(分)
	 */
	public void setRealSellPrice(Integer realSellPrice) {
		this.realSellPrice = realSellPrice;
	}

	/**
	 * Get the 订单金额(分).
	 * 
	 * @return 订单金额(分)
	 */
	public Integer getRealSellPrice() {
		return this.realSellPrice;
	}

	/**
	 * Set the 发票抬头.
	 * 
	 * @param invoiceHeader
	 *            发票抬头
	 */
	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	/**
	 * Get the 发票抬头.
	 * 
	 * @return 发票抬头
	 */
	public String getInvoiceHeader() {
		return this.invoiceHeader;
	}

	/**
	 * Set the 发票内容.
	 * 
	 * @param invoiceContent
	 *            发票内容
	 */
	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	/**
	 * Get the 发票内容.
	 * 
	 * @return 发票内容
	 */
	public String getInvoiceContent() {
		return this.invoiceContent;
	}

	/**
	 * Set the 促销信息(JSON).
	 * 
	 * @param promotion
	 *            促销信息(JSON)
	 */
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	/**
	 * Get the 促销信息(JSON).
	 * 
	 * @return 促销信息(JSON)
	 */
	public String getPromotion() {
		return this.promotion;
	}

	/**
	 * Set the 物流信息(JSON).
	 * 
	 * @param logistics
	 *            物流信息(JSON)
	 */
	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	/**
	 * Get the 物流信息(JSON).
	 * 
	 * @return 物流信息(JSON)
	 */
	public String getLogistics() {
		return this.logistics;
	}

	/**
	 * Set the 运费(分).
	 * 
	 * @param fareFee
	 *            运费(分)
	 */
	public void setFareFee(Long fareFee) {
		this.fareFee = fareFee;
	}

	/**
	 * Get the 运费(分).
	 * 
	 * @return 运费(分)
	 */
	public Long getFareFee() {
		return this.fareFee;
	}

	/**
	 * Set the 商户订单号.
	 * 
	 * @param outTradeNo
	 *            商户订单号
	 */
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	/**
	 * Get the 商户订单号.
	 * 
	 * @return 商户订单号
	 */
	public String getOutTradeNo() {
		return this.outTradeNo;
	}

	/**
	 * Set the 支付状态.
	 * 
	 * @param payStatus
	 *            支付状态
	 */
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	/**
	 * Get the 支付状态.
	 * 
	 * @return 支付状态
	 */
	public String getPayStatus() {
		return this.payStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Orders other = (Orders) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
