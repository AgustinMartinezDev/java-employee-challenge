package com.reliaquest.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateEmployeeRequest(@NotBlank String name,
                                    @Positive @NotNull int salary,
                                    @NotNull @Min(18) @Max(75) int age,
                                    @NotBlank String title)
{
}
