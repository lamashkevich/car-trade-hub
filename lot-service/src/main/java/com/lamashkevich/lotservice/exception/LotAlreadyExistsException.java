package com.lamashkevich.lotservice.exception;

public class LotAlreadyExistsException extends RuntimeException {
    public LotAlreadyExistsException(Integer lotNumber) {
        super("Lot " + lotNumber + " already exists");
    }
}
