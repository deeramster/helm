package com.yandex.practicum.devops.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductPKTest {
    @Test
    void testOrderProductPK() {
        Order order = new Order();
        Product product = new Product();
        OrderProductPK orderProductPK = new OrderProductPK();
        orderProductPK.setOrder(order);
        orderProductPK.setProduct(product);


        OrderProductPK orderProductPK2 = new OrderProductPK();
        orderProductPK2.setOrder(order);
        orderProductPK2.setProduct(product);

        assertEquals(orderProductPK.hashCode(), orderProductPK2.hashCode());
        assertEquals(orderProductPK, orderProductPK2);
        assertNotEquals(orderProductPK, new OrderProductPK());
    }
}