package com.github.forest.core.exception;

import com.github.forest.enumerate.TransactionCode;

/**
 * @author ronger
 */
public class TransactionException extends BusinessException {

    private int code;

    private String message;

    public TransactionException(TransactionCode transactionCode) {
        super(transactionCode.getMessage());
        this.code = transactionCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
