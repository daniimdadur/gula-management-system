package com.guvaren.product.master.service;

import com.guvaren.product.master.dto.ProductListRes;
import com.guvaren.product.master.dto.ProductReq;
import com.guvaren.product.master.dto.ProductRes;
import java.util.Optional;

public interface ProductService {
    ProductListRes list(Integer page, Integer size, String q, String category);
    Optional<ProductRes> getById(String id);
    Optional<ProductRes> create(ProductReq request);
    Optional<ProductRes> update(String id, ProductReq request);
    Optional<ProductRes> delete(String id);
}

