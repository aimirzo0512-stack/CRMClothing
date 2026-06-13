package com.crm.clothing.controller;

import com.crm.clothing.entity.Order;
import com.crm.clothing.repository.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final CustomerRepository customers;
    private final ProductRepository products;
    private final OrderRepository orders;

    public DashboardController(CustomerRepository c, ProductRepository p, OrderRepository o) {
        this.customers = c; this.products = p; this.orders = o;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        Map<String, Object> m = new HashMap<>();
        m.put("totalCustomers", customers.count());
        m.put("totalProducts", products.count());
        m.put("totalOrders", orders.count());
        m.put("totalRevenue", orders.totalRevenue());
        List<Order> recent = orders.findTop5ByOrderByOrderDateDesc();
        m.put("recentOrders", recent);
        // sales chart: last 14 days
        List<Object[]> daily = orders.dailySalesSince(LocalDateTime.now().minusDays(14));
        List<Map<String, Object>> sales = new ArrayList<>();
        for (Object[] row : daily) {
            Map<String, Object> r = new HashMap<>();
            r.put("date", row[0]);
            r.put("total", row[1]);
            sales.add(r);
        }
        m.put("salesChart", sales);
        return m;
    }
}
