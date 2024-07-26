package com.meftaul.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String orderCreatedTopic;
    private String orderProcessedTopic;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getOrderCreatedTopic() {
        return orderCreatedTopic;
    }

    public void setOrderCreatedTopic(String orderCreatedTopic) {
        this.orderCreatedTopic = orderCreatedTopic;
    }

    public String getOrderProcessedTopic() {
        return orderProcessedTopic;
    }

    public void setOrderProcessedTopic(String orderProcessedTopic) {
        this.orderProcessedTopic = orderProcessedTopic;
    }
}
