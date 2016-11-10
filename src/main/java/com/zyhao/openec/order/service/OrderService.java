package com.zyhao.openec.order.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.zyhao.openec.order.entity.Orders;
import com.zyhao.openec.order.entity.PayInfo;
import com.zyhao.openec.order.entity.RefundOrders;
import com.zyhao.openec.order.entity.User;
import com.zyhao.openec.order.pojo.BigOrder;
import com.zyhao.openec.order.pojo.SellerOrder;
import com.zyhao.openec.order.repository.OrderRepository;
import com.zyhao.openec.order.repository.RefundOrderRepository;
import com.zyhao.openec.order.util.RepEntity;

/**
 * 
 * Title:OrderService
 * Desc: 支付服务功能
 * @author Administrator
 * @date 2016年10月14日 下午3:29:38
 */
@Service
public class OrderService {
	private final Log log = LogFactory.getLog(OrderService.class);
	private OAuth2RestTemplate oAuth2RestTemplate;
    private RestTemplate restTemplate;
    
    @Autowired
    public OrderService(@LoadBalanced OAuth2RestTemplate oAuth2RestTemplate,
        @LoadBalanced RestTemplate normalRestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.restTemplate = normalRestTemplate;
    }
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RefundOrderRepository refundOrderRepository;
	
	/**
	 * 认证平台
	 * @return
	 */
	public User getAuthenticatedUser() {
        return oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
    }

	public String getTradeOutNo(String channelId) {
		String getPayInfoCode = oAuth2RestTemplate.getForObject("http://payment-service/v1/getPayInfoCode?channel_id="+channelId, String.class);		
		log.info("getTradeOutNo is "+getPayInfoCode);
		return getPayInfoCode;
	}

	public String createPayInfo(BigOrder reqOrder) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		
		JSONObject json = new JSONObject();
		json.put("outTradeNo", reqOrder.getTradeOutNo());
		json.put("totalPrice", reqOrder.getTotalPrice());
		json.put("payPrice", reqOrder.getTotalPrice());
		json.put("userId", getAuthenticatedUser().getId());
		json.put("payType","1");//1-现金支付 2-货到付款
		json.put("channelId", reqOrder.getChannelId());

        log.info("createPayInfo method call order method param is "+json);
        
	    HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
		String createPayInfo = oAuth2RestTemplate.postForObject("http://payment-service/v1/createPayInfo",formEntity, String.class);		
		log.info("createPayInfo is "+createPayInfo);
		return createPayInfo;
	}
	
	/**
	 * 生成订单
	 * @throws Exception 
	 */
	@Transactional
	public BigOrder createOrder(BigOrder bigOrder) throws Exception{		
		String tradeOutNo = getTradeOutNo(bigOrder.getChannelId());
		
		String address = bigOrder.getAddress();
		String consignee = bigOrder.getConsignee();
		String contactTel = bigOrder.getContactTel();
		String invoiceContent = bigOrder.getInvoiceContent();
		String invoiceHeader = bigOrder.getInvoiceHeader();
		
		for(SellerOrder sellerOrder : bigOrder.getSellerOrders()){
			Orders tempOrder = new Orders();
			Long currTime = System.currentTimeMillis();
			//订单号生成算法待完善
			
			tempOrder.setOrderCode(""+currTime);
			
			tempOrder.setCreatedAt(currTime);

			tempOrder.setStatus("0");
			
			tempOrder.setMemberId(getAuthenticatedUser().getId());
			
			tempOrder.setRealSellPrice(sellerOrder.getRealSellPrice());
			
			tempOrder.setGoodsCount(sellerOrder.getGoodsCount());
			
			tempOrder.setSellerId(sellerOrder.getSellerId());
			
			tempOrder.setSellerName(sellerOrder.getSellerName());
				
			tempOrder.setChannelId(bigOrder.getChannelId());
			
			tempOrder.setAddress(address);
			tempOrder.setConsignee(consignee);
			tempOrder.setContactTel(contactTel);
			tempOrder.setInvoiceHeader(invoiceContent);
			tempOrder.setInvoiceContent(invoiceHeader);	
			
			tempOrder.setOrderItems(sellerOrder.getOrderItems());
			
			tempOrder.setOutTradeNo(tradeOutNo);
			tempOrder.setPayStatus("0"); 

			log.info("tempOrder----------->"+tempOrder.toString());
			
			orderRepository.save(tempOrder);
			
			
		}
		bigOrder.setTradeOutNo(tradeOutNo);
		bigOrder.setTotalPrice(bigOrder.getTotalPrice());
		/**调用支付生成支付信息*/
		createPayInfo(bigOrder);


//		return new ResponseEntity<String>(String.valueOf(order.getId()),HttpStatus.OK);
		
		//返回bigOrder
		return bigOrder;
	}

	/**
	 * 申请退单
	 * @param refundOrders
	 * @return
	 */
	public RefundOrders createRefundOrder(RefundOrders refundOrders) {
		// TODO Auto-generated method stub
		User user = getAuthenticatedUser();
		
		Long currTime = System.currentTimeMillis();
		
		refundOrders.setRefundOrderCode(""+currTime);
		
		refundOrders.setCreateAt(currTime);
		
		refundOrders.setMemberId(user.getId());
		
		return refundOrderRepository.save(refundOrders);
		
	}
	
	/**
	 * 退单列表
	 * @param refundOrders
	 * @return
	 */
	public Page<RefundOrders> getRefundList(int page,int size) {
		
		User user = getAuthenticatedUser();
		Pageable pageable = new PageRequest(page, size);
		Page<RefundOrders> refundOrderList = refundOrderRepository.findByMemberId(user.getId(),pageable);
		return refundOrderList;

	}

	/**
	 * 订单列表(按状态)
	 * @param page
	 * @param size
	 * @return
	 */
	public RepEntity getOrderList(int page, int size,String status) {
		page = page - 1;
		RepEntity resp = new RepEntity();
		try{
			User user = getAuthenticatedUser();
			Pageable pageable = new PageRequest(page, size);
			Page<Orders> orderList = orderRepository.findByMemberIdAndStatus(user.getId(),status,pageable);
			
			resp.setMsg("订单列表查询成功");
			resp.setStatus("0");
			resp.setData(orderList);
			
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setMsg("订单列表查询失败");
			resp.setStatus("-1");
			return resp;
		}	
	}
	
	/**
	 * 待支付订单列表
	 * @param page
	 * @param size
	 * @return
	 */
	public List<PayInfo> getWaitPayOrderList(int page, int size) {
		
//		/api/payment/v1/getPayInfo/noPayment
		PayInfo[] waitPayInfoAry = restTemplate.getForObject("http://payment-service/v1/getPayInfo/noPayment",PayInfo[].class);
		
		List<PayInfo> waitPayInfoList = Arrays.asList(waitPayInfoAry);
		 			
		//todo
//		User user = getAuthenticatedUser();
//		Pageable pageable = new PageRequest(page, size);
//		Page<Orders> orderList = orderRepository.findByMemberId(user.getId(),pageable);
		return waitPayInfoList;
		
	}
	
	/**
	 * 待支付订单详情
	 * @param page
	 * @param size
	 * @return
	 */
	public RepEntity getWaitPayOrderDetail(String outTradeNo) {
		RepEntity resp = new RepEntity();
		try{
			User user = getAuthenticatedUser();
			List<Orders> orders = orderRepository.findByMemberIdAndOutTradeNo(user.getId(),outTradeNo);
			resp.setStatus("0");
			resp.setMsg("查询成功");
			resp.setData(orders);
			
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("详情查询失败");
			return resp;
		}

	}
	
	
	/**
	 * 订单详情
	 * @param page
	 * @param size
	 * @return
	 */
	public RepEntity getOrderByOrderCode(String orderCode){
		RepEntity resp = new RepEntity();
		try{
			User user = getAuthenticatedUser();
			Orders order = orderRepository.findByMemberIdAndOrderCode(user.getId(),orderCode);
			
			resp.setStatus("0");
			resp.setMsg("详情查询成功");
			resp.setData(order);
			
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("详情查询失败");
			return resp;
		}

	}

	/**
	 * 修改订单状态
	 * @param orderCode
	 * @param status
	 * @return
	 */
	public RepEntity editOrderStatus(String orderCode, String status) {
		RepEntity resp = new RepEntity();
		try{
			User user = getAuthenticatedUser();
			Orders order = orderRepository.findByMemberIdAndOrderCode(user.getId(),orderCode);
			
			/**
			 * 3 状态为确认收货 , 5状态为取消订单
			 */
			if(!(status.equals("3") || status.equals("5"))){
				
				resp.setStatus("-1");
				resp.setMsg("订单状态修改失败,状态不允许");
				
				return resp;
				
			}
			
			order.setStatus(status);
			orderRepository.save(order);
			
			resp.setStatus("0");
			resp.setMsg("订单状态修改成功");
			resp.setData(order);
			
			return resp;
			
			
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("订单状态修改失败");
			return resp;
		}

		
	}
	

}
