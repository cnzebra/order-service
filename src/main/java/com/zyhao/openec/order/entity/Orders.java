package com.zyhao.openec.order.entity;

import java.io.Serializable;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
/**
 * Model class of 订单表.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Entity(name="orders")
public class Orders implements Serializable {
	
	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** id. */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;


	/** 订单号 . */
//	@NotNull
	private String orderCode;

	/** 收货人. */
	private String consignee;

	/** 买家地址. */
	private String address;

	/** 联系电话. */
	private String contactTel;

	/** 订单状态. */
	private String status;

	/** 订单提交时间. */
	@CreatedDate
	private Long createdAt;

	/** 最后变更时间. */
	@LastModifiedDate
	private Long modifyAt;

	/** 商户名称. */
	private String sellerName;

	/** 商户id. */
	private Long sellerId;

	/** 渠道. */
	private String channelId;

	/** 用户. */
	private Long memberId;

	/** 商品件数. */
	private Integer goodsCount;

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
	
	/** 是否提醒发货. */
	private String isRemind;
	
	/** 是否出账单. */
	private String isBilled;
	
	
	/** 订单商品List*/
	@NotNull
    // cascade表示级联操作  
    // CascadeType.MERGE级联更新：若items属性修改了那么order对象保存时同时修改items里的对象。对应EntityManager的merge方法  
    // CascadeType.PERSIST级联刷新：获取order对象里也同时也重新获取最新的items时的对象。对应EntityManager的refresh(object)方法有效。即会重新查询数据库里的最新数据  
    // CascadeType.REFRESH级联保存：对order对象保存时也对items里的对象也会保存。对应EntityManager的presist方法  
    // CascadeType.REMOVE级联删除：对order对象删除也对items里的对象也会删除。对应EntityManager的remove方法  
    // FetchType.LAZY表示懒加载。对于xxxtoMany时即获得多的一方fetch的默认值是FetchType.LAZY懒加载。对于xxxtoOne时即获得一的一方fetch的默认值是FetchType.EAGER立即加载  
    // mappedBy表示关系统被维护端，它的值是关系维护端维护关系的属性   mappedBy = "orderId"
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH,  
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinColumn(name="orderCode",referencedColumnName ="orderCode")
	private List<OrderItem> orderItems;

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



	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getModifyAt() {
		return modifyAt;
	}

	public void setModifyAt(Long modifyAt) {
		this.modifyAt = modifyAt;
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
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Get the 订单提交时间.
	 * 
	 * @return 订单提交时间
	 */
	public Long getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Set the 最后变更时间.
	 * 
	 * @param modifyTime
	 *            最后变更时间
	 */
	public void setModifyTime(Long createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Get the 最后变更时间.
	 * 
	 * @return 最后变更时间
	 */
	public Long getModifyTime() {
		return this.createdAt;
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
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	/**
	 * Get the 商品件数.
	 * 
	 * @return 商品件数
	 */
	public Integer getGoodsCount() {
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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
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

	public String getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(String isRemind) {
		this.isRemind = isRemind;
	}

	public String getIsBilled() {
		return isBilled;
	}

	public void setIsBilled(String isBilled) {
		this.isBilled = isBilled;
	}

	@Override
	public String toString() {
		return "Orders [id=" + id + ", orderCode=" + orderCode + ", consignee=" + consignee + ", address=" + address
				+ ", contactTel=" + contactTel + ", status=" + status + ", createdAt=" + createdAt + ", modifyAt="
				+ modifyAt + ", sellerName=" + sellerName + ", sellerId=" + sellerId + ", channelId=" + channelId
				+ ", memberId=" + memberId + ", goodsCount=" + goodsCount + ", realSellPrice=" + realSellPrice
				+ ", invoiceHeader=" + invoiceHeader + ", invoiceContent=" + invoiceContent + ", promotion=" + promotion
				+ ", logistics=" + logistics + ", fareFee=" + fareFee + ", outTradeNo=" + outTradeNo + ", payStatus="
				+ payStatus + ", isRemind=" + isRemind + ", isBilled=" + isBilled + ", orderItems=" + orderItems + "]";
	}



}
