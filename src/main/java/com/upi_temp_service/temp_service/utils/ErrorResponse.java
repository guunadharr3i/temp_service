package com.upi_temp_service.temp_service.utils;


public class ErrorResponse<T> {
    private int code;
    private String message;
    private T data;

    public ErrorResponse() {}

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getDefaultMessage();
    }

    public ErrorResponse(ErrorCode errorCode, String customMessage) {
        this.code = errorCode.getCode();
        this.message = customMessage;
    }

    public ErrorResponse(ErrorCode errorCode, String customMessage, T data) {
        this.code = errorCode.getCode();
        this.message = customMessage;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
