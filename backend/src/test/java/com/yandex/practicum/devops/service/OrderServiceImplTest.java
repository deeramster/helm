package com.yandex.practicum.devops.service;

import com.yandex.practicum.devops.model.Order;
import com.yandex.practicum.devops.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    private OrderRepository orderRepository = mock(OrderRepository.class);
    OrderService orderService;
    List<Order> orders = List.of(new Order());

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository);

        when(orderRepository.findAll()).thenReturn(orders);
    }

    @Test
    void getAllOrders() {
        assertEquals(orders, orderService.getAllOrders());
    }

    @Test
    void create() {
        orderService.create(new Order());
        verify(orderRepository).save(any());
    }

    @Test
    void update() {
        orderService.create(new Order());
        verify(orderRepository).save(any());
    }
}