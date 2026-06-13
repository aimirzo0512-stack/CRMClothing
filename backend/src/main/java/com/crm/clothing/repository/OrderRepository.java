package com.crm.clothing.repository;

import com.crm.clothing.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByOrderByOrderDateDesc(Pageable p);
    List<Order> findTop5ByOrderByOrderDateDesc();

    @Query("select coalesce(sum(o.totalAmount),0) from Order o where o.status <> 'CANCELLED'")
    java.math.BigDecimal totalRevenue();

    @Query("select function('to_char', o.orderDate, 'YYYY-MM-DD') as d, coalesce(sum(o.totalAmount),0) as t " +
           "from Order o where o.status <> 'CANCELLED' and o.orderDate >= :from " +
           "group by function('to_char', o.orderDate, 'YYYY-MM-DD') order by d")
    List<Object[]> dailySalesSince(LocalDateTime from);

    @Query("select function('to_char', o.orderDate, 'YYYY-MM') as m, coalesce(sum(o.totalAmount),0) as t " +
           "from Order o where o.status <> 'CANCELLED' " +
           "group by function('to_char', o.orderDate, 'YYYY-MM') order by m")
    List<Object[]> monthlySales();

    @Query("select i.product.id, i.product.name, sum(i.quantity) as qty " +
           "from OrderItem i where i.order.status <> 'CANCELLED' " +
           "group by i.product.id, i.product.name order by qty desc")
    List<Object[]> topProducts(Pageable pageable);
}
