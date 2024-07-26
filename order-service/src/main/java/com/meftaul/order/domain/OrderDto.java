package com.meftaul.order.domain;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class OrderDto {

    private Long id;

    @Min(value = 1, message = "Product id must be greater than 0")
    private Long productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
