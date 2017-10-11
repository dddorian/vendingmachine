package com.dorian.vendingmachine.service;

import com.dorian.vendingmachine.domain.VendingInput;
import com.dorian.vendingmachine.domain.VendingOutput;

/**
 * Translates the user input into vending instructions for the vending machine.
 * Translates the output of the vending machine's instruction processing into user readable output .
 */
public interface VendingParser {

    VendingInput parseVendingInstruction(String vendingInstruction);

}

