package br.com.southsystem.importer.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    private static final String NO_MESSAGE_AVAILABLE = "No message available";

    private final MessageSource apiErrorMessageSource;

    public ApiExceptionHandler(MessageSource apiErrorMessageSource) {
        this.apiErrorMessageSource = apiErrorMessageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handlerNotValidException(MethodArgumentNotValidException e, Locale locale) {

        Stream<ObjectError> errorStream = e.getBindingResult().getAllErrors().stream();

        List<ApiErrorResponse.ApiError> apiErrors = errorStream
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(defaultMessage -> toApiError(defaultMessage, locale))
                .collect(Collectors.toList());

        ApiErrorResponse errorResponse = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiErrorResponse> handlerInvalidFormatException(InvalidFormatException e, Locale locale) {

        final String errorCode = "generic-1";
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final ApiErrorResponse errorResponse = ApiErrorResponse.of(status, toApiError(errorCode, locale, e.getValue()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ApiErrorResponse> handleDataAccessExceptionException(DataAccessException e, Locale locale) {

        final String errorCode = "generic-error-1";

        final ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR, toApiError(errorCode, locale));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

    @ExceptionHandler({BusinessException.class, SecurityException.class})
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException e, Locale locale) {
        final ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(e.getStatus(), toApiError(e.getErrorCode(), locale));
        return ResponseEntity.badRequest().body(apiErrorResponse);
    }

    public ApiErrorResponse.ApiError toApiError(String code, Locale locale, Object... args) {

        String message;
        try {
            message = apiErrorMessageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            message = NO_MESSAGE_AVAILABLE;
        }

        return new ApiErrorResponse.ApiError(code, message);
    }
}
