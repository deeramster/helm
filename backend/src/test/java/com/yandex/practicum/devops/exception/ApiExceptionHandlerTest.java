package com.yandex.practicum.devops.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionHandlerTest {

    ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    void setUp() {
        apiExceptionHandler = new ApiExceptionHandler();
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        // When
        ResponseEntity<ApiExceptionHandler.ErrorItem> handle =
                apiExceptionHandler.handle(new ResourceNotFoundException());

        // Then
        assertEquals(HttpStatus.NOT_FOUND, handle.getStatusCode());
    }

    @Test
    void shouldHandleConstraintViolationException() {
        // When
        ResponseEntity<ApiExceptionHandler.ErrorResponse> handle =
                apiExceptionHandler.handle(new ConstraintViolationException(
                        new HashSet(List.of(Mockito.mock(ConstraintViolation.class)))));

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, handle.getStatusCode());
    }
}