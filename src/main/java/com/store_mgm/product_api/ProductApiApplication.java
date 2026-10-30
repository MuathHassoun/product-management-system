// src/main/java/com/store_mgm/product_api/ProductApiApplication.java
package com.store_mgm.product_api;

import com.store_mgm.product_api.model.Product;
import com.store_mgm.product_api.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiApplication.class, args);
	}

	@Bean
    CommandLineRunner runner(ProductRepository productRepository) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
				int choice;

				do {
				    System.out.println("\n===== PRODUCT MANAGEMENT SYSTEM =====");
				    System.out.println("1. Add Product");
				    System.out.println("2. Display All Products");
				    System.out.println("3. Search Product by ID");
				    System.out.println("4. Update Product");
				    System.out.println("5. Delete Product");
				    System.out.println("6. Search Products by Category");
				    System.out.println("7. Calculate Total Inventory Value");
				    System.out.println("0. Exit");
				    System.out.print("\nChoose an option: ");

				    choice = scanner.nextInt();
				    scanner.nextLine();

				    switch (choice) {
				        case 1:
				            System.out.print("Enter name: ");
				            String name = scanner.nextLine();
				            System.out.print("Enter price: ");
				            BigDecimal price = scanner.nextBigDecimal();
				            System.out.print("Enter quantity: ");
				            int quantity = scanner.nextInt();
				            scanner.nextLine();
				            System.out.print("Enter category: ");
				            String category = scanner.nextLine();

				            Product product = new Product();
				            product.setName(name);
				            product.setPrice(price);
				            product.setQuantity(quantity);
				            product.setCategory(category);
				            
				            productRepository.save(product);
				            System.out.println("Product added successfully!");
				            break;

				        case 2:
				            List<Product> products = productRepository.findAll();
				            products.forEach(p -> System.out.println(p.getId() + " - " + p.getName() + " ($" + p.getPrice() + ")"));
				            break;

				        case 3:
				            System.out.print("Enter Product ID: ");
				            Long id = scanner.nextLong();
				            productRepository.findById(id).ifPresentOrElse(
				                p -> System.out.println("Found: " + p.getName() + ", Price: " + p.getPrice() + ", Quantity: " + p.getQuantity() + ", Category: " + p.getCategory()),
				                () -> System.out.println("Product not found!")
				            );
				            break;

				        case 7:
				            BigDecimal total = productRepository.calculateTotalInventoryValue();
				            System.out.println("Total Inventory Value: $" + (total != null ? total : BigDecimal.ZERO));
				            break;

				        case 0:
				            System.out.println("Exiting... Goodbye!");
				            System.exit(0);
				            break;

				        default:
				            System.out.println("Invalid choice! Try again.");
				    }
				} while (choice != 0);
			}
        };
    }
}
