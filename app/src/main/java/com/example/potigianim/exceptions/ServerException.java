package com.example.potigianim.exceptions;

import com.example.potigianim.utils.requests.ResponseErrorInfo;

public class ServerException extends RuntimeException {
    public ServerException(ResponseErrorInfo errorInfo) {
        super(errorInfo.getMessage() + ". Stacktrace: " + errorInfo.getStackTrace());
    }
}
