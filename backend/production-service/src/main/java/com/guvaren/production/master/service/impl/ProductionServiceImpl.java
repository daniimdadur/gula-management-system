package com.guvaren.production.master.service.impl;

import com.guvaren.production.master.dto.ProductionReq;
import com.guvaren.production.master.dto.ProductionRes;
import com.guvaren.production.master.entity.ProductionEntity;
import com.guvaren.production.master.repository.ProductionRepository;
import com.guvaren.production.master.service.ProductionService;
import com.guvaren.production.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {
    private final ProductionRepository productionRepository;

    @Override
    public Optional<ProductionRes> create(ProductionReq request) {
        ProductionEntity production = ProductionEntity.builder()
                .id(CommonUtil.getUUID())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .productionDate(request.getProductionDate())
                .notes(request.getNotes())
                .build();

        productionRepository.save(production);
        return Optional.of(toResponse(production));
    }

    private ProductionRes toResponse(ProductionEntity entity) {
        return ProductionRes.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .productionDate(entity.getProductionDate())
                .notes(entity.getNotes())
                .build();
    }
}

