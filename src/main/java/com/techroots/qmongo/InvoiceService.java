package com.techroots.qmongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.Decimal128;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.Arrays.asList;

@ApplicationScoped
public class InvoiceService {

    @Inject
    MongoClient mongoClient;

    public List<Invoice> list(){
        List<Invoice> list = new ArrayList<>();
        MongoCursor<Document> cursor = getCollection().find().iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Invoice invoice = new Invoice();
                invoice.setName(document.getString("name"));
                invoice.setAmount(new BigDecimal(document.getString("amount")));
                invoice.setInvoiceDate(LocalDate.parse(document.getString("invoiceDate")));
                list.add(invoice);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public Map<LocalDate, BigDecimal> totalRevenuePerYear() {
        MongoCursor<Document> cursor = getCollection()
                .aggregate(asList(new Document("$group",
                        new Document("_id",
                                new Document("$dateTrunc",
                                                new Document("date", "$invoiceDate")
                                                        .append("unit", "year")))
                                .append("sumAmount",
                                        new Document("$sum", "$amount"))))).iterator();
        Map<LocalDate, BigDecimal> revenuePerYear = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                revenuePerYear.put(toLocalDate((Date) document.get("_id")), ((Decimal128) document.get("sumAmount")).bigDecimalValue());
            }
        } finally {
            cursor.close();
        }
        return revenuePerYear;
    }

    public LocalDate toLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public void add(Invoice invoice){
        Document document = new Document()
                .append("name", invoice.getName())
                .append("invoiceDate", invoice.getInvoiceDate())
                .append("amount", invoice.getAmount());
        getCollection().insertOne(document);
    }

    private MongoCollection getCollection(){
        return mongoClient.getDatabase("crm").getCollection("invoices");
    }
}
