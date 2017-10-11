package com.dorian.vendingmachine.domain;

public class Token {
    final String id;
    final boolean isVended;

    public Token(String id, boolean isVended) {
        this.id = id;
        this.isVended = isVended;
    }

    public boolean isVended() {
        return isVended;
    }

    public String getId() {
        return id;
    }
}
