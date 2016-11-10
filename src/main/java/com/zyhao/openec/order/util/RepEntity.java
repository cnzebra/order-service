package com.zyhao.openec.order.util;

/**
 * * @author 作者 E-mail: * @date 创建时间：2016年11月2日 下午6:53:49 * @version 1.0
 * * @parameter * @since * @return
 */
public class RepEntity {
	
	private String status;
	
	private String msg;
	
	private Object data;
	
	public RepEntity() {
		super();
	}

	public RepEntity(String status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	

}
