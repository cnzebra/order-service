package com.zyhao.openec.order.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.OrderItem;

public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Long>{

	List<OrderItem> findByOrderCode(String orderCode);

}
