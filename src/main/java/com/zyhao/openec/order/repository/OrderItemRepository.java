package com.zyhao.openec.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.zyhao.openec.order.entity.OrderItems;

public interface OrderItemRepository extends PagingAndSortingRepository<OrderItems, Long>{

}
