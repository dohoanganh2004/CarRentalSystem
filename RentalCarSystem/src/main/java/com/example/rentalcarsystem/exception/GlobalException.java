package com.example.rentalcarsystem.exception;

import com.example.rentalcarsystem.exception.orther_exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runTimeException(RuntimeException ex) {
      BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
      baseErrorResponse.setCode("400");
      baseErrorResponse.setStatus(ex.getMessage());
        return new ResponseEntity<>(baseErrorResponse.toString(), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> userNotFound(UsernameNotFoundException e) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setCode("404");
        baseErrorResponse.setStatus(e.getMessage());
        return new ResponseEntity<>(baseErrorResponse.toString(), HttpStatus.NOT_FOUND);
    }
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ValidatorErrorResponse> methodArgumentNotValidException  (MethodArgumentNotValidException e) {

       Map<String, String> errors = new HashMap<>();
       for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
           errors.put(fieldError.getField(), fieldError.getDefaultMessage());
       }
       ValidatorErrorResponse validatorErrorResponse = new ValidatorErrorResponse(errors);
       validatorErrorResponse.setCode("400");
       validatorErrorResponse.setStatus("Validation field!");
       validatorErrorResponse.setErrors(errors);

       return new ResponseEntity<>(validatorErrorResponse, HttpStatus.BAD_REQUEST);
   }
  @ExceptionHandler(UnAuthorizedException.class)
   public ResponseEntity<String> unAuthorizedException(Exception e) {
        BaseErrorResponse baseErrorResponse = new BaseErrorResponse();
        baseErrorResponse.setCode("401");
        baseErrorResponse.setStatus("Unauthorized");
        return new ResponseEntity<>(baseErrorResponse.toString(), HttpStatus.UNAUTHORIZED);
   }
}



