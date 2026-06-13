package com.crm.clothing.controller;

import com.crm.clothing.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final OrderRepository orders;
    public ReportController(OrderRepository orders) { this.orders = orders; }

    @GetMapping("/daily-sales")
    public List<Map<String,Object>> daily(@RequestParam(defaultValue = "30") int days) {
        List<Object[]> rows = orders.dailySalesSince(LocalDateTime.now().minusDays(days));
        return toMap(rows, "date");
    }

    @GetMapping("/monthly-sales")
    public List<Map<String,Object>> monthly() {
        return toMap(orders.monthlySales(), "month");
    }

    @GetMapping("/top-products")
    public List<Map<String,Object>> top(@RequestParam(defaultValue = "5") int limit) {
        List<Object[]> rows = orders.topProducts(PageRequest.of(0, limit));
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> m = new HashMap<>();
            m.put("productId", r[0]);
            m.put("name", r[1]);
            m.put("quantity", r[2]);
            out.add(m);
        }
        return out;
    }

    private List<Map<String,Object>> toMap(List<Object[]> rows, String key) {
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String,Object> m = new HashMap<>();
            m.put(key, r[0]);
            m.put("total", r[1]);
            out.add(m);
        }
        return out;
    }
}
