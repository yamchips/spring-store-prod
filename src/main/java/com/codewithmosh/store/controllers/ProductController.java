package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Category;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        List<Product> products;
        if (categoryId != null) {
            products =  productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
