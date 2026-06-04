package com.guvaren.inventory.base;

import lombok.Builder;

@Builder
public record Response<T>(int status, String message, T data) {}
