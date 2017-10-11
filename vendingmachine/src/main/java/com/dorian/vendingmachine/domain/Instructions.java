package com.dorian.vendingmachine.domain;

public enum Instructions {
    RETURN_COINS("RETURN-COINS"), GET("GET-");

    private final String parsingString;

    Instructions(String parsingString) {
        this.parsingString = parsingString;
    }

    public String getParsingString() {
        return parsingString;
    }

}
