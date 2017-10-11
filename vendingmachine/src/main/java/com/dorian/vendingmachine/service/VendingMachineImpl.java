package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;
import com.dorian.vendingmachine.domain.VendingInput;
import com.dorian.vendingmachine.domain.VendingOutput;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dorian.vendingmachine.domain.Money.newMoney;

/**
 * Service for the vending logic.
 */
@lombok.Getter
@Component
public class VendingMachineImpl implements VendingMachine {

    private static final String RETURN_MESSAGE = " Returning inserted money";
    private static final String EMPTY_STRING = "";

    private final VendingParserImpl parser;
    private Map<Money, Long> changeReserve = new ConcurrentHashMap<>();
    private List<Money> insertedMoney;

    @Inject
    public VendingMachineImpl(VendingParserImpl parser) {
        this.parser = parser;
    }

    /**
     * In a production application, this would be loaded from a database or config file.
     */
    @PostConstruct
    private void init() {
        addChange(Lists.newArrayList(newMoney("DOLLAR", new BigDecimal("1")), newMoney("DOLLAR", new BigDecimal("1")),
                newMoney("DIME", new BigDecimal("0.1")), newMoney("DIME", new BigDecimal("0.1")),
                newMoney("QUARTER", new BigDecimal("0.25")), newMoney("QUARTER", new BigDecimal("0.25")),
                newMoney("NICKEL", new BigDecimal("0.05")), newMoney("NICKEL", new BigDecimal("0.05"))));
    }


    @Override
    /**
     * Operation performed by service person to set the available items.
     *
     * @param items
     */
    public void addItems(List<Item> items) {
        VendingDataService.addVendingItems(items);
    }

    @Override
    /**
     * Operation performed by service person to set the available change.
     *
     * @param monies
     */
    public void addChange(List<Money> monies) {
        Map<Money, Long> changeByCount = monies.stream().collect(Collectors.groupingBy(Function.identity(),
                Collectors.counting()));
        changeReserve.putAll(changeByCount);
    }

    /**
     * Computes the total amount of change that can be returned.
     *
     * @return
     */
    public BigDecimal calculateReserveValue() {
        return changeReserve.entrySet().stream().map(this::multiplyCountAndValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal multiplyCountAndValue(Map.Entry<Money, Long> valueByCount) {
        return valueByCount.getKey()
                .getValue().multiply(BigDecimal.valueOf(valueByCount.getValue()));
    }

    @Override
    public String vendItems(String userInput) {
        VendingInput vendingInput = parser.parseVendingInstruction(userInput);
        Preconditions.checkArgument(vendingInput != null, "the input to the vending machine" +
                " cannot be null");
        Preconditions.checkArgument(vendingInput.getInstruction() != null,
                "the instruction received by the vending machine cannot be null");

        switch ((vendingInput.getInstruction())) {
            case GET:
                VendingOutput output = null;
                //is enough money inserted? if not return coins
                BigDecimal insertedAmount = vendingInput.getMonies().stream().map(Money::getValue).
                        reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalItemsValue = vendingInput.getItems().stream().map(Item::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (insertedAmount.compareTo(totalItemsValue) < 0) {
                    output = returnCoins(vendingInput, "Funds inserted insufficient.");
                } else {
                    //can handle change? if not return coins
                    BigDecimal changeToReturn = insertedAmount.subtract(totalItemsValue);
                    List<Money> change = getChangeFromReserve(changeToReturn);
                    if (calculateReserveValue().compareTo(changeToReturn) < 0 || change == null) {
                        output = returnCoins(vendingInput, "Not enough change available.");
                    } else {
                        //return items and change
                        output = new VendingOutput(vendingInput.getItems(), getChangeFromReserve(changeToReturn),
                                "Please retrieve your items and change. ");
                    }
                }
                return output.toString();

            case RETURN_COINS:
                return returnCoins(vendingInput, EMPTY_STRING).toString();

        }
        return null;
    }

    private List<Money> getChangeFromReserve(BigDecimal changeToReturn) {
        long dollarCoinsToReturn = Long.valueOf(Integer.valueOf(changeToReturn.intValue()));
        int centsToReturn =
                changeToReturn.remainder(BigDecimal.ONE).movePointRight(changeToReturn.scale()).intValue();
        int quartersToReturn = (int) (centsToReturn / 25);
        centsToReturn %= 25;
        int dimesToReturn = (int) (centsToReturn / 10);
        centsToReturn %= 10;
        int nickelsToReturn = (int) (centsToReturn / 5);
        centsToReturn %= 5;
        if (changeReserve.get(newMoney(VendingDataService.DOLLAR)).intValue() < dollarCoinsToReturn ||
                changeReserve.get(newMoney(VendingDataService.QUARTER)).intValue() < quartersToReturn ||
                changeReserve.get(newMoney(VendingDataService.DIME)).intValue() < dimesToReturn ||
                changeReserve.get(newMoney(VendingDataService.NICKEL)).intValue() < nickelsToReturn) {
            return null;
        } else {
            List<Money> change = new ArrayList<>();
            for (int i = 0; i < dollarCoinsToReturn; i++) {
                change.add(newMoney(VendingDataService.DOLLAR));
                updateReserveCount(VendingDataService.DOLLAR);
            }
            for (int i = 0; i < quartersToReturn; i++) {
                change.add(newMoney(VendingDataService.QUARTER));
                updateReserveCount(VendingDataService.QUARTER);
            }
            for (int i = 0; i < dimesToReturn; i++) {
                change.add(newMoney(VendingDataService.DIME));
                updateReserveCount(VendingDataService.DIME);
            }
            for (int i = 0; i < nickelsToReturn; i++) {
                change.add(newMoney(VendingDataService.NICKEL));
                updateReserveCount(VendingDataService.NICKEL);
            }
            return change;
        }
    }

    private VendingOutput returnCoins(VendingInput vendingInput, String returnReason) {
        return new VendingOutput(null, ImmutableList.copyOf(vendingInput.getAllInsertedMoney()),
                returnReason + RETURN_MESSAGE);
    }

    private void updateReserveCount(String moneyId) {
        Long count = changeReserve.get(newMoney(moneyId));
        if (count != null) {
            changeReserve.put(newMoney(moneyId), count.longValue() - 1);
        }

    }
}
