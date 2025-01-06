package com.example.couponcore.couponissue.infrastructure.repository;

import com.example.couponcore.couponissue.application.repository.CouponIssueRepository;
import com.example.couponcore.couponissue.domain.CouponIssue;
import com.example.couponcore.couponissue.infrastructure.entity.CouponIssueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {
    private final CouponIssueJpaRepository couponIssueJpaRepository;

    public CouponIssue save(CouponIssue couponIssue) {
        CouponIssueEntity couponIssueEntity = couponIssueJpaRepository.save(CouponIssueEntity.toEntity(couponIssue));
        return CouponIssue.from(couponIssueEntity);
    }
}
