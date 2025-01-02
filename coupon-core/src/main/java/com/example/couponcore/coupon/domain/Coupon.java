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

    public boolean availableIssueQuantity() {
        if (totalQuantity == null) {
            return true;
        }
        return totalQuantity > issuedQuantity;
    }

    public boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return issueStartDate.isBefore(now) && issueEndDate.isAfter(now);
    }

    public void issue() {
        if (!availableIssueQuantity()) {
            throw new RuntimeException("수량 검증");
        }
        if (!availableIssueDate()) {
            throw new RuntimeException("기한 검증");
        }

        issuedQuantity++;
    }
}
