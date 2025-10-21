package com.yandex.practicum.devops.service;

import com.yandex.practicum.devops.exception.ResourceNotFoundException;
import com.yandex.practicum.devops.model.Product;
import com.yandex.practicum.devops.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class ProductServiceImplTest {
    ProductRepository productRepository = mock(ProductRepository.class);
    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void getAllProducts() {
        // Given
        List<Product> products = List.of(new Product());
        when(productRepository.findAll())
                .thenReturn(products);
        // When & Then
        assertEquals(products, productService.getAllProducts());
    }

    @Test
    void getProductNotFound() {
        // Given
        List<Product> products = List.of(new Product());
        when(productRepository.findAll())
                .thenReturn(products);
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProduct(11);
        });
    }

    @Test
    void save() {
        productService.save(new Product());
        verify(productRepository).save(any());
    }
}