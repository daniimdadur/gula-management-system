package com.guvaren.inventory.master.controller;

import com.guvaren.inventory.base.BaseController;
import com.guvaren.inventory.base.Response;
import com.guvaren.inventory.master.dto.InventoryRes;
import com.guvaren.inventory.master.dto.InventoryTransactionReq;
import com.guvaren.inventory.master.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventories")
public class InventoryController extends BaseController<InventoryRes> {
    private final InventoryService inventoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<Response> getByProductId(@PathVariable String productId) {
        return super.getResponse(inventoryService.getByProductId(productId));
    }

    @PostMapping("/{inventoryId}/in")
    public ResponseEntity<Response> stockIn(@PathVariable String inventoryId, @RequestBody InventoryTransactionReq request) {
        return super.getResponse(inventoryService.stockIn(inventoryId, request));
    }

    @PostMapping("/{inventoryId}/out")
    public ResponseEntity<Response> stockOut(@PathVariable String inventoryId, @RequestBody InventoryTransactionReq request) {
        return super.getResponse(inventoryService.stockOut(inventoryId, request));
    }

    @PostMapping("/{inventoryId}/adjust")
    public ResponseEntity<Response> adjust(@PathVariable String inventoryId, @RequestBody InventoryTransactionReq request) {
        return super.getResponse(inventoryService.adjust(inventoryId, request));
    }

    @GetMapping("/{inventoryId}/transactions")
    public ResponseEntity<Response> getTransactionHistory(@PathVariable String inventoryId) {
        return super.getResponse(inventoryService.getTransactionHistory(inventoryId));
    }
}

