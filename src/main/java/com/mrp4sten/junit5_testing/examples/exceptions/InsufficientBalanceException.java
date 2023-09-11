package com.mrp4sten.junit5_testing.examples.exceptions;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(String message) {
    super(message);
  }
}
