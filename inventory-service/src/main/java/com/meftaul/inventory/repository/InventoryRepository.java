package com.meftaul.inventory.repository;

import com.meftaul.inventory.domain.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByProductId(Long productId);
}
