package com.zyhao.openec.order.exception.pojo;

/**
 * * @author 作者 E-mail: * @date 创建时间：2016年10月14日 下午8:53:14 * @version 1.0
 * * @parameter * @since * @return
 */
public class CustomerException {
	private String errorCode;
	private String errorMsg;
	
	

	public CustomerException() {
		super();
	}

	public CustomerException(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
