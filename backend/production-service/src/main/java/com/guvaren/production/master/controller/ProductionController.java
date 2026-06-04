package com.guvaren.production.master.controller;

import com.guvaren.production.base.BaseController;
import com.guvaren.production.base.Response;
import com.guvaren.production.master.dto.ProductionReq;
import com.guvaren.production.master.dto.ProductionRes;
import com.guvaren.production.master.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productions")
public class ProductionController extends BaseController<ProductionRes> {
    private final ProductionService productionService;

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody ProductionReq request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(HttpStatus.CREATED.getReasonPhrase())
                        .data(productionService.create(request).orElse(null))
                        .build());
    }
}

