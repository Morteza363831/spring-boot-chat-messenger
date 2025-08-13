package com.example.messenger.utility;

import com.example.messenger.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Validator {

    private final jakarta.validation.Validator validator;

    public void validate(Object dto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }
}
