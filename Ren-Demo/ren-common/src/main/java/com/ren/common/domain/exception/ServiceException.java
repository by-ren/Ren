package com.ren.common.domain.exception;

public class ServiceException extends RuntimeException {
  public ServiceException(String message) {
    super(message);
  }
}
