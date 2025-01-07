package com.example.couponapi.service;

import com.example.couponcore.common.component.DistributeLockExecutor;
import com.example.couponcore.coupon.application.facade.AsyncCouponFacadeV1;
import com.example.couponcore.coupon.application.facade.AsyncCouponFacadeV2;
import com.example.couponcore.coupon.application.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponFacade couponFacade;
    private final DistributeLockExecutor distributeLockExecutor;
    private final AsyncCouponFacadeV1 asyncCouponFacadeV1;
    private final AsyncCouponFacadeV2 asyncCouponFacadeV2;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public void issueRequestV1(long couponId, long userId) {

        distributeLockExecutor.execute("lock_" + couponId,10000, 10000,  () -> {
            couponFacade.issue(couponId, userId);
        });

        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(couponId, userId));
    }

    public void asyncIssueRequestV1(long couponId, long userId) {
        asyncCouponFacadeV1.issue(couponId, userId);
    }

    public void asyncIssueRequestV2(long couponId, long userId) {
        asyncCouponFacadeV2.issue(couponId, userId);
    }


}
