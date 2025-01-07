package com.example.couponcore.coupon.application.facade;

import com.example.couponcore.coupon.application.service.CouponService;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.domain.event.CouponIssueCompleteEvent;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import com.example.couponcore.couponissue.application.service.CouponIssueService;
import com.example.couponcore.couponissue.domain.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CouponFacade {
    private final CouponService couponService;
    private final CouponIssueService couponIssueService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void issue(Long couponId, Long userId) {
        Coupon coupon = couponService.getById(couponId);
        coupon.issue();
        couponService.save(coupon);

        CouponIssue issue = couponIssueService.findFirstCouponIssue(userId, couponId);

        if (issue != null) {
            throw new CouponIssueException(ErrorCode.DUPLICATE_COUPON_ISSUE, "이미 발급된 쿠폰입니다. user_id: %s, coupon_id: %s".formatted(userId, couponId));
        }

        couponIssueService.save(CouponIssue.of(couponId, userId));

        if (coupon.isIssueComplete()) {
           applicationEventPublisher.publishEvent(new CouponIssueCompleteEvent(couponId));
        }
    }
}
