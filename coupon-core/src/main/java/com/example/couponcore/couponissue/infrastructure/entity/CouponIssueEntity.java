package com.example.couponcore.couponissue.infrastructure.entity;

import com.example.couponcore.common.domain.entity.BaseTimeEntity;
import com.example.couponcore.couponissue.domain.CouponIssue;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "coupon_issues")
public class CouponIssueEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime issuedDate;

    private LocalDateTime usedDate;

    public static CouponIssueEntity toEntity(CouponIssue couponIssue) {
        return CouponIssueEntity.builder()
                .id(couponIssue.getId())
                .couponId(couponIssue.getCouponId())
                .userId(couponIssue.getUserId())
                .issuedDate(couponIssue.getIssuedDate())
                .usedDate(couponIssue.getUsedDate())
                .build();
    }
}
