package com.zyhao.openec.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.zyhao.openec.entity.Store;
import com.zyhao.openec.order.entity.Inventory;
import com.zyhao.openec.order.entity.OrderItem;
import com.zyhao.openec.order.entity.Orders;
import com.zyhao.openec.order.entity.RefundOrderItem;
import com.zyhao.openec.order.entity.RefundOrders;
import com.zyhao.openec.order.pojo.BigOrder;
import com.zyhao.openec.order.repository.OrderItemRepository;
import com.zyhao.openec.order.repository.OrderRepository;
import com.zyhao.openec.order.repository.RefundOrderRepository;
import com.zyhao.openec.pojo.MachineCode;
import com.zyhao.openec.pojo.RepEntity;
import com.zyhao.openec.util.UniqueCodeUtil;

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
//	private OAuth2RestTemplate oAuth2RestTemplate;
    private RestTemplate restTemplate;
    
    @Autowired
    public OrderService(
//    		@LoadBalanced OAuth2RestTemplate oAuth2RestTemplate,
        @LoadBalanced RestTemplate normalRestTemplate) {
//        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.restTemplate = normalRestTemplate;
    }
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RefundOrderRepository refundOrderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private UniqueCodeUtil uniqueCodeUtil;
	@Autowired
	private MachineCode machineCode;
	/**
	 * 认证平台
	 * @return
	 */
//	public User getAuthenticatedUser() {
//        return oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
//    }
	
	@Autowired  
    HttpServletRequest request;
	/**
	 * 认证平台
	 * @return
	 */
	public Map<String,String[]> getAuthenticatedUser() {
		return request.getParameterMap();
	}
	public String getTradeOutNo(String channelId) {
		String getPayInfoCode = restTemplate.getForObject("http://payment-service/v1/getPayInfoCode?channel_id="+channelId, String.class);		
		log.info("getTradeOutNo is "+getPayInfoCode);
		return getPayInfoCode;
	}

	public String createPayInfo(HttpServletRequest request,BigOrder reqOrder) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		
		JSONObject json = new JSONObject();
		json.put("outTradeNo", reqOrder.getTradeOutNo());
		json.put("totalPrice", reqOrder.getTotalPrice());
		json.put("payPrice", reqOrder.getTotalPrice());
		json.put("userId", getAuthenticatedUser().get("Session_id")[0]);
		json.put("payType","1");//1-现金支付 2-货到付款
		json.put("channelId", reqOrder.getChannelId());
		json.put("payStatus", "0");
		
        log.info("createPayInfo method call order method param is "+json);
        
	    HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
		String createPayInfo = restTemplate.postForObject("http://payment-service/v1/createPayInfo",formEntity, String.class);		
		log.info("createPayInfo is "+createPayInfo);
		return createPayInfo;
	}
	
	/**
	 * 生成订单
	 * @throws Exception 
	 */
	@Transactional
	public BigOrder createOrder(HttpServletRequest request,BigOrder bigOrder,List<Orders>orders,List<OrderItem> orderItems) throws Exception{
		//保存订单信息
		if(orders != null){
	        orderRepository.save(orders);
		}
		//保存订单项信息
	    if(orderItems != null){
		    orderItemRepository.save(orderItems);
	    }
		//保存支付信息
	    if(bigOrder != null){
		    createPayInfo(request,bigOrder);
	    }
		return bigOrder;
	}

	public String getOrderCode() {
		return uniqueCodeUtil.getUniqueCode(machineCode);
	}

	/**
	 * 申请退单
	 * @param refundOrders
	 * @return
	 */
	public RepEntity createRefundOrder(HttpServletRequest request,RefundOrders refundOrders) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> userId = getAuthenticatedUser();
			
			Long currTime = System.currentTimeMillis();
			
			/** 退单号. */
			refundOrders.setRefundOrderCode(""+currTime);
			
			/** 订单提交时间. */
			refundOrders.setCreateAt(currTime);
			
			/** 用户id. */
			refundOrders.setMemberId(userId.get("Session_id")[0]);
			
			/** 最后变更时间. */
			refundOrders.setModifyAt(currTime);

			/** 退单状态. */
			refundOrders.setStatus("0");
			
			/** 是否对账. */
			refundOrders.setIsBilled("F");
			
			/** 商户订单号(通过原订单号查询回来). */
			Orders reOrder = orderRepository.findByMemberIdAndOrderCode(userId.get("Session_id")[0], refundOrders.getOrderCode());
			if(reOrder == null){
				resp.setStatus("-1");
				resp.setMsg("原订单查询失败,请检查订单号");
				return resp;
			}
			refundOrders.setOutTradeNo(reOrder.getOutTradeNo()); 
			
			/** 退单金额(分)(通过SKU获取商品金额). */
			
			List<RefundOrderItem> refundOrderItems = refundOrders.getRefundOrderItems();
			
			List<String> skus = refundOrderItems.stream().map(refundOrderItem -> refundOrderItem.getSku()).collect(Collectors.toList());
			
			
			
			Inventory[] inventorys = getInventoryBySKUS(new ArrayList(Arrays.asList(skus.toArray())));
			
			//设置价格
			int realSellPrice = 0;
			for (Inventory _inventory : inventorys) {
				for(RefundOrderItem refundOrderItem:refundOrderItems){
					if(_inventory.getSku().equals(refundOrderItem.getSku())){
						//设置价格
						refundOrderItem.setPrice(_inventory.getPrice());
						
						//设置图片
						refundOrderItem.setProductPic(_inventory.getPictures());
						
						//设置规格
						refundOrderItem.setSpecifications(_inventory.getSpecs());
						
						//设置退货价格
						realSellPrice += _inventory.getPrice()*refundOrderItem.getGoodsCount();
					}
				}
			}
			
			//设置退款总价
			refundOrders.setRealSellPrice(realSellPrice);
			
			resp.setStatus("0");
			resp.setMsg("退单申请成功");
			resp.setData(refundOrderRepository.save(refundOrders));
			
			return resp;
			
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("退单申请失败");
			return resp;
		}

	}
	
	/**
	 * 通过SKU数组获取库存集合
	 */
	public Inventory[] getInventoryBySKUS(List<String> reqAry) throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);				
		HttpEntity<List<String>> formEntity = new HttpEntity<List<String>>(reqAry, headers);		
		Inventory[] inventoryAry = restTemplate.postForObject("http://inventory-service/v1/batch",formEntity, Inventory[].class);
		
		return inventoryAry;
	}
	
	/**
	 * 退单列表
	 * @param 
	 * @return
	 */
	public RepEntity getRefundList(HttpServletRequest request,int page,int size,String type) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> userId = getAuthenticatedUser();
			Pageable pageable = new PageRequest(page, size);
			Page<RefundOrders> refundOrderList = refundOrderRepository.findByMemberIdAndType(userId.get("Session_id")[0],type,pageable);
			resp.setData(refundOrderList);
			resp.setMsg("退单列表获取成功");
			resp.setStatus("0");
			return resp;
			
		}catch(Exception e){
			e.printStackTrace();
			resp.setMsg("退单列表获取失败");
			resp.setStatus("-1");
			return resp;
		}

	}

	/**
	 * 退单详情
	 * @param 
	 * @return
	 */
	public RepEntity getRefundDetail(HttpServletRequest request,String refundCode){
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> userId = getAuthenticatedUser();
		
			RefundOrders refundOrder =  refundOrderRepository.findByMemberIdAndRefundOrderCode(userId.get("Session_id")[0],refundCode);
		
			resp.setData(refundOrder);
			resp.setMsg("退单详情获取成功");
			resp.setStatus("0");
			return resp;
		
		}catch(Exception e){
			e.printStackTrace();
			resp.setMsg("退单详情获取失败");
			resp.setStatus("-1");
			return resp;
		}
	}
	
	/**
	 * 退单审核
	 * @param 
	 * @return
	 */
	public RepEntity modifyRefundStatus(HttpServletRequest request,String refundCode,String status,String refundOpinion){
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			
			RefundOrders refundOrder =  refundOrderRepository.findByMemberIdAndRefundOrderCode(userId,refundCode);
			
			refundOrder.setStatus(status);
			
			refundOrder.setRefundOpinion(refundOpinion);
			
			RefundOrders _refundOrder = refundOrderRepository.save(refundOrder);
			
			resp.setData(_refundOrder);
			resp.setMsg("退单审核成功");
			resp.setStatus("0");
			return resp;	
		}catch(Exception e){
			e.printStackTrace();
			resp.setMsg("退单审核失败");
			resp.setStatus("-1");
			return resp;	
		}

	}
	
	/**
	 * 订单列表(按状态,排除删除态和待支付态)
	 * @param page
	 * @param size
	 * @return
	 */
	public RepEntity getOrderList(HttpServletRequest request,int page, int size,String status) {
		page = page - 1;
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Pageable pageable = new PageRequest(page, size);
			if(status.equals("all") || status.equals("ALL")){
				
				List<String> statusNot = new ArrayList();
				
				statusNot.add("100");
				statusNot.add("0");
				
				Page<Orders> orderList = orderRepository.findByMemberIdAndStatusNotIn(userId,statusNot,pageable);
				List<Orders> content = orderList.getContent();
				log.info("====content======"+content);
				for(Orders o :content){
					o.setOrderItems(orderItemRepository.findByOrderCode(o.getOrderCode()));
				}
				
				resp.setMsg("订单列表查询成功");
				resp.setStatus("0");
				resp.setData(orderList);
				
				return resp;
			}
			
			if(status.equals("0") || status.equals("100")){
				
				resp.setMsg("订单列表查询失败，状态不允许");
				resp.setStatus("-1");
				
				return resp;
			}
			

			Page<Orders> orderList = orderRepository.findByMemberIdAndStatus(userId,status,pageable);
			List<Orders> content = orderList.getContent();
			log.info("====content======"+content);
			for(Orders o :content){
				o.setOrderItems(orderItemRepository.findByOrderCode(o.getOrderCode()));
			}
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
	public RepEntity getWaitPayOrderList(HttpServletRequest request,String outTradeNos) {
		RepEntity resp = new RepEntity();

		try{
			//当前用户待支付列表
			String[] _outTradeNos = outTradeNos.split(",");

			List<List<Orders>> orderList = new LinkedList<List<Orders>>();
			
			for (String outTradeNo : _outTradeNos) {
				orderList.add(getWaitPayOrderByOutTradeNo(request,outTradeNo));
			}
			
//			List<List<Orders>> orderList = waitPayInfoList.stream().map(payInfoMap -> getWaitPayOrderByOutTradeNo(payInfoMap)).collect(Collectors.toList());
//			
			resp.setStatus("0");
			resp.setMsg("订单列表查询成功");
			resp.setData(orderList);

			
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("订单列表查询失败");
			return resp;
		}

		
	}
	
	public List<Orders> getWaitPayOrderByOutTradeNo(HttpServletRequest request,String outTradeNo){
		try{	
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
		    return orderRepository.findByMemberIdAndOutTradeNo(userId,outTradeNo);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 待支付订单详情
	 * @param page
	 * @param size
	 * @return
	 */
	public RepEntity getWaitPayOrderDetail(HttpServletRequest request,String outTradeNo) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			List<Orders> orders = orderRepository.findByMemberIdAndOutTradeNo(userId,outTradeNo);
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
	public RepEntity getOrderByOrderCode(HttpServletRequest request,String orderCode){
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Orders order = orderRepository.findByMemberIdAndOrderCode(userId,orderCode);
			
			if(order == null || order.getStatus().equals("100")){
				resp.setStatus("-1");
				resp.setMsg("该订单不存在或已被删除");
				
				return resp;
			}
			
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
	public RepEntity editOrderStatus(HttpServletRequest request,String orderCode, String status) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Orders order = orderRepository.findByMemberIdAndOrderCode(userId,orderCode);
			
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

	
	/**
	 * 根据订单号删除订单
	 * @param orderCode
	 * @return
	 */
	public Object deleteOrder(HttpServletRequest request,String orderCode) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Orders order = orderRepository.findByMemberIdAndOrderCode(userId,orderCode);
			
			if(order == null || order.getStatus().equals("100")){
				resp.setStatus("-1");
				resp.setMsg("该订单不存在或已被删除");
				
				return resp;
			}
			
			order.setStatus("100");
			orderRepository.save(order);
			
			resp.setStatus("0");
			resp.setMsg("订单删除成功");
			resp.setData(order);
			
			return resp;
						
		}catch(Exception e){
			e.printStackTrace();
			resp.setStatus("-1");
			resp.setMsg("订单删除失败");
			return resp;
		}
	}

	/**
	 * 修改订单支付状态
	 * @param out_trade_no
	 * @param status
	 * @return
	 */
	public Object editOrderPayStatus(HttpServletRequest request,String out_trade_no, String status,String orderstatus) {
		Map<String,String[]> user = getAuthenticatedUser();
		String userId = user.get("Session_id")[0];
		List<Orders> orders = orderRepository.findByMemberIdAndOutTradeNo(userId,out_trade_no);
		for (Orders order : orders) {
			//若支付失败，订单状态为待支付
			order.setPayStatus(status);
			order.setPayStatus(orderstatus);
		}

		//[{"sku":"120161121135401001-0000","amount":"600"}]
//		HttpHeaders headers = new HttpHeaders();
//		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//		headers.setContentType(type);				
//		HttpEntity<List<String>> formEntity = new HttpEntity<List<String>>(reqAry, headers);
		//restTemplate.postForObject("http://inventory-service/nologin/minusInventory",formEntity, Inventory[].class)
		

		
		return orderRepository.save(orders);
	}

	public RepEntity getRefundListByStatus(HttpServletRequest request,int page, int size, String type, String status) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Pageable pageable = new PageRequest(page, size);
			Page<RefundOrders> refundOrderList = refundOrderRepository.findByMemberIdAndTypeAndStatus(userId,status,type,pageable);
			resp.setData(refundOrderList);
			resp.setMsg("退单列表获取成功");
			resp.setStatus("0");
			return resp;
			
		}catch(Exception e){
			e.printStackTrace();
			resp.setMsg("退单列表获取失败");
			resp.setStatus("-1");
			return resp;
		}
	}

	public RepEntity setIsRemind(HttpServletRequest request,String orderCode) {
		RepEntity resp = new RepEntity();
		try{
			Map<String,String[]> user = getAuthenticatedUser();
			String userId = user.get("Session_id")[0];
			Orders order = orderRepository.findByMemberIdAndOrderCode(userId, orderCode);
			order.setIsRemind("1");
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

	public String getSellerName(String sellerId) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		
		JSONObject json = new JSONObject();
		json.put("id",sellerId);
		
        log.info("getSellerName method call order method param is "+json);
        
	    HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
		Store store = restTemplate.postForObject("http://store-service/nologin/findStoreById",formEntity, Store.class);		
		log.info("getSellerName is "+store);
		return store.getStoreName();
	}

}
