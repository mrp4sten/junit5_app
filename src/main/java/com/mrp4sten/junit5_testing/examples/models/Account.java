package com.mrp4sten.junit5_testing.examples.models;

import java.math.BigDecimal;

public class Account {
  private String person;
  private BigDecimal balance;

  public String getPerson() {
    return person;
  }

  public void setPerson(String person) {
    this.person = person;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

}
