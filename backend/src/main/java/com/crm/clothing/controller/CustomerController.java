package com.crm.clothing.controller;

import com.crm.clothing.entity.Customer;
import com.crm.clothing.exception.NotFoundException;
import com.crm.clothing.repository.CustomerRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repo;
    public CustomerController(CustomerRepository repo) { this.repo = repo; }

    @GetMapping
    public Page<Customer> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "") String q) {
        Pageable p = PageRequest.of(page, size, Sort.by("id").descending());
        if (q.isBlank()) return repo.findAll(p);
        return repo.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, p);
    }

    @GetMapping("/all")
    public Iterable<Customer> all() { return repo.findAll(Sort.by("fullName")); }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public Customer create(@RequestBody Customer c) { c.setId(null); return repo.save(c); }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Customer update(@PathVariable Long id, @RequestBody Customer c) {
        Customer existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        existing.setFullName(c.getFullName());
        existing.setEmail(c.getEmail());
        existing.setPhone(c.getPhone());
        existing.setAddress(c.getAddress());
        existing.setLoyaltyPoints(c.getLoyaltyPoints() == null ? 0 : c.getLoyaltyPoints());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
