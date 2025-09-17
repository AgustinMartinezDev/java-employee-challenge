package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Employee(UUID id,
                       @JsonProperty("employee_name")
                       String name,
                       @JsonProperty("employee_salary")
                       int salary,
                       @JsonProperty("employee_age")
                       int age,
                       @JsonProperty("employee_title")
                       String title,
                       @JsonProperty("employee_email")
                       String email)
{
}
