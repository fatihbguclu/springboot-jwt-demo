package com.jwt.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(HttpServletResponse response, CustomException exception) throws IOException {
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse response, AccessDeniedException exception) throws IOException{
        response.sendError(HttpStatus.FORBIDDEN.value(),"Access denied");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, Exception exception) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(),"Something went wrong");
    }

}
