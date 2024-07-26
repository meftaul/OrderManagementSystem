package com.meftaul.order.service;

import com.meftaul.order.domain.OrderEntity;
import com.meftaul.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(
            topics = "order-processed",
            groupId = "order",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderEntity message) {
        System.out.println("Consumed message: " + message);
        orderRepository.save(message);
    }

}
