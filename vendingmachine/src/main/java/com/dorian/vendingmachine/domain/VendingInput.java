package com.dorian.vendingmachine.domain;

import java.util.List;

@lombok.Getter
public final class VendingInput {
    private final List<Item> items;
    private final List<Item> invalidItems;
    private final List<Money> monies;
    private final List<Money> inValidMonies;
    private final Instructions instruction;

    public VendingInput(List<Item> items, List<Item> invalidItems, List<Money> monies, List<Money> inValidMonies,
                        Instructions instruction) {
        this.items = items;
        this.invalidItems = invalidItems;
        this.monies = monies;
        this.inValidMonies = inValidMonies;
        this.instruction = instruction;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getUnVendedItems() {
        return invalidItems;
    }

    public List<Money> getMonies() {
        return monies;
    }

    public List<Money> getUnVendedMonies() {
        return inValidMonies;
    }

    public Instructions getInstruction() {
        return instruction;
    }

    public List<Money> getAllInsertedMoney() {
        List<Money> moneyToReturn = getMonies();
        moneyToReturn.addAll(getInValidMonies());
        return moneyToReturn;
    }


}
