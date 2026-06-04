package com.guvaren.payment.base;

import lombok.Builder;

@Builder
public record Response<T>(int status, String message, T data) {}
