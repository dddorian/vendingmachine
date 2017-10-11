package com.dorian.vendingmachine.domain;

import java.math.BigDecimal;

public class Item extends Token {
    private BigDecimal price;

    public Item(String id, BigDecimal price) {
        super(id, true);
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        return getId().equals(item.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + price +
                ", id='" + id + '\'' +
                '}';
    }

    public Item(String id) {
        super(id, false);
    }

    public static Item newItem(String id, BigDecimal price) {
        return new Item(id, price);
    }

    public static Item newItem(String id) {
        return new Item(id);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
