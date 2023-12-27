package com.mrp4sten.junit5_testing.examples.models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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
  @Tag("account")
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
  @Tag("account")
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
  @Tag("account")
  @DisplayName("Testing reference account")
  void testReferenceAccount() {
    account = new Account("Jhon Doe", new BigDecimal("8900.9997"));
    Account accountTwo = new Account("Jhon Doe", new BigDecimal("8900.9997"));

    /** To validate instance */
    // assertNotEquals(accountTwo, account);

    assertEquals(accountTwo, account);
  }

  @Test
  @Tag("account")
  @DisplayName("Testing debit from account")
  void testDebitAccount() {
    account.debit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(900, account.getBalance().intValue());
    assertEquals("900.12345", account.getBalance().toPlainString());

  }

  @Test
  @Tag("account")
  @DisplayName("Testing credit from account")
  void testCreditAccount() {
    account.credit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(1100, account.getBalance().intValue());
    assertEquals("1100.12345", account.getBalance().toPlainString());

  }

  @Test
  @Tag("exception")
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
  @Tag("account")
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
  @Tag("account")
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

  // @RepeatedTest(5)
  @Tag("repeated")
  @DisplayName("Testing debit from account with RepeatedTest")
  @RepeatedTest(value = 5, name = "{displayName} - Repetition number {currentRepetition} of {totalRepetitions}")
  void repeatedTestDebitAccount(RepetitionInfo info) {
    if (info.getCurrentRepetition() == 3) {
      System.out.println("This is the third repetition");
    }
    account.debit(new BigDecimal("100"));

    assertNotNull(account.getBalance());
    assertEquals(900, account.getBalance().intValue());
    assertEquals("900.12345", account.getBalance().toPlainString());

  }

  @Tag("parameterized")
  @Nested
  @DisplayName("Testing debit from account with ParameterizedTest")
  class ParameterizedTesting {

    @ParameterizedTest(name = "Testing debit from account with ParameterizedTest with value {0} - {argumentsWithNames}")
    @ValueSource(strings = { "100", "200", "300", "500", "700", "1000" })
    void parameterizedValueSourceTestDebitAccount(String amount) {
      account.debit(new BigDecimal(amount));

      assertNotNull(account.getBalance());
      assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "Testing debit from account with ParameterizedTest with value {0} - {argumentsWithNames}")
    @CsvSource({ "1,100", "2,200", "3,300", "4,500", "5,700", "6,1000" })
    void parameterizedCsvSourceTestDebitAccount(String index, String amount) {
      System.out.println("Index: " + index + " - Amount: " + amount);
      account.debit(new BigDecimal(amount));

      assertNotNull(account.getBalance());
      assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "Testing debit from account with ParameterizedTest with value {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void parameterizedCsvFileSourceTestDebitAccount(String amount) {
      account.debit(new BigDecimal(amount));

      assertNotNull(account.getBalance());
      assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "Testing debit from account with ParameterizedTest with value {0} - {argumentsWithNames}")
    @MethodSource("amountList")
    void parameterizedMethodSourceTestDebitAccount(String amount) {
      account.debit(new BigDecimal(amount));

      assertNotNull(account.getBalance());
      assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    List<String> amountList() {
      return Arrays.asList("100", "200", "300", "500", "700", "1000");
    }

  }

  @Tag("os")
  @Nested
  @DisplayName("Testing for OS")
  class OSTest {
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
  }

  @Tag("java")
  @Nested
  @DisplayName("Testing for Java Version")
  class JavaVersionTest {
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

  @Tag("environment")
  @Nested
  @DisplayName("Testing for System and Environment Properties")
  class SystemAndEnvProperiesTest {
    @Test
    void printEnvVars() {
      System.getenv().forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "HOME", matches = "/home/mrp4sten")
    void testHome() {

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ZSH", matches = "/home/mrp4sten/.oh-my-zsh")
    void testZsh() {

    }

    @Test
    @DisplayName("Testing account balance dev")
    void testAccountBalanceDev() {
      assumeTrue(System.getProperty("ENV").equals("dev"));

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
    @DisplayName("Testing account balance dev 2")
    void testAccountBalanceDev2() {
      assumingThat(System.getProperty("ENV").equals("dev"), () -> {
        Account account = new Account();
        BigDecimal balance = new BigDecimal("30000");
        account.setBalance(balance);

        BigDecimal expected = balance;
        BigDecimal result = account.getBalance();
        assertEquals(expected, result);
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
      });

    }
  }
}
