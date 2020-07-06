package br.com.southsystem.importer.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse {

    private final int httpStatus;
    private final List<ApiError> errors;

    static ApiErrorResponse of(HttpStatus status, List<ApiError> errors) {
        return new ApiErrorResponse(status.value(), errors);
    }

    static ApiErrorResponse of(HttpStatus status, ApiError error) {
        return of(status, Collections.singletonList(error));
    }

    @AllArgsConstructor
    @JsonAutoDetect(fieldVisibility = ANY)
    static class ApiError {

        private final String code;
        private final String userMessage;
    }
}
