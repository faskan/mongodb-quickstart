package com.techroots.qmongo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class InvoiceServiceTest {

    @Inject
    InvoiceService invoiceService;

    @Test
    void test() {
        invoiceService.add(new Invoice("Mango", BigDecimal.valueOf(1000), LocalDate.of(2022, 10, 10)));
        invoiceService.add(new Invoice("Orange", BigDecimal.valueOf(1000), LocalDate.of(2022,1, 1)));
        invoiceService.add(new Invoice("Pineapple", BigDecimal.valueOf(450), LocalDate.of(2021,1, 1)));

        Map map = invoiceService.totalRevenuePerYear();
        assertEquals(BigDecimal.valueOf(2000), map.get(LocalDate.of(2022, 1, 1)));
        assertEquals(BigDecimal.valueOf(450), map.get(LocalDate.of(2021, 1, 1)));
    }
}
