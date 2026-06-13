-- Sample data (run AFTER the app started at least once so Hibernate
-- created tables and seeded the admin/employee accounts).

INSERT INTO customers (full_name, email, phone, address, loyalty_points) VALUES
('Alice Johnson', 'alice@example.com', '555-0101', '12 Main St',   120),
('Bob Smith',     'bob@example.com',   '555-0102', '34 Oak Ave',    45),
('Carla Diaz',    'carla@example.com', '555-0103', '88 Pine Rd',   300),
('David Lee',     'david@example.com', '555-0104', '5 Maple Blvd',   0)
ON CONFLICT (email) DO NOTHING;

INSERT INTO products (name, category, size, color, price, stock_quantity, description) VALUES
('Classic T-Shirt',  'T-Shirts', 'M',  'Black', 19.99, 50, '100% cotton classic tee'),
('Slim Fit Jeans',   'Jeans',    '32', 'Blue',  49.99, 30, 'Slim fit blue denim'),
('Hoodie Pullover',  'Hoodies',  'L',  'Grey',  39.99, 20, 'Warm fleece hoodie'),
('Summer Dress',     'Dresses',  'S',  'Red',   59.99, 15, 'Light floral dress'),
('Leather Belt',     'Accessories','M','Brown', 24.99, 4,  'Genuine leather belt'),
('Running Sneakers', 'Shoes',    '42', 'White', 79.99, 12, 'Lightweight running shoes');
