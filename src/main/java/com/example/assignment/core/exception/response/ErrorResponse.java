package com.example.assignment.core.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse<T> {
    private Integer status;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T detail;
}