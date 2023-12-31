package com.mrp4sten.junit5_testing.examples.models;

import java.math.BigDecimal;

import com.mrp4sten.junit5_testing.examples.exceptions.InsufficientBalanceException;

public class Account {
  private String person;
  private BigDecimal balance;
  private Bank bank;

  public Account() {
  }

  public Account(String person, BigDecimal balance) {
    this.person = person;
    this.balance = balance;
  }

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

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public void debit(BigDecimal amount) {
    BigDecimal newBalance = this.balance.subtract(amount);
    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new InsufficientBalanceException("Insufficient Balance");
    }

    this.balance = newBalance;
  }

  public void credit(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }

    Account account = (Account) obj;
    if (!(obj instanceof Account)) {
      return false;
    }

    if (this.person == null || this.balance == null) {
      return false;
    }
    return this.person.equals(account.getPerson()) && this.balance.equals(account.getBalance());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
