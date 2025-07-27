package com.shoppinglist.mgmt.exception;

/**
 * CustomApplicationException for returning custom Exception.
 * <p>
 * This class is a custom Exception to handle application exceptions.
 * </p>
 */
public class CustomApplicationException extends RuntimeException {
  public CustomApplicationException(String message) {
    super(message);
  }
}
