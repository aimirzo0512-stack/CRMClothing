package com.crm.clothing.dto;

import com.crm.clothing.entity.OrderStatus;
import java.util.List;

public class OrderDtos {
    public static class ItemDto {
        public Long productId;
        public Integer quantity;
    }
    public static class CreateOrderRequest {
        public Long customerId;
        public List<ItemDto> items;
        public OrderStatus status; // optional
    }
    public static class UpdateStatusRequest {
        public OrderStatus status;
    }
}
