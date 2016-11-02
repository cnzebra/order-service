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

	List<Orders> findByOutTradeNo(String out_trade_no);

	Page<Orders> findByMemberIdAndStatus(Long id,String status, Pageable pageable);

}

