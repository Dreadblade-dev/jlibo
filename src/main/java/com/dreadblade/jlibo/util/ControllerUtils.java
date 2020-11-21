package com.dreadblade.jlibo.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    public static Map<String, String> getValidationErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "IsInvalid",
                FieldError::getDefaultMessage);

        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
