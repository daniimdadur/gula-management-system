package com.guvaren.product.master.service.impl;

import com.guvaren.product.exception.NotFoundException;
import com.guvaren.product.master.dto.ProductListRes;
import com.guvaren.product.master.dto.ProductReq;
import com.guvaren.product.master.dto.ProductRes;
import com.guvaren.product.master.entity.ProductEntity;
import com.guvaren.product.master.repository.ProductRepository;
import com.guvaren.product.master.service.ProductService;
import com.guvaren.product.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductListRes list(Integer page, Integer size, String q, String category) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<ProductEntity> products = productRepository.search(q != null ? q : "", category, pageable);

        List<ProductRes> data = products.getContent().stream().map(this::toResponse).toList();

        return ProductListRes.builder()
                .data(data)
                .page(pageNum)
                .size(pageSize)
                .total(products.getTotalElements())
                .build();
    }

    @Override
    public Optional<ProductRes> getById(String id) {
        return productRepository.findById(id).map(this::toResponse);
    }

    @Override
    public Optional<ProductRes> create(ProductReq request) {
        ProductEntity product = ProductEntity.builder()
                .id(CommonUtil.getUUID())
                .code(request.getCode())
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .weight(request.getWeight())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .status(request.getStatus())
                .build();

        productRepository.save(product);
        return Optional.of(toResponse(product));
    }

    @Override
    public Optional<ProductRes> update(String id, ProductReq request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setStatus(request.getStatus());

        productRepository.save(product);
        return Optional.of(toResponse(product));
    }

    @Override
    public Optional<ProductRes> delete(String id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
        return Optional.of(toResponse(product));
    }

    private ProductRes toResponse(ProductEntity entity) {
        return ProductRes.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .weight(entity.getWeight())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .status(entity.getStatus())
                .build();
    }
}



