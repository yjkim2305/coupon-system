package com.example.couponcore.couponissue.infrastructure.repository;

import com.example.couponcore.couponissue.infrastructure.entity.CouponIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssueEntity, Long> {
}
