package com.mrp4sten.junit5_testing.examples.models;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  void testAccountName() {
    Account account = new Account();
    account.setPerson("Mauricio");

    String expected = "Mauricio";
    String result = account.getPerson();

    assertEquals(expected, result);
  }

  @Test
  void testAccountBalance() {
    Account account = new Account();
    BigDecimal balance = new BigDecimal("30000");
    account.setBalance(balance);

    BigDecimal expected = balance;
    BigDecimal result = account.getBalance();
    assertEquals(expected, result);
    assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
    assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);

  }

  @Test
  void testReferenceAccount() {
    Account account = new Account("Jhon Doe", new BigDecimal("8900.9997"));
    Account accountTwo = new Account("Jhon Doe", new BigDecimal("8900.9997"));

    /** To validate instance */
    // assertNotEquals(accountTwo, account);

    assertEquals(accountTwo, account);
  }

  @Test
  void testDebitAccount() {
    Account account = new Account("Jhon Doe", new BigDecimal("1000.12345"));
    account.debit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(900, account.getBalance().intValue());
    assertEquals("900.12345", account.getBalance().toPlainString());

  }

  @Test
  void testCreditAccount() {
    Account account = new Account("Jhon Doe", new BigDecimal("1000.12345"));
    account.credit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(1100, account.getBalance().intValue());
    assertEquals("1100.12345", account.getBalance().toPlainString());

  }

}
