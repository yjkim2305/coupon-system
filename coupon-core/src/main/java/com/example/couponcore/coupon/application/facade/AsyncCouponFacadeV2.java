package com.example.couponcore.coupon.application.facade;

import com.example.couponcore.common.component.DistributeLockExecutor;
import com.example.couponcore.coupon.application.service.CouponCacheService;
import com.example.couponcore.coupon.application.service.CouponIssueRedisService;
import com.example.couponcore.coupon.application.service.CouponRedisService;
import com.example.couponcore.coupon.application.service.CouponService;
import com.example.couponcore.coupon.application.service.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AsyncCouponFacadeV2 {

    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponService couponService;
    private final CouponRedisService couponRedisService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final CouponCacheService couponCacheService;

    public void issue(long couponId, long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();
        couponRedisService.issueRequestScript(couponId, userId, coupon.totalQuantity());

    }
}
