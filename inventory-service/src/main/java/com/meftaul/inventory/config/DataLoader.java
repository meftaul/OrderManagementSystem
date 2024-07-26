package com.meftaul.inventory.config;

import com.meftaul.inventory.domain.InventoryEntity;
import com.meftaul.inventory.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostConstruct
    public void loadData() {
        inventoryRepository.saveAll(List.of(
            new InventoryEntity(1L, new BigDecimal(100)),
            new InventoryEntity(2L, new BigDecimal(200)),
            new InventoryEntity(3L, new BigDecimal(400)),
            new InventoryEntity(4L, new BigDecimal(300)),
            new InventoryEntity(5L, new BigDecimal(500)),
            new InventoryEntity(6L, new BigDecimal(600))
        ));
    }
}
