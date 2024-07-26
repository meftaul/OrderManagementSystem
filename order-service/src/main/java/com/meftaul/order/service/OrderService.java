package com.meftaul.order.service;

import com.meftaul.order.config.KafkaProperties;
import com.meftaul.order.domain.OrderDto;
import com.meftaul.order.domain.OrderEntity;
import com.meftaul.order.domain.OrderStatus;
import com.meftaul.order.exception.NotFoundException;
import com.meftaul.order.exception.OrderFailedException;
import com.meftaul.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, OrderEntity> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaProperties kafkaProperties, KafkaTemplate<String, OrderEntity> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderEntity getOrderById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }

    public Long createOrder(OrderDto dto) {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setProductId(dto.getProductId());
        orderEntity.setQuantity(dto.getQuantity());
        orderEntity.setStatus(OrderStatus.CREATED);
        orderRepository.save(orderEntity);

        sendToKafka(orderEntity);

        return orderEntity.getId();
    }

    public OrderEntity updateOrder(Long id, OrderDto dto) {

        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("Id in path and body must be same");
        }

        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        if (orderEntity.getStatus().equals(OrderStatus.PROCESSED)) {
            throw new IllegalArgumentException("Order is already processed");
        }

        orderEntity.setProductId(dto.getProductId());
        orderEntity.setQuantity(dto.getQuantity());
        orderRepository.save(orderEntity);

        sendToKafka(orderEntity);

        return orderEntity;
    }

    private void sendToKafka(OrderEntity orderEntity) {
        CompletableFuture<SendResult<String, OrderEntity>> future = kafkaTemplate
                .send(kafkaProperties.getOrderCreatedTopic(), orderEntity);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                orderEntity.setStatus(OrderStatus.FAILED);
                orderEntity.setMessage("Failed to place order. Try again order id: " + orderEntity.getId());
                orderRepository.save(orderEntity);

                throw new OrderFailedException("Order failed to send to Kafka");
            }
        });
    }

}
