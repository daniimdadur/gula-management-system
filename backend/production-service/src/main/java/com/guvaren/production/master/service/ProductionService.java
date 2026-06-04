package com.guvaren.production.master.service;

import com.guvaren.production.master.dto.ProductionReq;
import com.guvaren.production.master.dto.ProductionRes;
import java.util.Optional;

public interface ProductionService {
    Optional<ProductionRes> create(ProductionReq request);
}

