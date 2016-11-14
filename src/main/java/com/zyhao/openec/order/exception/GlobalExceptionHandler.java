package com.zyhao.openec.order.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zyhao.openec.order.exception.custom.EntityNotNullException;
import com.zyhao.openec.order.exception.pojo.ArgumentExceptionResponse;
import com.zyhao.openec.order.exception.pojo.CustomerException;

/**
 * * @author 作者 E-mail: * @date 创建时间：2016年10月12日 下午5:40:59 * @version 1.0
 * * @parameter * @since * @return
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	ArgumentExceptionResponse aer;
	CustomerException ce;

	/**
	 * 统一参数验证异常
	 * 
	 * @param e
	 * @param response
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	// @ExceptionHandler(value={RuntimeException.class,MyRuntimeException.class})
	// @ExceptionHandler//处理所有异常
	@ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
	public List<ArgumentExceptionResponse> exceptionHandler(MethodArgumentNotValidException e,
			HttpServletResponse response) {
		BindingResult br = e.getBindingResult();
		List<FieldError> list = br.getFieldErrors();
		List<ArgumentExceptionResponse> aerList = new ArrayList<ArgumentExceptionResponse>();
		for (FieldError fieldError : list) {
			aer = new ArgumentExceptionResponse();
			aer.setField(fieldError.getField());
			aer.setErrorMsg(fieldError.getDefaultMessage());
			aerList.add(aer);
		}
		response.setStatus(403);
		return aerList;
	}

	/**
	 * 捕获请求json映射异常
	 * 
	 * @param e
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = { JsonMappingException.class })
	public ResponseEntity<CustomerException> exceptionHandler1(JsonMappingException e, HttpServletResponse response) {
		return new ResponseEntity<CustomerException>(new CustomerException("-101", "json映射异常，请检查数据类型"),
				HttpStatus.BAD_REQUEST);

	}

	/**
	 * 捕获请求json解析异常
	 * 
	 * @param e
	 * @param response
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = { JsonParseException.class })
	public ResponseEntity<CustomerException> exceptionHandler2(JsonParseException e, HttpServletResponse response) {
		// 处理json解析异常（json格式错误）
		return new ResponseEntity<CustomerException>(new CustomerException("-102", "json解析异常，请检查json格式时候正确"),
				HttpStatus.BAD_REQUEST);
	}

	@ResponseBody
	@ExceptionHandler(value = { EntityNotNullException.class })
	public ResponseEntity<CustomerException> exceptionHandler3(EntityNotNullException e, HttpServletResponse response) {
		response.setStatus(400);
		return new ResponseEntity<CustomerException>(new CustomerException("-103", "请求对象不存在"),
				HttpStatus.BAD_REQUEST);
	}

}
