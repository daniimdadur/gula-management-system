package com.guvaren.customer.master.service.impl;

import com.guvaren.customer.exception.NotFoundException;
import com.guvaren.customer.master.dto.CustomerReq;
import com.guvaren.customer.master.dto.CustomerRes;
import com.guvaren.customer.master.entity.CustomerEntity;
import com.guvaren.customer.master.repository.CustomerRepository;
import com.guvaren.customer.master.service.CustomerService;
import com.guvaren.customer.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerRes> getAll() {
        List<CustomerEntity> customers = customerRepository.findAll();
        return customers.stream().map(this::toResponse).toList();
    }

    @Override
    public Optional<CustomerRes> getById(String id) {
        return customerRepository.findById(id).map(this::toResponse);
    }

    @Override
    public Optional<CustomerRes> save(CustomerReq request) {
        CustomerEntity result = toEntity(request);

        try {
            customerRepository.save(result);
            return Optional.of(toResponse(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CustomerRes> update(String id, CustomerReq request) {
        CustomerEntity result = toEntity(request, id);

        try {
            customerRepository.save(result);
            return Optional.of(toResponse(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CustomerRes> delete(String id) {
        CustomerEntity result = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        try {
            customerRepository.delete(result);
            return Optional.of(toResponse(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CustomerEntity toEntity(CustomerReq request) {
        return CustomerEntity.builder()
                .id(CommonUtil.getUUID())
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
    }

    private CustomerRes toResponse(CustomerEntity customerEntity) {
        return CustomerRes.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .address(customerEntity.getAddress())
                .build();
    }

    private CustomerEntity toEntity(CustomerReq request, String id) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        customerEntity.setName(request.getName());
        customerEntity.setEmail(request.getEmail());
        customerEntity.setAddress(request.getAddress());
        return customerEntity;
    }
}
