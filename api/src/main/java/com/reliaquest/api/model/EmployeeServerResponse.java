package com.reliaquest.api.model;

public record EmployeeServerResponse<T> (T data, String status, String error) {
}
