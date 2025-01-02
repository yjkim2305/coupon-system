package com.example.couponcore.coupon.domain;

import com.example.couponcore.common.domain.enums.CouponType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon {
    private Long id;
    private String title;
    private CouponType couponType;
    private Integer totalQuantity;
    private int issuedQuantity;
    private int discountAmount;
    private int minAvailableAmount;
    private LocalDateTime issueStartDate;
    private LocalDateTime issueEndDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
