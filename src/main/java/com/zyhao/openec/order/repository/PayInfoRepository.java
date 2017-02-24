package com.zyhao.openec.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.zyhao.openec.order.entity.PayInfo;

@Repository
public interface PayInfoRepository extends JpaRepository<PayInfo, String> {
	
	public PayInfo findByOutTradeNo(String outTradeNo);
}
