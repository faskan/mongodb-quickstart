package com.techroots.qmongo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Invoice {

    private String name;
    private BigDecimal amount;
    private String id;
    private LocalDate invoiceDate;

    public Invoice() {
    }

    public Invoice(String name, BigDecimal amount, LocalDate invoiceDate) {
        this.name = name;
        this.amount = amount;
        this.invoiceDate = invoiceDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Invoice)) {
            return false;
        }

        Invoice other = (Invoice) obj;

        return Objects.equals(other.name, this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
