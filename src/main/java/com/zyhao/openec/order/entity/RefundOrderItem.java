package com.zyhao.openec.order.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Model class of 订单项.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Entity(name="refund_order_item")
@SequenceGenerator(name="SEQ_refund_order_item_id",sequenceName="SEQ_refund_order_item_id")
public class RefundOrderItem implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** id. */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_refund_order_item_id")
	private Long id;

	/** 订单号. */
	private Long refundOrderId;

	/** 商品数量. */
	private Integer goodsCount;

	/** 商品SKU. */
	private String sku;

	/** 商品名称. */
	private String productName;

	/** 单价(分). */
	private Integer price;

	/** 商品图(JSON). */
	private String productPic;

	/** 规格(JSON). */
	private String specifications;

	/**
	 * Constructor.
	 */
	public RefundOrderItem() {
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

	
	
	public Long getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(Long refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Set the 商品数量.
	 * 
	 * @param goodsCount
	 *            商品数量
	 */
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}

	/**
	 * Get the 商品数量.
	 * 
	 * @return 商品数量
	 */
	public Integer getGoodsCount() {
		return this.goodsCount;
	}

	/**
	 * Set the 商品SKU.
	 * 
	 * @param sku
	 *            商品SKU
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * Get the 商品SKU.
	 * 
	 * @return 商品SKU
	 */
	public String getSku() {
		return this.sku;
	}

	/**
	 * Set the 商品名称.
	 * 
	 * @param productName
	 *            商品名称
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Get the 商品名称.
	 * 
	 * @return 商品名称
	 */
	public String getProductName() {
		return this.productName;
	}

	/**
	 * Set the 单价(分).
	 * 
	 * @param price
	 *            单价(分)
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}

	/**
	 * Get the 单价(分).
	 * 
	 * @return 单价(分)
	 */
	public Integer getPrice() {
		return this.price;
	}

	/**
	 * Set the 商品图(JSON).
	 * 
	 * @param productPic
	 *            商品图(JSON)
	 */
	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}

	/**
	 * Get the 商品图(JSON).
	 * 
	 * @return 商品图(JSON)
	 */
	public String getProductPic() {
		return this.productPic;
	}

	/**
	 * Set the 规格(JSON).
	 * 
	 * @param specifications
	 *            规格(JSON)
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	/**
	 * Get the 规格(JSON).
	 * 
	 * @return 规格(JSON)
	 */
	public String getSpecifications() {
		return this.specifications;
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
		RefundOrderItem other = (RefundOrderItem) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RefundOrderItem [id=" + id + ", refundOrderId=" + refundOrderId + ", goodsCount=" + goodsCount
				+ ", sku=" + sku + ", productName=" + productName + ", price=" + price + ", productPic=" + productPic
				+ ", specifications=" + specifications + "]";
	}

}
