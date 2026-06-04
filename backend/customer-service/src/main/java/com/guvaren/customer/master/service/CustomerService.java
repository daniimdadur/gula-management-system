package com.guvaren.customer.master.service;

import com.guvaren.customer.master.dto.CustomerReq;
import com.guvaren.customer.master.dto.CustomerRes;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerRes> getAll();
    Optional<CustomerRes> getById(String id);
    Optional<CustomerRes> save(CustomerReq request);
    Optional<CustomerRes> update(String id, CustomerReq request);
    Optional<CustomerRes> delete(String id);
}
