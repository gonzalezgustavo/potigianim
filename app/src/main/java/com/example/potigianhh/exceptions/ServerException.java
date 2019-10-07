package com.example.potigianhh.exceptions;

import com.example.potigianhh.utils.requests.ResponseErrorInfo;

public class ServerException extends RuntimeException {
    public ServerException(ResponseErrorInfo errorInfo) {
        super(errorInfo.getMessage() + ". Stacktrace: " + errorInfo.getStackTrace());
    }
}
