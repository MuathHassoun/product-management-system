# Product Management System

A full-stack web application for managing products and store inventory, built with **Spring Boot, REST APIs, React, Tailwind CSS, and PostgreSQL**. The application provides product CRUD operations, search and filtering, and inventory value calculations through a responsive web interface.

## 1. Technology Stack

- **Frontend:** React, Tailwind CSS, TypeScript
- **Backend:** Java, Spring Boot, Spring Web
- **API Architecture:** RESTful APIs
- **Database:** Cloud-hosted PostgreSQL (Neon)
- **Data Access:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **API Testing:** cURL
- **Version Control:** Git and GitHub

## 2. Database Design

The application uses a PostgreSQL database named `store_db` with a `products` table.

| Column | Type | Constraints |
|---|---|---|
| `id` | `BIGINT` | Primary key, auto-generated |
| `name` | `VARCHAR(100)` | Required |
| `price` | `NUMERIC(10,2)` | Required, non-negative |
| `quantity` | `INTEGER` | Required, non-negative |
| `category` | `VARCHAR(50)` | Required |

## 3. Features

### Product Management

1. **Add Product** — Create products with a name, price, quantity, and category.
2. **Display All Products** — Retrieve and display all products.
3. **Search by ID** — Find a specific product using its ID.
4. **Update Product** — Modify existing product information.
5. **Delete Product** — Remove a product after confirmation.
6. **Filter by Category** — Display products belonging to a specific category.
7. **Calculate Inventory Value** — Calculate the total value of available stock using `SUM(price * quantity)`.
