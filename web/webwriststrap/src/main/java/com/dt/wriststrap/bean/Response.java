package com.dt.wriststrap.bean;


public class Response<T> {
    private boolean success;
    private String message;
    private T result;

    public Response() {
    }

    private Response(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    private Response(boolean success) {
        this.success = success;
    }

    private Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private Response(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public static Response buildOK() {
        return new Response(true);
    }

    public static <T> Response<T> buildOK(T result) {
        return new Response<>(true, result);
    }

    public static Response buildError(String errorMsg) {
        return new Response(false, errorMsg);
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
