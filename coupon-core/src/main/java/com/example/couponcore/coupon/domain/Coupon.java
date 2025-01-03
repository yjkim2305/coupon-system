package com.example.couponcore.coupon.domain;

import com.example.couponcore.common.domain.enums.CouponType;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
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
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. total : %s, issued: %s".formatted(totalQuantity, issuedQuantity));
        }
        if (!availableIssueDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. request: %s, issueStart: %s, issueEnd: %s".formatted(LocalDateTime.now(), issueStartDate, issueEndDate));
        }

        issuedQuantity++;
    }
}
