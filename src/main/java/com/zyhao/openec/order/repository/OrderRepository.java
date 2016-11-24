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
	 * @param memberId
	 * @param outTradeNo
	 * @return
	 */
	List<Orders> findByMemberIdAndOutTradeNo(Long memberId,String outTradeNo);
	
	
	/**
	 * 通过订单号查询订单详情
	 * @param memberId
	 * @param orderCode
	 * @return
	 */
	Orders findByMemberIdAndOrderCode(Long memberId,String orderCode);
	
	/**
	 * 按状态查询订单列表
	 * @param memberId
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<Orders> findByMemberIdAndStatus(Long memberId,String status, Pageable pageable);

	
	/**
	 * 按交易流水号查询订单列表,(可作为待支付订单详情,pageable方式)
	 * @param memberId
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<Orders> findByMemberIdAndOutTradeNo(Long memberId,String outTradeNo, Pageable pageable);
	
//	/**
//	 * 查询某人全部订单(pageable方式)
//	 * @param memberId
//	 * @param status
//	 * @param pageable
//	 * @return
//	 */
//	Page<Orders>
	
	/**
	 * 批量返回多条订单数据
	 * @param outTradeNos
	 * @return
	 */
	public List<Orders> findByOutTradeNoIn(List<String> outTradeNos);
	
	/**
	 * 排除某状态查询订单列表
	 * @param memberId
	 * @param status
	 * @param pageable
	 * @return
	 */
	public Page<Orders> findByMemberIdAndStatusNotIn(Long memberId,List<String> status, Pageable pageable);
	
}

