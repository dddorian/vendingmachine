package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Item;
import com.dorian.vendingmachine.domain.Money;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static com.dorian.vendingmachine.domain.Item.newItem;
import static org.junit.Assert.*;

public class VendingDataServiceTest {
    private VendingDataService vendingDataService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddVendingItems() throws Exception {
        Map<String, Item> itemsMap = VendingDataService.getVendableProductsMap();
        assertEquals(3, itemsMap.size());
        assertEquals( itemsMap.get("A").getPrice().compareTo(BigDecimal.valueOf(0.65)), 0);
        assertEquals( itemsMap.get("B").getPrice().compareTo(BigDecimal.valueOf(1)), 0);
        assertEquals( itemsMap.get("C").getPrice().compareTo(BigDecimal.valueOf(1.50)), 0);
    }

    @Test
    public void testInitialiseVendingMonies() throws Exception {
        Map<String, Money> moniesMap = VendingDataService.getVendableMoneiesMap();
        assertEquals(4, moniesMap.size());
        assertEquals( moniesMap.get(VendingDataService.DOLLAR).getValue().compareTo(BigDecimal.valueOf(1)), 0);
        assertEquals( moniesMap.get(VendingDataService.QUARTER).getValue().compareTo(BigDecimal.valueOf(0.25)), 0);
        assertEquals( moniesMap.get(VendingDataService.DIME).getValue().compareTo(BigDecimal.valueOf(0.1)), 0);
        assertEquals( moniesMap.get(VendingDataService.NICKEL).getValue().compareTo(BigDecimal.valueOf(0.05)), 0);
    }

}