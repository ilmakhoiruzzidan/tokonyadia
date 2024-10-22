package com.enigma.tokonyadia_api.audit;

import com.enigma.tokonyadia_api.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AuditableTest {
    @InjectMocks
    private Product product;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateCreatedDateAndLastModifiedDate() {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedDate(now);
        product.setLastModifiedDate(now);

        assertEquals(now, product.getCreatedDate());
        assertEquals(now, product.getLastModifiedDate());
    }

    @Test
    public void shouldReturnCreatedByAndLastModifiedBy() {
        String user = "admin";

        product.setCreatedBy(user);
        product.setLastModifiedBy(user);

        assertEquals(user, product.getCreatedBy());
        assertEquals(user, product.getLastModifiedBy());
    }
}