package com.example.couponcore.coupon.infrastructure.enums;

import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;

public enum CouponIssueRequestCode {
    SUCCESS(1),
    DUPLICATED_COUPON_ISSUE(2),
    INVALID_COUPON_ISSUE_QUANTITY(3);

    CouponIssueRequestCode(int code) {

    }

    public static CouponIssueRequestCode find(String code) {
        int codeValue = Integer.parseInt(code);
        if (codeValue == 1) {
            return SUCCESS;
        }

        if (codeValue == 2) {
            return DUPLICATED_COUPON_ISSUE;
        }

        if (codeValue == 3) {
            return INVALID_COUPON_ISSUE_QUANTITY;
        }

        throw new IllegalArgumentException("존재하지 않는 코드입니다.");
    }

    public static void checkRequestResult(CouponIssueRequestCode code) {
        if (code == INVALID_COUPON_ISSUE_QUANTITY) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다.");
        }

        if (code == DUPLICATED_COUPON_ISSUE) {
            throw new CouponIssueException(ErrorCode.DUPLICATE_COUPON_ISSUE, "이미 발급 요청된 쿠폰입니다.");
        }
    }
}
