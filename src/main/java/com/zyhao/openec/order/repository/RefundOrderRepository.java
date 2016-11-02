package com.zyhao.openec.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.RefundOrders;

public interface RefundOrderRepository extends PagingAndSortingRepository<RefundOrders, Long>{
	
	Page<RefundOrders> findByMemberId(Long memberId,Pageable p);
	
}
