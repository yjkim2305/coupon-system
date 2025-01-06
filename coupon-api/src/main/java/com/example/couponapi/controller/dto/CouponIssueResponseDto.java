package com.example.couponapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@JsonInclude(value = Include.NON_NULL)
public record CouponIssueResponseDto(boolean isSuccess, String comment) {
}
