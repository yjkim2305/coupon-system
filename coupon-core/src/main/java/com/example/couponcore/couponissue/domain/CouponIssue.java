package com.example.couponcore.couponissue.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssue {
    private Long id;
    private Long couponId;
    private Long userId;
    private LocalDateTime issuedDate;
    private LocalDateTime usedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
