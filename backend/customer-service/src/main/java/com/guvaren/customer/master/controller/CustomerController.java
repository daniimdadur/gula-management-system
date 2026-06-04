package com.guvaren.customer.master.controller;

import com.guvaren.customer.base.BaseController;
import com.guvaren.customer.base.Response;
import com.guvaren.customer.master.dto.CustomerReq;
import com.guvaren.customer.master.dto.CustomerRes;
import com.guvaren.customer.master.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController extends BaseController<CustomerRes> {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Response> get() {
        return super.getResponse(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable String id) {
        return super.getResponse(customerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CustomerReq request) {
        return super.getResponse(customerService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable String id, @RequestBody CustomerReq request) {
        return super.getResponse(customerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable String id) {
        return super.getResponse(customerService.delete(id));
    }
}
