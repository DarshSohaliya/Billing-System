package com.example.Billing.System.DTO;

public class ResponseDTO<T> {
    private String message;

    public String getMessage() {
        return message;
    }

    public ResponseDTO(String message, T data) {
        this.message = message;
        Data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    private T Data;
}
