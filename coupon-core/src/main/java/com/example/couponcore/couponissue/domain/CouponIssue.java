package com.example.couponcore.couponissue.domain;

import com.example.couponcore.couponissue.infrastructure.entity.CouponIssueEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssue {
    private Long id;
    private Long couponId;
    private Long userId;
    private LocalDateTime issuedDate;
    private LocalDateTime usedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CouponIssue from(CouponIssueEntity couponIssueEntity) {
        return CouponIssue.builder()
                .id(couponIssueEntity.getId())
                .couponId(couponIssueEntity.getCouponId())
                .userId(couponIssueEntity.getUserId())
                .createdDate(couponIssueEntity.getCreatedDate())
                .updatedDate(couponIssueEntity.getUpdatedDate())
                .build();
    }

    public static CouponIssue of(Long couponId, Long userId) {
        return CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();
    }
}
