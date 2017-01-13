package com.zyhao.openec.order.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.zyhao.openec.order.entity.Inventory;
import com.zyhao.openec.order.entity.OrderItem;
import com.zyhao.openec.order.entity.Orders;
import com.zyhao.openec.order.entity.RefundOrders;
import com.zyhao.openec.order.entity.User;
import com.zyhao.openec.order.pojo.BigOrder;
import com.zyhao.openec.order.pojo.SellerOrder;
import com.zyhao.openec.order.service.OrderService;
import com.zyhao.openec.pojo.RepEntity;

/**
 * 
 * @author zgy_c
 *
 */
@RestController
@RequestMapping(path = "/v1")
public class OrderController {
    private Log log = LogFactory.getLog(OrderController.class);
	
	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单
	 * 
	 * @param reqOrder
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="/new",method=RequestMethod.POST)
	public ResponseEntity createOrder(@Validated @RequestBody BigOrder reqOrder) throws Exception {
		RepEntity response = new RepEntity();
	    log.info("createOrder method run params is "+reqOrder);
		//判断是否登陆
		User authenticatedUser = orderService.getAuthenticatedUser();
		if(authenticatedUser == null || authenticatedUser.getId() == null){
			
			response.setData("");
			response.setMsg("请登录");
			response.setStatus("1");
			
			return new ResponseEntity(response,HttpStatus.NOT_FOUND);
		}
		
		Integer totalPrice = 0;//总支付价格
		//初始化订单信息
		List<String> inventoryList = new ArrayList<String>();
		Map<String,OrderItem> mapper = new HashMap<String,OrderItem>();
		List<SellerOrder> sellerOrders = reqOrder.getSellerOrders();
		String tradeOutNo = orderService.getTradeOutNo(reqOrder.getChannelId());
		
		List<Orders> orders = new ArrayList<Orders>();
		List<OrderItem> orderitems = new ArrayList<OrderItem>();
		log.info("createOrder method run tradeOutNo is "+tradeOutNo+ "reqOrder is " +reqOrder);
		
		for (SellerOrder sellerOrder : sellerOrders) {
			inventoryList.clear();
			Integer orderPrice = 0;
			//获取订单的编码
			String orderCode = orderService.getOrderCode();
			
			//判断商品库存
			List<OrderItem> orderItems = sellerOrder.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				if(orderItem.getSku() != null){
					mapper.put(orderItem.getSku(),orderItem);
					inventoryList.add(orderItem.getSku());
					orderItem.setOrderCode(orderCode);
				}else{
					response.setData("product is null, cannot creater order");
					response.setMsg("product is null, cannot creater order");
					response.setStatus("1");
					
					return new ResponseEntity(response,HttpStatus.NOT_FOUND);
					
				}
				orderitems.add(orderItem);
			}
			
			log.info("createOrder method run tradeOutNo is "+tradeOutNo+" 调用库存接口,获取商品库存 reqOrder is " +reqOrder);
			
			//调用库存接口,获取商品库存
			Inventory[] inventoryBySKUS = orderService.getInventoryBySKUS(inventoryList);
			if(inventoryBySKUS == null){
				
				response.setData("query inventory failed, cannot creater order");
				response.setMsg("query inventory failed, cannot creater order");
				response.setStatus("1");
				
				return new ResponseEntity(response,HttpStatus.NOT_FOUND);
				
			}
			
			//查询商品价格和库存校验
			for(int i=0;i<inventoryBySKUS.length;i++){
				Inventory inventory = inventoryBySKUS[i];
				OrderItem orderItem = mapper.get(inventory.getSku());
				if(orderItem.getGoodsCount() > inventory.getAmount()){//判断库存
				    response.setData(inventory.getSku()+"Not enough stock, cannot creater order");
					response.setMsg(inventory.getSku()+"Not enough stock, cannot creater order");
					response.setStatus("1");
					
					return new ResponseEntity(response,HttpStatus.NOT_FOUND);
					
				}else{
					//计算价格
					totalPrice = totalPrice + inventory.getPrice();//总售价格
					orderPrice = orderPrice + inventory.getPrice();//订单价格
				}
			}
			
			//设置订单支付金额
			Orders tempOrder = new Orders();
			//订单号生成算法待完善  订单编码
			tempOrder.setOrderCode(orderCode);
			tempOrder.setCreatedAt(new Date().getTime());
			tempOrder.setStatus("0");
			tempOrder.setMemberId(authenticatedUser.getId());
			tempOrder.setRealSellPrice(sellerOrder.getRealSellPrice());
			tempOrder.setGoodsCount(sellerOrder.getGoodsCount());
			tempOrder.setSellerId(sellerOrder.getSellerId());
			tempOrder.setSellerName(sellerOrder.getSellerName());
			tempOrder.setChannelId(reqOrder.getChannelId());
			tempOrder.setAddress(reqOrder.getAddress());
			tempOrder.setConsignee(reqOrder.getConsignee());
			tempOrder.setContactTel(reqOrder.getContactTel());
			tempOrder.setInvoiceHeader(reqOrder.getInvoiceHeader());
			tempOrder.setInvoiceContent(reqOrder.getInvoiceContent());	
			tempOrder.setOutTradeNo(tradeOutNo);
			tempOrder.setPayStatus("0"); 
			tempOrder.setIsRemind("0");
			tempOrder.setIsBilled("F");
			
			orders.add(tempOrder);
		}
		reqOrder.setTradeOutNo(tradeOutNo);
		reqOrder.setTotalPrice(totalPrice);
		log.info("createOrder method run tradeOutNo is "+tradeOutNo+ "reqOrder is " +reqOrder+" orders = "+orders+" orderitems is "+orderitems);
        orderService.createOrder(reqOrder, orders, orderitems);
		mapper.clear();
		mapper = null;
		response.setData(reqOrder);
		response.setMsg("creater order success");
		response.setStatus("0");
			
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	/**
	 * 查询订单列表
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path="/orderList",method = RequestMethod.GET)
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
	@RequestMapping(path = "/{orderCode}", method = RequestMethod.GET)
	public ResponseEntity<Orders> getOrder(@PathVariable("orderCode") String orderCode) throws Exception {
        return Optional.ofNullable(orderService.getOrderByOrderCode(orderCode))
                .map(order -> new ResponseEntity(order,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getOrder"));
	}

	/**
	 * 待支付订单详情
	 * @param outTradeNo
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/waitPayOrder/{outTradeNo}",method=RequestMethod.GET)
	public ResponseEntity<String> getWaitPayOrderDetail(@Validated @PathVariable("outTradeNo") String outTradeNo) throws Exception {
        return Optional.ofNullable(orderService.getWaitPayOrderDetail(outTradeNo))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getWaitPayOrderDetail"));	
	}
	
	/**
	 * 修改订单支付状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path = "/edit/{out_trade_no}", method = RequestMethod.GET)
	public ResponseEntity<List<Orders>> editOrder(@PathVariable("out_trade_no") String out_trade_no,
			@RequestParam String status,String orderstatus) throws Exception {
        return Optional.ofNullable(orderService.editOrderPayStatus(out_trade_no,status,orderstatus))
                .map(orders -> new ResponseEntity(orders,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getWaitPayOrderDetail"));
	}
	
	/**
	 * 修改订单状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path = "/editStatus/{orderCode}", method = RequestMethod.GET)
	public ResponseEntity<Orders> editOrderStatus(@PathVariable("orderCode") String orderCode,
			@RequestParam String status) throws Exception {

        return Optional.ofNullable(orderService.editOrderStatus(orderCode,status))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getWaitPayOrderDetail"));	
	}
	
	/**
	 * 删除订单
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path = "/delete/{orderCode}", method = RequestMethod.GET)
	public ResponseEntity<Orders> deleteOrder(@PathVariable("orderCode") String orderCode) throws Exception {

        return Optional.ofNullable(orderService.deleteOrder(orderCode))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getWaitPayOrderDetail"));	
	}
	
	/**
	 * 待支付订单列表
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/waitPayOrderList",method=RequestMethod.GET)
	public ResponseEntity<String> getWaitPayOrderList(@Validated @RequestParam String outTradeNos) throws Exception {
        return Optional.ofNullable(orderService.getWaitPayOrderList(outTradeNos))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getWaitPayOrderList"));	
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
	public ResponseEntity<String> getRefundOrderList(@Validated @RequestParam int page, @RequestParam int size,@RequestParam String type) throws Exception {
        return Optional.ofNullable(orderService.getRefundList(page, size,type))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getRefundOrderList"));	
	}
	
	/**
	 * 退单列表(按状态)
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/refundListByStatus",method=RequestMethod.GET)
	public ResponseEntity<String> getRefundOrderListByStatus(@Validated @RequestParam int page, @RequestParam int size,@RequestParam String type,@RequestParam String status) throws Exception {
        return Optional.ofNullable(orderService.getRefundListByStatus(page, size,type,status))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find getRefundOrderList"));	
	}
	
	/**
	 * 退单审核
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/refundVerify/{refundCode}",method=RequestMethod.GET)
	public ResponseEntity<String> modifyRefundStatus(@Validated @PathVariable("refundCode") String refundCode,@RequestParam String status, @RequestParam String refundOpinion) throws Exception {
        return Optional.ofNullable(orderService.modifyRefundStatus(refundCode, status, refundOpinion))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find modifyRefundStatus"));	
	}
	
	/**
	 * 退单详情
	 * @param reqRefundOrder
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	@RequestMapping(path="/refundDetail/{refundCode}",method=RequestMethod.GET)
	public ResponseEntity<String> getRefundDetail(@Validated @PathVariable("refundCode") String refundCode) throws Exception {
        return Optional.ofNullable(orderService.getRefundDetail(refundCode))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find modifyRefundStatus"));	
	}
	
	/**
	 * 提醒发货
	 * @param refundCode
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	@RequestMapping(path="/remind/{orderCode}",method=RequestMethod.GET)
	public ResponseEntity<String> setIsRemind(@Validated @PathVariable("orderCode") String orderCode) throws Exception {
        return Optional.ofNullable(orderService.setIsRemind(orderCode))
                .map(bigOrder -> new ResponseEntity(bigOrder,HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find modifyRefundStatus"));	
	}
	
}
