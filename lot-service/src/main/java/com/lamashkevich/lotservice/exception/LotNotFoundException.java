package com.lamashkevich.lotservice.exception;

public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(Long id) {
        super("Lot with id " + id + " not found");
    }
}
