package com.zyhao.openec.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.RefundOrders;

public interface RefundOrderRepository extends PagingAndSortingRepository<RefundOrders, Long>{
	
	public Page<RefundOrders> findByMemberIdAndType(Long memberId,String type,Pageable p);
	
	public Page<RefundOrders> findByMemberIdAndTypeAndStatus(Long memberId,String type,String status,Pageable p);
	
	public RefundOrders findByMemberIdAndRefundOrderCode(Long memberId,String refundOrderCode);
	
	
}
