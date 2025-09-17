package com.reliaquest.api.model;

public record ServerResponse<T> (T data, String status) {
}
