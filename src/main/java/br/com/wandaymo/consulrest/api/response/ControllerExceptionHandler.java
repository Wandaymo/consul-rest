package br.com.wandaymo.consulrest.api.response;

import com.auth0.jwt.exceptions.TokenExpiredException;
import java.time.ZonedDateTime;
import javax.naming.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
        var exceptionMessage = e.getMessage();
        LOGGER.warn("{}", exceptionMessage);
        var errorResponse = new ErrorResponse(exceptionMessage, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        var exceptionMessage = e.getMessage();
        LOGGER.warn("{}", exceptionMessage);
        var errorResponse = new ErrorResponse(exceptionMessage, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        var exceptionMessage = ex.getMessage();
        LOGGER.warn("{}", exceptionMessage);
        var errorResponse = new ErrorResponse(exceptionMessage, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> tokenExpiredException(Exception ex) {
        var exceptionMessage = ex.getMessage();
        LOGGER.warn("{}", exceptionMessage);
        var errorResponse = new ErrorResponse(exceptionMessage, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, null, HttpStatus.UNAUTHORIZED);
    }
}
