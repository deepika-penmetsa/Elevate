package org.elevate.exceptions;

import org.elevate.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildResponse(String error, String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Handle User Already Exists Exception
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildResponse("User already exists", ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handle User Not Found Exception
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponse("User not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Club Limit Exceeded Exception
     */
    @ExceptionHandler(ClubLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleClubLimitExceededException(ClubLimitExceededException ex) {
        return buildResponse("Club limit exceeded", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Club Capacity Exceeded Exception
     */
    @ExceptionHandler(ClubCapacityExceededException.class)
    public ResponseEntity<Map<String, String>> handleClubCapacityExceededException(ClubCapacityExceededException ex) {
        return buildResponse("Club capacity exceeded", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Club Not Found Exception
     */
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleClubNotFoundException(ClubNotFoundException ex) {
        return buildResponse("Club not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Invalid Request Exception
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestException(InvalidRequestException ex) {
        return buildResponse("Invalid request", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Question Not Found Exception
     */
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return buildResponse("Question not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Record Not Found Exception
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRecordNotFoundException(RecordNotFoundException ex) {
        return buildResponse("Record not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Request Already Exists Exception
     */
    @ExceptionHandler(RequestAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleRequestAlreadyExistsException(RequestAlreadyExistsException ex) {
        return buildResponse("Request already exists", ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handle Unauthorized Action Exception
     */
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        return buildResponse("Unauthorized action", ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handle Undefined User Club Exception
     */
    @ExceptionHandler(UndefinedUserClubException.class)
    public ResponseEntity<Map<String, String>> handleUndefinedUserClubException(UndefinedUserClubException ex) {
        return buildResponse("User not part of any club", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Generic Exceptions (Fallback)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        return buildResponse("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
