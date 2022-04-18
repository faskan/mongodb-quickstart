package com.techroots.qmongo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, BigDecimal> totalRevenuePerYear() {
        MongoCursor<Document> cursor = getCollection()
                .aggregate(asList(new Document("$group",
                        new Document("_id",
                                new Document("truncatedInvoiceDate",
                                        new Document("$dateTrunc",
                                                new Document("date", "$invoiceDate")
                                                        .append("unit", "year"))))
                                .append("sumAmount",
                                        new Document("$sum", "$amount"))))).iterator();
        return new HashMap<>();
    }

    public void add(Invoice invoice){
        Document document = new Document()
                .append("name", invoice.getName())
                .append("invoiceDate", invoice.getInvoiceDate().toString())
                .append("amount", invoice.getAmount().toString());
        getCollection().insertOne(document);
    }

    private MongoCollection getCollection(){
        return mongoClient.getDatabase("crm").getCollection("invoices");
    }
}
