package com.adpro.pembelian;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
    private BackendApplication backendApplication;


    @Test
    void contextLoads() {
        assertNotNull(backendApplication);
    }

    @SuppressWarnings("static-access")
    @Test
    void testSpringApplicationRun() {
         assertDoesNotThrow(() -> backendApplication.main(new String[] {}));

    }

}
