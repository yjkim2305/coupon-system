package com.example.couponcore.coupon.application.service;

import com.example.couponcore.coupon.application.service.dto.CouponIssueRequest;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.infrastructure.repository.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couponcore.common.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.couponcore.common.util.CouponRedisUtils.getIssueRequestQueueKey;
import static com.example.couponcore.coupon.domain.exception.ErrorCode.FAIL_COUPON_ISSUE_REQUEST;

@Service
@RequiredArgsConstructor
public class CouponRedisService {

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    public void issueRequest(long couponId, long userId) {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId, userId);
        try {
            String value = objectMapper.writeValueAsString(couponIssueRequest);
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));
            redisRepository.rPush(getIssueRequestQueueKey(), value);
        } catch (JsonProcessingException e) {
            throw new CouponIssueException(FAIL_COUPON_ISSUE_REQUEST, "input: %s".formatted(couponIssueRequest));
        }

    }

    public void issueRequestScript(long couponId, long userId, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, userId, totalIssueQuantity);
    }
}
