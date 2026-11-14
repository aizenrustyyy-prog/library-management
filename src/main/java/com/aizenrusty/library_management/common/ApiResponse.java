package com.aizenrusty.library_management.common;

public class ApiResponse<T> {

    private String isSuccess;   // "Y" or "N"
    private T data;
    private Object errors;

    public ApiResponse() {
    }

    private ApiResponse(String isSuccess, T data, Object errors) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("Y", data, null);
    }

    public static <T> ApiResponse<T> failure(Object errors) {
        return new ApiResponse<>("N", null, errors);
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}