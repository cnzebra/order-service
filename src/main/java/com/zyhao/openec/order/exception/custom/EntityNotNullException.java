package com.zyhao.openec.order.exception.custom;

/**
 * * @author 作者 E-mail: * @date 创建时间：2016年10月14日 下午8:12:10 * @version 1.0
 * * @parameter * @since * @return
 */
public class EntityNotNullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EntityNotNullException() {
	}

	public EntityNotNullException(String message) {
		super(message);
	}

}
