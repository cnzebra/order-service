package com.zyhao.openec.order.exception.pojo;

/**
 * * @author 作者 E-mail: * @date 创建时间：2016年10月12日 下午5:45:48 * @version 1.0
 * * @parameter * @since * @return
 */
public class ArgumentExceptionResponse {
	private String field;
	
	private String errorMsg;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	
	

}
