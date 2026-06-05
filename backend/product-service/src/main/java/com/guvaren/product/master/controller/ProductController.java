package com.guvaren.product.master.controller;

import com.guvaren.product.base.BaseController;
import com.guvaren.product.base.Response;
import com.guvaren.product.master.dto.ProductListRes;
import com.guvaren.product.master.dto.ProductReq;
import com.guvaren.product.master.dto.ProductRes;
import com.guvaren.product.master.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController<ProductRes> {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Response> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category) {
        ProductListRes result = productService.list(page, size, q, category);
        return ResponseEntity.ok(Response.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(result)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable String id) {
        return super.getResponse(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody ProductReq request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(HttpStatus.CREATED.getReasonPhrase())
                        .data(productService.create(request).orElse(null))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable String id, @RequestBody ProductReq request) {
        return super.getResponse(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable String id) {
        return super.getResponse(productService.delete(id));
    }
}

