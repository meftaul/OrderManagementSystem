package com.meftaul.inventory.controller;

import com.meftaul.inventory.domain.InventoryEntity;
import com.meftaul.inventory.exception.NotFoundException;
import com.meftaul.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public InventoryEntity getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping()
    public Long createOrUpdateInventory(@RequestBody InventoryEntity inventoryEntity) {
        return inventoryService.createOrUpdateInventory(inventoryEntity);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
