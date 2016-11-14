package com.zyhao.openec.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.OrderItem;

public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Long>{

}
