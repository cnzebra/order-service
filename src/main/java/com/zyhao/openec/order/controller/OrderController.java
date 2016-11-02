package com.zyhao.openec.order.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zyhao.openec.order.entity.Orders;
import com.zyhao.openec.order.entity.RefundOrders;
import com.zyhao.openec.order.pojo.BigOrder;
import com.zyhao.openec.order.repository.OrderRepository;
import com.zyhao.openec.order.service.OrderService;

/**
 * 
 * @author zgy_c
 *
 */
@RestController
@RequestMapping(path = "/v1")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单
	 * 
	 * @param reqOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/new",method=RequestMethod.POST,consumes="application/json")
	public ResponseEntity<String> createOrder(@Validated @RequestBody BigOrder reqOrder) throws Exception {
        return Optional.ofNullable(orderService.createOrder(reqOrder))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find createOrder"));

	}
	
	
	/**
	 * 查询订单列表
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Orders>> queryOrderList(@RequestParam int page, @RequestParam int size,@RequestParam String status) throws Exception {
        return Optional.ofNullable(orderService.getOrderList(page, size,status))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getOrderList"));	
	}

	/**
	 * 查询订单详情
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Orders> getOrder(@PathVariable("id") long id) {
		Orders order = orderRepository.findOne(id);
		return new ResponseEntity<Orders>(order, HttpStatus.OK);
	}

	/**
	 * 修改订单状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path = "/edit/{out_trade_no}", method = RequestMethod.GET)
	public ResponseEntity<List<Orders>> editOrder(@PathVariable("out_trade_no") String out_trade_no,
			@RequestParam String status) {

		List<Orders> orders = orderRepository.findByOutTradeNo(out_trade_no);
		for (Orders order : orders) {
			order.setStatus(status);
		}
		orderRepository.save(orders);
		return new ResponseEntity<List<Orders>>(orders, HttpStatus.OK);
	}
	
	/**
	 * 申请退单
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/newRefund",method=RequestMethod.POST)
	public ResponseEntity<String> createRefundOrder(@Validated @RequestBody RefundOrders refundOrders) throws Exception {
        return Optional.ofNullable(orderService.createRefundOrder(refundOrders))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find createRefundOrder"));	
	}
	
	/**
	 * 退单列表
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/refundList",method=RequestMethod.GET)
	public ResponseEntity<String> getRefundOrderList(@Validated @RequestParam int page, @RequestParam int size) throws Exception {
        return Optional.ofNullable(orderService.getRefundList(page, size))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find createRefundOrder"));	
	}
	
	
}
