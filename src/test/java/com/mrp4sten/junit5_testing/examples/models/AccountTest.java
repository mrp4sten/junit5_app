package com.mrp4sten.junit5_testing.examples.models;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.mrp4sten.junit5_testing.examples.exceptions.InsufficientBalanceException;

class AccountTest {

  @Test
  void testAccountName() {
    Account account = new Account();
    account.setPerson("Mauricio");

    String expected = "Mauricio";
    String result = account.getPerson();

    assertEquals(expected, result,
        () -> "The account name is not correct. Value expected: " + expected + " But the value is: " + result);
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

  @Test
  void testInsufficentBalanceException() {
    Account account = new Account("Jhon Doe", new BigDecimal("1000.12345"));
    BigDecimal charge = new BigDecimal("1500");

    Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
      account.debit(charge);
    });

    String current = exception.getMessage();
    String expected = "Insufficient Balance";

    assertEquals(expected, current);
  }

  @Test
  void testTransferBalanceAccount() {
    Account accountOne = new Account("Jhon Doe", new BigDecimal("2500"));
    Account accountTwo = new Account("Jhon Snow", new BigDecimal("1500.8989"));

    Bank bank = new Bank();
    bank.setName("State Bank");
    bank.transfer(accountTwo, accountOne, new BigDecimal(500));
    assertEquals("1000.8989", accountTwo.getBalance().toPlainString());
    assertEquals("3000", accountOne.getBalance().toPlainString());

  }

  @Test
  void testBankAccountRelationship() {
    Account accountOne = new Account("Jhon Doe", new BigDecimal("2500"));
    Account accountTwo = new Account("Jhon Snow", new BigDecimal("1500.8989"));

    Bank bank = new Bank();
    bank.addAccount(accountOne);
    bank.addAccount(accountTwo);

    bank.setName("State Bank");
    bank.transfer(accountTwo, accountOne, new BigDecimal(500));

    assertAll(() -> {
      assertEquals("1000.8989", accountTwo.getBalance().toPlainString());
    },
        () -> {
          assertEquals("3000", accountOne.getBalance().toPlainString());
        },
        () -> {
          assertEquals(2, bank.getAccounts().size());
        },
        () -> {
          assertEquals("State Bank", accountOne.getBank().getName());
        },
        () -> {
          assertEquals("State Bank", accountTwo.getBank().getName());
        },
        () -> {
          assertTrue(bank.getAccounts().stream()
              .anyMatch(account -> account.getPerson().equals("Jhon Snow")));
        });

  }

}
