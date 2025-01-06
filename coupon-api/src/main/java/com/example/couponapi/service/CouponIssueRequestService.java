package com.example.couponapi.service;

import com.example.couponcore.coupon.application.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponFacade couponFacade;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public void issueRequestV1(long couponId, long userId) {
        couponFacade.issue(couponId, userId);
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(couponId, userId));
    }
}
