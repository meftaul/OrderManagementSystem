package com.meftaul.inventory.domain;

import java.math.BigDecimal;

public class OrderObj {

    private Long id;
    private Long productId;
    private BigDecimal quantity;
    private OrderStatus status;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderObj{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
