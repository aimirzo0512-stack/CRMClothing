# ER Diagram

```
+-----------+        +------------+        +---------+
|   users   |*------*|   roles    |        |customers|
+-----------+ user_  +------------+        +---------+
| id (PK)   | roles  | id (PK)    |        | id (PK) |
| username  |        | name       |        | full_name
| password  |                              | email
| full_name |                              | phone
+-----------+                              | address
                                           | registration_date
                                           | loyalty_points
                                           +----+----+
                                                |
                                                | 1
                                                |
                                                | *
                                           +----+----+
                                           |  orders |
                                           +---------+
                                           | id (PK) |
                                           | customer_id (FK)
                                           | order_date
                                           | status
                                           | total_amount
                                           +----+----+
                                                | 1
                                                |
                                                | *
                            +----------+   +----+--------+
                            | products |1-*| order_items |
                            +----------+   +-------------+
                            | id (PK)  |   | id (PK)
                            | name     |   | order_id (FK)
                            | category |   | product_id (FK)
                            | size     |   | quantity
                            | color    |   | unit_price
                            | price    |   +-------------+
                            | stock_quantity
                            | description
                            +----------+
```

**Relationships**
- users *..* roles (via `user_roles`)
- customers 1..* orders
- orders 1..* order_items
- products 1..* order_items
