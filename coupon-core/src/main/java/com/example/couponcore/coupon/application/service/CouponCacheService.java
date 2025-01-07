package com.example.couponcore.coupon.application.service;

import com.example.couponcore.coupon.application.service.dto.CouponRedisEntity;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.infrastructure.entity.CouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponCacheService {

    private final CouponService couponService;

    @Cacheable(cacheNames = "coupon")
    public CouponRedisEntity getCouponCache(long couponId) {
        Coupon coupon = couponService.getById(couponId);
        return new CouponRedisEntity(CouponEntity.toEntity(coupon));
    }
}
