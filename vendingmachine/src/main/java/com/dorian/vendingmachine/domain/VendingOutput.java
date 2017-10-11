package com.dorian.vendingmachine.domain;

import java.util.List;

@lombok.Getter
public final class VendingOutput {
    private final String message;
    private final List<Item> items;
    private final List<Money> monies;

    public VendingOutput(List<Item> items, List<Money> monies, String message) {
        this.items = items;
        this.monies = monies;
        this.message = message;
    }

    @Override
    public String toString() {
        return "VendingOutput{" +
                "message='" + message + '\'' +
                ", items=" + items +
                ", monies=" + monies +
                '}';
    }
}
