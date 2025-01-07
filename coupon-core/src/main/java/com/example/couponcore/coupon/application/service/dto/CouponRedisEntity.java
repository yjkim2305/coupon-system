package com.example.couponcore.coupon.application.service.dto;

import com.example.couponcore.common.domain.enums.CouponType;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import com.example.couponcore.coupon.infrastructure.entity.CouponEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record CouponRedisEntity(
        Long id,
        CouponType couponType,
        Integer totalQuantity,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime issueStartDate,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime issueEndDate
) {
    public CouponRedisEntity(CouponEntity couponEntity){
        this(
                couponEntity.getId(),
                couponEntity.getCouponType(),
                couponEntity.getTotalQuantity(),
                couponEntity.getIssueStartDate(),
                couponEntity.getIssueEndDate()
        );
    }

    private boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return issueStartDate.isBefore(now) && issueEndDate.isAfter(now);
    }

    public void checkIssuableCoupon() {
        if (!availableIssueDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다. couponId: %s, issueStart:%s, issueEnd:%s".formatted(id, issueStartDate, issueEndDate));
        }
    }

}
