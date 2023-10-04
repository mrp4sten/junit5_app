package com.mrp4sten.junit5_testing.examples.models;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import com.mrp4sten.junit5_testing.examples.exceptions.InsufficientBalanceException;

class AccountTest {

  private Account account;

  @BeforeEach
  void initMethod() {
    this.account = new Account("Jhon Doe", new BigDecimal("1000.12345"));
    System.out.println("This is BeforeEach method");
  }

  @AfterEach
  void tearDown() {
    System.out.println("This is an After each method");
  }

  @BeforeAll
  static void beforeAll() {
    System.out.println("This method only execute once before all");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("This method only execute once after all");
  }

  @Test
  @Disabled("Thats example of using Disabled")
  @DisplayName("Testing the account name")
  void testAccountName() {
    Account account = new Account();
    account.setPerson("Mauricio");

    String expected = "Mauricio";
    String result = account.getPerson();

    assertEquals(expected, result,
        () -> "The account name is not correct. Value expected: " + expected + " But the value is: " + result);
  }

  @Test
  @DisplayName("Testing account balance")
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
  @DisplayName("Testing reference account")
  void testReferenceAccount() {
    account = new Account("Jhon Doe", new BigDecimal("8900.9997"));
    Account accountTwo = new Account("Jhon Doe", new BigDecimal("8900.9997"));

    /** To validate instance */
    // assertNotEquals(accountTwo, account);

    assertEquals(accountTwo, account);
  }

  @Test
  @DisplayName("Testing debit from account")
  void testDebitAccount() {
    account.debit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(900, account.getBalance().intValue());
    assertEquals("900.12345", account.getBalance().toPlainString());

  }

  @Test
  @DisplayName("Testing credit from account")
  void testCreditAccount() {
    account.credit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(1100, account.getBalance().intValue());
    assertEquals("1100.12345", account.getBalance().toPlainString());

  }

  @Test
  @DisplayName("Testing exception from Insufficient balance")
  void testInsufficientBalanceException() {
    BigDecimal charge = new BigDecimal("1500");

    Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
      account.debit(charge);
    });

    String current = exception.getMessage();
    String expected = "Insufficient Balance";

    assertEquals(expected, current);
  }

  @Test
  @DisplayName("Testing transfer")
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
  @DisplayName("Testing bank account relationship")
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

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testOnlyOnWindows() {
    System.out.println("Test only on Windows");
  }

  @Test
  @DisabledOnOs(OS.WINDOWS)
  void testNoOnWindows() {
    System.out.println("Test no on Windows");
  }

  @Test
  @EnabledOnOs({ OS.LINUX, OS.MAC })
  void testOnlyOnLinux() {
    System.out.println("Test only on Linux");
  }

  @Test
  @EnabledOnJre(JRE.JAVA_8)
  void testOnlyOnJre8() {
    System.out.println("Test only on Jre 8");
  }

  @Test
  @DisabledOnJre(JRE.JAVA_22)
  void testNoOnJre22() {
    System.out.println("Test no on Jre 22");
  }
}
