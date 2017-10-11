package com.dorian.vendingmachine.domain;

import com.dorian.vendingmachine.service.VendingDataService;

import java.math.BigDecimal;

public class Money extends Token {

    private BigDecimal value;

    private Money(String id, BigDecimal value) {
        super(id, true);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;

        Money money = (Money) o;

        return money.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                ", id='" + id + '\'' +
                '}';
    }

    private Money(String id) {
        super(id, false);
    }

    public BigDecimal getValue() {
        return value;
    }

    public static Money newMoney(String id) {
        switch(id){
            case VendingDataService.DOLLAR:
                return newMoney(id, new BigDecimal("1"));
            case VendingDataService.QUARTER:
                return newMoney(id, new BigDecimal("0.25"));
            case VendingDataService.DIME:
                return newMoney(id, new BigDecimal("0.1"));
            case VendingDataService.NICKEL:
                return newMoney(id, new BigDecimal("0.05"));
                default:
                    return new Money(id);
        }

    }

    public static Money newMoney(String id, BigDecimal price) {
        return new Money(id, price);
    }
}