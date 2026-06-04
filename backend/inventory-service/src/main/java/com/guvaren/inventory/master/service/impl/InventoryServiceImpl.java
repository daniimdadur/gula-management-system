package com.guvaren.inventory.master.service.impl;

import com.guvaren.inventory.enums.TransactionType;
import com.guvaren.inventory.exception.NotFoundException;
import com.guvaren.inventory.master.dto.InventoryRes;
import com.guvaren.inventory.master.dto.InventoryTransactionReq;
import com.guvaren.inventory.master.dto.InventoryTransactionRes;
import com.guvaren.inventory.master.entity.InventoryEntity;
import com.guvaren.inventory.master.entity.InventoryTransactionEntity;
import com.guvaren.inventory.master.repository.InventoryRepository;
import com.guvaren.inventory.master.repository.InventoryTransactionRepository;
import com.guvaren.inventory.master.service.InventoryService;
import com.guvaren.inventory.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    @Override
    public Optional<InventoryRes> getByProductId(String productId) {
        return inventoryRepository.findByProductId(productId).map(this::toResponse);
    }

    @Override
    public Optional<InventoryRes> stockIn(String inventoryId, InventoryTransactionReq request) {
        InventoryEntity inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + inventoryId));

        inventory.setCurrentStock(inventory.getCurrentStock() + request.getQuantity());
        createTransaction(inventory, TransactionType.IN, request);
        inventoryRepository.save(inventory);
        return Optional.of(toResponse(inventory));
    }

    @Override
    public Optional<InventoryRes> stockOut(String inventoryId, InventoryTransactionReq request) {
        InventoryEntity inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + inventoryId));

        if (inventory.getCurrentStock() - request.getQuantity() < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setCurrentStock(inventory.getCurrentStock() - request.getQuantity());
        createTransaction(inventory, TransactionType.OUT, request);
        inventoryRepository.save(inventory);
        return Optional.of(toResponse(inventory));
    }

    @Override
    public Optional<InventoryRes> adjust(String inventoryId, InventoryTransactionReq request) {
        InventoryEntity inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found with id: " + inventoryId));

        int newStock = inventory.getCurrentStock() + request.getQuantity();
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setCurrentStock(newStock);
        createTransaction(inventory, TransactionType.ADJUSTMENT, request);
        inventoryRepository.save(inventory);
        return Optional.of(toResponse(inventory));
    }

    @Override
    public List<InventoryTransactionRes> getTransactionHistory(String inventoryId) {
        List<InventoryTransactionEntity> transactions = inventoryTransactionRepository.findByInventoryId(inventoryId);
        return transactions.stream().map(this::toTransactionResponse).toList();
    }

    private void createTransaction(InventoryEntity inventory, TransactionType type, InventoryTransactionReq request) {
        InventoryTransactionEntity transaction = InventoryTransactionEntity.builder()
                .id(CommonUtil.getUUID())
                .inventory(inventory)
                .transactionType(type)
                .quantity(request.getQuantity())
                .referenceType(request.getReferenceType())
                .referenceId(request.getReferenceId())
                .notes(request.getNotes())
                .build();
        inventoryTransactionRepository.save(transaction);
    }

    private InventoryRes toResponse(InventoryEntity entity) {
        return InventoryRes.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .currentStock(entity.getCurrentStock())
                .minimumStock(entity.getMinimumStock())
                .build();
    }

    private InventoryTransactionRes toTransactionResponse(InventoryTransactionEntity entity) {
        return InventoryTransactionRes.builder()
                .id(entity.getId())
                .inventoryId(entity.getInventory().getId())
                .transactionType(entity.getTransactionType())
                .quantity(entity.getQuantity())
                .referenceType(entity.getReferenceType())
                .referenceId(entity.getReferenceId())
                .notes(entity.getNotes())
                .build();
    }
}

