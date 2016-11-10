package com.zyhao.openec.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.Orders;



/**
 * 
 * @author zgy_c
 *
 */
public interface OrderRepository extends PagingAndSortingRepository<Orders, Long> {
	
	
	/**
	 * 通过支付流水号查询订单,(可作为待支付订单详情)
	 * @param id
	 * @param outTradeNo
	 * @return
	 */
	List<Orders> findByMemberIdAndOutTradeNo(Long id,String outTradeNo);
	
	
	/**
	 * 通过订单号查询订单详情
	 * @param id
	 * @param orderCode
	 * @return
	 */
	Orders findByMemberIdAndOrderCode(Long id,String orderCode);
	
	/**
	 * 按状态查询订单列表
	 * @param id
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<Orders> findByMemberIdAndStatus(Long id,String status, Pageable pageable);

	
	/**
	 * 按交易流水号查询订单列表,(可作为待支付订单详情,pageable方式)
	 * @param id
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<Orders> findByMemberIdAndOutTradeNo(Long id,String outTradeNo, Pageable pageable);
	
	
}

