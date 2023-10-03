package com.mrp4sten.junit5_testing.examples.models;

import java.math.BigDecimal;

public class Bank {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void transfer(Account origin, Account destiny, BigDecimal ammount) {
    origin.debit(ammount);
    destiny.credit(ammount);
  }
}
