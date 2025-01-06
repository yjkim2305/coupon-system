package com.example.couponcore.couponissue.infrastructure.repository;

import com.example.couponcore.couponissue.domain.CouponIssue;

public interface CustomCouponIssueRepository {
    CouponIssue findFirstCouponIssue(Long couponId, Long userId);
}
