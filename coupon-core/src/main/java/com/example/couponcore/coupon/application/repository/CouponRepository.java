package com.example.couponcore.coupon.application.repository;

import com.example.couponcore.coupon.domain.Coupon;

public interface CouponRepository {

    Coupon findById(Long id);
}