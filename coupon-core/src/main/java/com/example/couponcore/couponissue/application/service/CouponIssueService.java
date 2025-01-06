package com.example.couponcore.couponissue.application.service;

import com.example.couponcore.couponissue.application.repository.CouponIssueRepository;
import com.example.couponcore.couponissue.domain.CouponIssue;
import com.example.couponcore.couponissue.infrastructure.repository.CustomCouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;
    private final CustomCouponIssueRepository customCouponIssueRepository;

    public CouponIssue save(CouponIssue couponIssue) {
        return couponIssueRepository.save(couponIssue);
    }

    public CouponIssue findFirstCouponIssue(Long couponId, Long userId) {
        return customCouponIssueRepository.findFirstCouponIssue(couponId, userId);
    }
}
