package com.meftaul.inventory.service;

import com.meftaul.inventory.config.KafkaProperties;
import com.meftaul.inventory.domain.InventoryEntity;
import com.meftaul.inventory.domain.OrderObj;
import com.meftaul.inventory.domain.OrderStatus;
import com.meftaul.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private KafkaTemplate<String, OrderObj> kafkaTemplate;

    @Value("${kafka.order-created-topic}")
    private String orderTopic;

    @KafkaListener(
            topics = "order-created",
            groupId = "inventory",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderObj message) {
        System.out.println("Consumed message: " + message);

        Optional<InventoryEntity> inventory = inventoryRepository.findByProductId(message.getProductId());

        if (inventory.isPresent()) {
            InventoryEntity inventoryEntity = inventory.get();
            if (inventoryEntity.getQuantity().compareTo(message.getQuantity()) >= 0) {
                inventoryEntity.setQuantity(inventoryEntity.getQuantity().subtract(message.getQuantity()));
                inventoryRepository.save(inventoryEntity);

                message.setStatus(OrderStatus.PROCESSED);
                message.setMessage("Order processed successfully");
                sendOrderProcessedMessage(message);
            } else {
                message.setStatus(OrderStatus.OUT_OF_STOCK);
                message.setMessage("Not enough quantity available");
                sendOrderProcessedMessage(message);
            }
            inventoryRepository.save(inventoryEntity);
        } else {
            message.setStatus(OrderStatus.FAILED);
            message.setMessage("Product with id " + message.getId() + " not found");
            sendOrderProcessedMessage(message);
        }

    }

    private void sendOrderProcessedMessage(OrderObj message) {
        kafkaTemplate.send(kafkaProperties.getOrderProcessedTopic(), message);
    }

}
