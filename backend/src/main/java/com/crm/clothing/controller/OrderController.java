package com.crm.clothing.controller;

import com.crm.clothing.dto.OrderDtos.*;
import com.crm.clothing.entity.Order;
import com.crm.clothing.exception.NotFoundException;
import com.crm.clothing.repository.OrderRepository;
import com.crm.clothing.service.OrderService;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository repo;
    private final OrderService service;

    public OrderController(OrderRepository repo, OrderService service) {
        this.repo = repo; this.service = service;
    }

    @GetMapping
    public Page<Order> list(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        return repo.findAllByOrderByOrderDateDesc(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) { return service.create(req); }

    @PutMapping("/{id}/status")
//    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest req) {
        return service.updateStatus(id, req.status);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
