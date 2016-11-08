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
import org.springframework.http.MediaType;
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
		log.info("OrderInfo----------->"+bigOrder);
		
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
			
			//todo 订单金额待计算
			
			tempOrder.setAddress(address);
			tempOrder.setConsignee(consignee);
			tempOrder.setContactTel(contactTel);
			tempOrder.setInvoiceHeader(invoiceContent);
			tempOrder.setInvoiceContent(invoiceHeader);			
			tempOrder.setOrderItems(sellerOrder.getOrderItems());
			
			tempOrder.setOutTradeNo(tradeOutNo);
			orderRepository.save(tempOrder);
			
			
		}
		bigOrder.setTradeOutNo(tradeOutNo);
		bigOrder.setTotalPrice("500");
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
	public Page<Orders> getOrderList(int page, int size,String status) {
		
		User user = getAuthenticatedUser();
		Pageable pageable = new PageRequest(page, size);
		Page<Orders> orderList = orderRepository.findByMemberIdAndStatus(user.getId(),status,pageable);
		return orderList;
		
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
	public List<Orders> getWaitPayOrderDetail(String outTradeNo) {
		return orderRepository.findByOutTradeNo(outTradeNo);
	}
	
}
