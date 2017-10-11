package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dorian.vendingmachine.domain.Item.newItem;
import static com.dorian.vendingmachine.domain.Money.newMoney;

public class VendingDataService {

    private static Map<String, Item> vendableProductsMap;
    private static Map<String, Money> vendableMoneiesMap;

    public static final String DOLLAR = "DOLLAR";
    public static final String DIME = "DIME";
    public static final String QUARTER = "QUARTER";
    public static final String NICKEL = "NICKEL";


    /**
     * In a production application, this would be loaded from a database or config file.
     */
    static {
        addVendingItems((Lists.newArrayList(newItem("A", new BigDecimal("0.65")),
                newItem("B", new BigDecimal("1")),
                newItem("C", new BigDecimal("1.50")))));

        initialiseVendingMonies(Sets.newHashSet(
                newMoney(DOLLAR), newMoney(DIME), newMoney(QUARTER), newMoney(NICKEL)));

    }

    public static Map<String, Item> getVendableProductsMap() {
        return vendableProductsMap;
    }

    public static Map<String, Money> getVendableMoneiesMap() {
        return vendableMoneiesMap;
    }

    public static void addVendingItems(List<Item> items) {
        vendableProductsMap = items.stream().collect(Collectors.toMap(Item::getId, Function.identity()));
    }

    public static void initialiseVendingMonies(Set<Money> monies) {
        vendableMoneiesMap = monies.stream().collect(Collectors.toMap(Money::getId, Function.identity()));
    }

}
