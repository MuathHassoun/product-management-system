package com.store_mgm.product_api.repository;

import com.store_mgm.product_api.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameIgnoreCase(String name);
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByPriceLessThan(BigDecimal price);
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    BigDecimal calculateTotalInventoryValue();

    @Query("SELECT p FROM Product p WHERE p.quantity > 10")
    List<Product> findProductsInStock();

    @Query ("SELECT p FROM Product p WHERE p.quantity <= 10 AND p.quantity > 0")
    List<Product> findProductsLimited();

    @Query ("SELECT p FROM Product p WHERE p.quantity = 0")
    List<Product> findProductsOutOfStock();

    @Query("SELECT p FROM Product p GROUP BY p.category")
    List<Product> findProductsGroupedByCategory();

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findProductsSortedByPriceAsc();
    
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findProductsSortedByPriceDesc();
}