package com.zyhao.openec.order.pojo;

import java.util.List;

public class BigOrder {
	
	/**
	 * 联系人
	 */
	private String consignee;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 联系电话
	 */
	private String contactTel;
	/**
	 * 发票抬头
	 */
	private String invoiceHeader;
	
	/**
	 * 发票内容
	 */
	private String invoiceContent;
	
	private List<SellerOrder> sellerOrders;

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public List<SellerOrder> getSellerOrders() {
		return sellerOrders;
	}

	public void setSellerOrders(List<SellerOrder> sellerOrders) {
		this.sellerOrders = sellerOrders;
	}

	@Override
	public String toString() {
		return "BigOrder [consignee=" + consignee + ", address=" + address + ", contactTel=" + contactTel
				+ ", invoiceHeader=" + invoiceHeader + ", invoiceContent=" + invoiceContent + ", sellerOrders="
				+ sellerOrders + "]";
	}
	
	
	
}
