package com.example.couponcore.coupon.infrastructure.entity;

import com.example.couponcore.common.domain.entity.BaseTimeEntity;
import com.example.couponcore.common.domain.enums.CouponType;
import com.example.couponcore.coupon.domain.Coupon;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "coupons")
public class CouponEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    @Column(nullable = false)
    private int issuedQuantity;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private int minAvailableAmount;

    @Column(nullable = false)
    private LocalDateTime issueStartDate;

    @Column(nullable = false)
    private LocalDateTime issueEndDate;


    public static CouponEntity toEntity(Coupon coupon) {
        return CouponEntity.builder()
                .id(coupon.getId())
                .title(coupon.getTitle())
                .couponType(coupon.getCouponType())
                .totalQuantity(coupon.getTotalQuantity())
                .issuedQuantity(coupon.getIssuedQuantity())
                .discountAmount(coupon.getDiscountAmount())
                .minAvailableAmount(coupon.getMinAvailableAmount())
                .issueStartDate(coupon.getIssueStartDate())
                .issueEndDate(coupon.getIssueEndDate())
                .build();
    }
}
