package com.yandex.practicum.devops.service;

import com.yandex.practicum.devops.model.OrderProduct;
import com.yandex.practicum.devops.repository.OrderProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderProductServiceImplTest {
    OrderProductServiceImpl orderProductService;
    OrderProductRepository orderProductRepository = mock(OrderProductRepository.class);

    @BeforeEach
    void setUp() {
        orderProductService = new OrderProductServiceImpl(orderProductRepository);
    }

    @Test
    void create() {
        orderProductService.create(new OrderProduct());

        verify(orderProductRepository).save(any());
    }
}