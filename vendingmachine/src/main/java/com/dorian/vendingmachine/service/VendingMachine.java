package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;

import java.util.List;

public interface VendingMachine {
    void addItems(List<Item> items);

    void addChange(List<Money> monies);

    String vendItems(String userInput);
}
