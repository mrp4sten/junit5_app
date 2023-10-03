package com.mrp4sten.junit5_testing.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
  private List<Account> accounts;
  private String name;

  public Bank() {
    accounts = new ArrayList<>();
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  public void addAccount(Account account) {
    accounts.add(account);
    account.setBank(this);
  }

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
