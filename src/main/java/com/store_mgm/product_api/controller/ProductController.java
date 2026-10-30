package com.store_mgm.product_api.controller;

import com.store_mgm.product_api.model.Product;
import com.store_mgm.product_api.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/product/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        return productRepository.findByNameIgnoreCase(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    
    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/less/{price}")
    public List<Product> getProductsByPriceLessThan(@PathVariable BigDecimal price) {
        return productRepository.findByPriceLessThan(price);
    }
    
    @GetMapping("/products/greater/{price}")
    public List<Product> getProductsByPriceGreaterThan(@PathVariable BigDecimal price) {
        return productRepository.findByPriceGreaterThan(price);
    }
    
    @GetMapping("/products/range")
    public List<Product> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    @GetMapping("/products/in-stock")
    public List<Product> getProductsInStock() {
        return productRepository.findProductsInStock();
    }

    @GetMapping("/products/limited")
    public List<Product> getProductsLimited() {
        return productRepository.findProductsLimited();
    }
    
    @GetMapping("/products/out-of-stock")
    public List<Product> getProductsOutStock() {
        return productRepository.findProductsOutOfStock();
    }

    @GetMapping("/products/inventory-value")
    public ResponseEntity<String> getTotalInventoryValue() {
        BigDecimal totalValue = productRepository.calculateTotalInventoryValue();
        if (totalValue == null) {
            totalValue = BigDecimal.ZERO;
        }
        
        return ResponseEntity.ok("Total Inventory Value: $" + totalValue);
    }
    
    @GetMapping("/products/sort/{type}")
    public List<Product> getProductsSorted(@PathVariable String type) {
        switch (type.toLowerCase()) {
            case "asc":
                return productRepository.findProductsSortedByPriceAsc();
            case "desc":
                return productRepository.findProductsSortedByPriceDesc();
            case "category":
                return productRepository.findProductsGroupedByCategory();
            default:
                throw new IllegalArgumentException("Invalid sort type. Use 'asc', 'desc', or 'category'.");
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = productRepository.save(product);        
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setQuantity(updatedProduct.getQuantity());
                    product.setCategory(updatedProduct.getCategory());
                    
                    productRepository.save(product);
                    return ResponseEntity.ok("Product updated successfully!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }
 
    @PatchMapping("/products/{id}")
    public ResponseEntity<String> patchProduct(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) Integer quantity) {
        
        return productRepository.findById(id)
                .map(product -> {
                    if (price != null) product.setPrice(price);
                    if (quantity != null) product.setQuantity(quantity);
                    
                    productRepository.save(product);
                    return ResponseEntity.ok("Product partially updated!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    }

    @DeleteMapping("/products/category/{category}")
    public ResponseEntity<String> deleteProductsByCategory(@PathVariable String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found in the specified category.");
        }
        productRepository.deleteAll(products);
        return ResponseEntity.ok("Products in category '" + category + "' deleted successfully!");
    }
}