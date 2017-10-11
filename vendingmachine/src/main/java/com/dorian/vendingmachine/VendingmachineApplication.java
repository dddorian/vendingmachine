package com.dorian.vendingmachine;

import com.dorian.vendingmachine.service.VendingMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class VendingmachineApplication implements CommandLineRunner {

    @Autowired
    private VendingMachine vendingMachine;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(VendingmachineApplication.class, args);

    }

    //access command line arguments
    @Override
    public void run(String... args) throws Exception {
        System.out.println("The coins have to be entered in capitals. The currently valid coins are DOLLAR, QUARTER," +
                " DIME " +
                "and NICKEL.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Enter coins, and item prefixed with GET- item comma separated. " +
                        "Example of a valid vending machine expression: QUARTER, QUARTER, DIME, GET-B");
                String vending = scanner.nextLine();


                String outcome = vendingMachine.vendItems(vending);
                System.out.println("Outcome: " + outcome);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}