package com.techroots.qmongo;

import com.mongodb.client.AggregateIterable;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class InvoiceServiceTest {

    @Inject
    InvoiceService invoiceService;

    @Test
    void test() {
        invoiceService.add(new Invoice("Mango", BigDecimal.valueOf(1000), LocalDate.of(2022, 10, 10)));
        invoiceService.add(new Invoice("Orange", BigDecimal.valueOf(1000), LocalDate.of(2022,1, 1)));

        Map map = invoiceService.totalRevenuePerYear();
        System.out.println(map);
    }
}
