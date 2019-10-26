package com.example.potigianim.utils.requests;

public class ResponseObject<T> {
    private boolean success;
    private T object;
    private ResponseErrorInfo errorInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public ResponseErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ResponseErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }
}
