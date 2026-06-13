package com.crm.clothing.controller;

import com.crm.clothing.entity.Product;
import com.crm.clothing.exception.NotFoundException;
import com.crm.clothing.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repo;
    public ProductController(ProductRepository repo) { this.repo = repo; }

    @GetMapping
    public Page<Product> list(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "") String q) {
        Pageable p = PageRequest.of(page, size, Sort.by("id").descending());
        if (q.isBlank()) return repo.findAll(p);
        return repo.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(q, q, p);
    }

    @GetMapping("/all")
    public Iterable<Product> all() { return repo.findAll(Sort.by("name")); }

    @GetMapping("/low-stock")
    public List<Product> lowStock(@RequestParam(defaultValue = "5") Integer threshold) {
        return repo.findByStockQuantityLessThan(threshold);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody Product p) { p.setId(null); return repo.save(p); }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product p) {
        Product e = repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        e.setName(p.getName()); e.setCategory(p.getCategory()); e.setSize(p.getSize());
        e.setColor(p.getColor()); e.setPrice(p.getPrice());
        e.setStockQuantity(p.getStockQuantity()); e.setDescription(p.getDescription());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
