package com.example.couponcore.coupon.application.facade;

import com.example.couponcore.common.component.DistributeLockExecutor;
import com.example.couponcore.coupon.application.service.CouponIssueRedisService;
import com.example.couponcore.coupon.application.service.CouponRedisService;
import com.example.couponcore.coupon.application.service.CouponService;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncCouponFacadeV1 {

    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponService couponService;
    private final CouponRedisService couponRedisService;
    private final DistributeLockExecutor distributeLockExecutor;

    public void issue(long couponId, long userId) {
        Coupon coupon = couponService.getById(couponId);
        if (!coupon.availableIssueDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId: %s, issueStart:%s, issueEnd:%s".formatted(couponId, coupon.getIssueStartDate(), coupon.getIssueEndDate()));
        }

        distributeLockExecutor.execute("lock_%s".formatted(couponId), 3000, 3000, () -> {
            if (!couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(), couponId)) {
                throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. couponId: %s, userId: %s".formatted(couponId, userId));
            }

            if (!couponIssueRedisService.availableUserIssueQuantity(couponId, userId)) {
                throw new CouponIssueException(ErrorCode.DUPLICATE_COUPON_ISSUE, "이미 발급 요청이 처리됐습니다. couponId: %s, userId: %s".formatted(couponId, userId));
            }

            couponRedisService.issueRequest(couponId, userId);
        });

    }
}
