package com.crm.clothing.service;

import com.crm.clothing.dto.OrderDtos.*;
import com.crm.clothing.entity.*;
import com.crm.clothing.exception.NotFoundException;
import com.crm.clothing.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final CustomerRepository customers;
    private final ProductRepository products;

    public OrderService(OrderRepository o, CustomerRepository c, ProductRepository p) {
        this.orders = o; this.customers = c; this.products = p;
    }

    @Transactional
    public Order create(CreateOrderRequest req) {
        Customer customer = customers.findById(req.customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        Order order = new Order();
        order.setCustomer(customer);
        if (req.status != null) order.setStatus(req.status);
        BigDecimal total = BigDecimal.ZERO;
        if (req.items != null) {
            for (ItemDto i : req.items) {
                if (i.quantity == null || i.quantity <= 0) continue;
                Product p = products.findById(i.productId)
                        .orElseThrow(() -> new NotFoundException("Product not found: " + i.productId));
                if (p.getStockQuantity() < i.quantity)
                    throw new IllegalArgumentException("Not enough stock for " + p.getName());
                p.setStockQuantity(p.getStockQuantity() - i.quantity);
                products.save(p);

                OrderItem oi = new OrderItem();
                oi.setOrder(order);
                oi.setProduct(p);
                oi.setQuantity(i.quantity);
                oi.setUnitPrice(p.getPrice());
                order.getItems().add(oi);
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(i.quantity)));
            }
        }
        order.setTotalAmount(total);
        // simple loyalty: 1 pt per currency unit (rounded)
        customer.setLoyaltyPoints((customer.getLoyaltyPoints() == null ? 0 : customer.getLoyaltyPoints())
                + total.intValue());
        customers.save(customer);
        return orders.save(order);
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order o = orders.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        // if cancelling, restock
        if (status == OrderStatus.CANCELLED && o.getStatus() != OrderStatus.CANCELLED) {
            for (OrderItem it : o.getItems()) {
                Product p = it.getProduct();
                p.setStockQuantity(p.getStockQuantity() + it.getQuantity());
                products.save(p);
            }
        }
        o.setStatus(status);
        return orders.save(o);
    }
}
