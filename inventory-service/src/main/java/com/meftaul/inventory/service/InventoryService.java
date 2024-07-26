package com.meftaul.inventory.service;

import com.meftaul.inventory.config.KafkaProperties;
import com.meftaul.inventory.domain.InventoryEntity;
import com.meftaul.inventory.exception.NotFoundException;
import com.meftaul.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaProperties kafkaProperties;

    public InventoryService(InventoryRepository inventoryRepository, KafkaProperties kafkaProperties) {
        this.inventoryRepository = inventoryRepository;
        this.kafkaProperties = kafkaProperties;
    }

    public Long createOrUpdateInventory(InventoryEntity dto) {

        InventoryEntity entity = inventoryRepository.findByProductId(dto.getProductId()).orElse(new InventoryEntity());

        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());

        InventoryEntity savedInventory = inventoryRepository.save(entity);
        return savedInventory.getId();
    }

    public InventoryEntity getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException("Inventory not found with product id: " + productId)
        );
    }

}
