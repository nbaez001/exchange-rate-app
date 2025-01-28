package com.empresa.proyecto.currencyconversion.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CurrencyConversion implements Serializable {
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal calculatedAmount;
    private BigDecimal exchangeRate;


    public CurrencyConversion() {
    }

    public CurrencyConversion(String from, String to, BigDecimal amount, BigDecimal calculatedAmount, BigDecimal exchangeRate) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.calculatedAmount = calculatedAmount;
        this.exchangeRate = exchangeRate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCalculatedAmount() {
        return calculatedAmount;
    }

    public void setCalculatedAmount(BigDecimal calculatedAmount) {
        this.calculatedAmount = calculatedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
