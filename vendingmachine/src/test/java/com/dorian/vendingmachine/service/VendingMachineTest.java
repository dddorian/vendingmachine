package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.dorian.vendingmachine.domain.Item.newItem;
import static com.dorian.vendingmachine.domain.Money.newMoney;
import static org.junit.Assert.assertEquals;

public class VendingMachineTest {

    private VendingMachineImpl systemUnderTest;

    @Before
    public void setUp() throws Exception {
        systemUnderTest = new VendingMachineImpl(new VendingParserImpl());
    }

    @Test
    public void testAddChange() {
        List<Money> changetoSet = Lists.newArrayList(
                newMoney(VendingDataService.DOLLAR), newMoney(VendingDataService.DOLLAR),
                newMoney(VendingDataService.DIME), newMoney(VendingDataService.DIME),
                newMoney(VendingDataService.QUARTER), newMoney(VendingDataService.QUARTER),
                newMoney(VendingDataService.NICKEL), newMoney(VendingDataService.NICKEL));

        systemUnderTest.addChange(changetoSet);

        assertEquals(systemUnderTest.calculateReserveValue().compareTo(BigDecimal.valueOf(2.80)), 0);
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("DOLLAR")));
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("DIME")));
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("QUARTER")));
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("NICKEL")));
        assertEquals(systemUnderTest.getChangeReserve().size(), 4);
    }

    @Test
    public void testAddItems() {
        List<Item> itemsToAdd = (com.google.common.collect.Lists.newArrayList(
                newItem("A", new BigDecimal("0.65")),
                newItem("B", new BigDecimal("1")),
                newItem("C", new BigDecimal("1.50"))));

        systemUnderTest.addItems(itemsToAdd);

        Map<String, Item> itemsMap = VendingDataService.getVendableProductsMap();
        assertEquals(3, itemsMap.size());
        assertEquals( itemsMap.get("A").getPrice().compareTo(BigDecimal.valueOf(0.65)), 0);
        assertEquals( itemsMap.get("B").getPrice().compareTo(BigDecimal.valueOf(1)), 0);
        assertEquals( itemsMap.get("C").getPrice().compareTo(BigDecimal.valueOf(1.50)), 0);
    }


    @Test
    public void shouldUpdateReserveCountCorrectly(){
        List<Money> changetoSet = Lists.newArrayList(
                newMoney(VendingDataService.DOLLAR), newMoney(VendingDataService.DOLLAR),
                newMoney(VendingDataService.DIME), newMoney(VendingDataService.DIME),
                newMoney(VendingDataService.QUARTER), newMoney(VendingDataService.QUARTER),
                newMoney(VendingDataService.NICKEL), newMoney(VendingDataService.NICKEL));
        List<Item> itemsToAdd = Lists.newArrayList(newItem("A", new BigDecimal("0.65")),
                newItem("B", new BigDecimal("1")),
                newItem("C", new BigDecimal("1.50")));
        systemUnderTest.addChange(changetoSet);
        systemUnderTest.addItems(itemsToAdd);
        String userInput = "DOLLAR, DOLLAR, GET-C";

        systemUnderTest.vendItems(userInput);

        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("DOLLAR")));
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("DIME")));
        assertEquals(Long.valueOf(0), systemUnderTest.getChangeReserve().get(newMoney("QUARTER")));
        assertEquals(Long.valueOf(2), systemUnderTest.getChangeReserve().get(newMoney("NICKEL")));
        assertEquals(systemUnderTest.calculateReserveValue().compareTo(BigDecimal.valueOf(2.30)), 0);
    }

}