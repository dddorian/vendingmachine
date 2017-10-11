package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.Instructions;
import com.dorian.vendingmachine.domain.VendingInput;
import org.junit.Before;
import org.junit.Test;

import static com.dorian.vendingmachine.domain.Item.newItem;
import static com.dorian.vendingmachine.domain.Money.newMoney;
import static org.junit.Assert.*;

public class VendingParserTest {

    private final VendingParserImpl parser = new VendingParserImpl();

    @Before
    public void setUp() {
//        VendingParserImpl.initialiseVendingItems(Sets.newHashSet(newItem("A", 0.65), newItem("B", 1),
//                newItem("C", 1.75)));
//        VendingParserImpl.initialiseVendingMonies(Sets.newHashSet(newMoney("DOLLAR", 1),
//                newMoney("DIME", .1),
//                newMoney("QUARTER", .25), newMoney("CENT", .05)));
    }

    @Test
    public void testIsMoney() {
        assertTrue(parser.isMoney("Any"));
        assertFalse(parser.isMoney("GET-a"));
        assertFalse(parser.isMoney("RETURN-COINS"));
        assertTrue(parser.isMoney("DOLLAR"));
    }

    @Test
    public void testIsItem() {
        assertFalse(parser.isItem("Any"));
        assertTrue(parser.isItem("GET-a"));
        assertFalse(parser.isItem("RETURN-COINS"));
        assertFalse(parser.isItem("DOLLAR"));
    }

    @Test
    public void testIsVendedOrNotVendItem() {
        assertFalse(parser.getVendedOrUnVendedItem("notVended").isVended());
        assertTrue(parser.getVendedOrUnVendedItem("GET-A").isVended());
        assertTrue(parser.getVendedOrUnVendedItem("GET-B").isVended());
        assertTrue(parser.getVendedOrUnVendedItem("GET-C").isVended());
    }

    @Test
    public void testIsVendedOrNotVendMoney() {
        assertFalse(parser.getVendedOrUnVendedMoney("notVended").isVended());
        assertTrue(parser.getVendedOrUnVendedMoney("DOLLAR").isVended());
        assertTrue(parser.getVendedOrUnVendedMoney("QUARTER").isVended());
        assertTrue(parser.getVendedOrUnVendedMoney("DIME").isVended());
        assertTrue(parser.getVendedOrUnVendedMoney("NICKEL").isVended());
    }

    @Test
    public void testInvalidProductReturned() {
        VendingInput parsingInput = parser.parseVendingInstruction("GET-notVended");
        assertTrue(parsingInput.getUnVendedItems().size() > 0);
        assertEquals("notVended", parsingInput.getUnVendedItems().get(0).getId());
        assertEquals(Instructions.RETURN_COINS, parsingInput.getInstruction());

    }

    @Test
    public void testValidProductReturned() {
        VendingInput parsingInput = parser.parseVendingInstruction("GET-C");
        assertTrue(parsingInput.getItems().size() > 0);
        assertEquals("C", parsingInput.getItems().get(0).getId());
        assertEquals(Instructions.GET, parsingInput.getInstruction());
    }

    @Test
    public void testInvalidMoneyReturned() {
        VendingInput parsingInput = parser.parseVendingInstruction("notVended");
        assertTrue(parsingInput.getUnVendedMonies().size() > 0);
        assertEquals("notVended", parsingInput.getUnVendedMonies().get(0).getId());
        assertEquals(Instructions.RETURN_COINS, parsingInput.getInstruction());
    }

    @Test
    public void testValidMoneyReturned() {
        VendingInput parsingInput = parser.parseVendingInstruction("DOLLAR");
        assertTrue(parsingInput.getMonies().size() > 0);
        assertEquals("DOLLAR", parsingInput.getMonies().get(0).getId());
        assertEquals(Instructions.RETURN_COINS, parsingInput.getInstruction());
    }

    @Test
    public void testValidMoneyItemAllowSpaces() {
        VendingInput parsingInput = parser.parseVendingInstruction(" DOLLAR,  QUARTER, , GET-B ");
        assertTrue(parsingInput.getMonies().size() == 2);
        assertTrue(parsingInput.getItems().size() > 0);

        assertEquals("DOLLAR", parsingInput.getMonies().get(0).getId());
        assertEquals("QUARTER", parsingInput.getMonies().get(1).getId());
        assertEquals("B", parsingInput.getItems().get(0).getId());
        assertEquals(Instructions.GET, parsingInput.getInstruction());
    }

    @Test
    public void testValidAndInValidMoneyItemAllowSpaces() {
        VendingInput parsingInput = parser.parseVendingInstruction(" DOLLAR,  POUND, , GET-B ");
        assertTrue(parsingInput.getMonies().size() == 1);
        assertTrue(parsingInput.getItems().size() > 0);
        assertTrue(parsingInput.getUnVendedMonies().size() > 0);

        assertEquals("DOLLAR", parsingInput.getMonies().get(0).getId());
        assertEquals("POUND", parsingInput.getUnVendedMonies().get(0).getId());
        assertEquals("B", parsingInput.getItems().get(0).getId());
        assertEquals(Instructions.GET, parsingInput.getInstruction());
    }

}
