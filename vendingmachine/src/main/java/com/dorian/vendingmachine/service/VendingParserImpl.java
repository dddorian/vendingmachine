package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;
import com.dorian.vendingmachine.domain.VendingInput;
import com.dorian.vendingmachine.domain.VendingOutput;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dorian.vendingmachine.domain.Instructions.GET;
import static com.dorian.vendingmachine.domain.Instructions.RETURN_COINS;
import static com.dorian.vendingmachine.domain.Item.newItem;
import static com.dorian.vendingmachine.domain.Money.newMoney;

/**
 * Implementation of VendingParser.
 */
@Component
public class VendingParserImpl implements VendingParser {

    @Override
    /**
     * Translates user's instructions into commands that the vending machine can process.
     *
     * @param vendingInstruction
     * @return
     */
    public VendingInput parseVendingInstruction(String vendingInstruction) {
        Preconditions.checkArgument(!vendingInstruction.isEmpty(), "The user input cannot be empty");
        Preconditions.checkArgument(!(vendingInstruction == null), "The user input cannot be null");


        String[] tokens = vendingInstruction.trim().split("\\s*,\\s*");

        Map<Boolean, List<Money>> moniesByIsVended = Arrays.stream(tokens).filter(this::isMoney)
                .map(this::getVendedOrUnVendedMoney).collect(Collectors.partitioningBy(Money::isVended));

        Map<Boolean, List<Item>> itemsByIsVended = Arrays.stream(tokens).filter(this::isItem)
                .map(this::getVendedOrUnVendedItem).collect(Collectors.partitioningBy(Item::isVended));

        // here could just return the first item Optional-findfirst item. really depends on reqs.
        // Optional<Item> item = Arrays.stream(tokens).filter(this::isItem).findFirst()
        //		 .map(this::getVendedOrUnVendedItem);


        if (isCoinReturnInstruction(tokens) || itemsByIsVended.get(true).isEmpty()) {
            return new VendingInput(itemsByIsVended.get(true), itemsByIsVended.get(false), moniesByIsVended.get(true),
                    moniesByIsVended.get(false), RETURN_COINS);
        } else {
            return new VendingInput(itemsByIsVended.get(true), itemsByIsVended.get(false), moniesByIsVended.get(true),
                    moniesByIsVended.get(false), GET);
        }
    }

    private boolean isCoinReturnInstruction(String[] tokens) {
        return Arrays.stream(tokens).anyMatch(RETURN_COINS.getParsingString()::equals);
    }


    Item getVendedOrUnVendedItem(String token) {
        String itemId = token.substring(GET.getParsingString().length());
        return Optional.ofNullable(VendingDataService.getVendableProductsMap().get(itemId)).orElse(newItem(itemId));
    }

    Money getVendedOrUnVendedMoney(String token) {
        return Optional.ofNullable(VendingDataService.getVendableMoneiesMap().get(token)).orElse(newMoney(token));
    }

    boolean isItem(String token) {
        return token.startsWith(GET.getParsingString());
    }

    boolean isMoney(String token) {
        return !(token.startsWith(GET.getParsingString()) || token.equals(RETURN_COINS.getParsingString()));
    }


}
