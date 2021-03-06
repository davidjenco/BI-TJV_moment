package cz.cvut.fit.tjv.moment.api.controller;

import cz.cvut.fit.tjv.moment.business.CheckCustomerAgeWarningException;
import cz.cvut.fit.tjv.moment.business.ElementAlreadyExistsException;
import cz.cvut.fit.tjv.moment.business.LuckyWinException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    //ResponseEntity je třída, kterou Spring umí zkonvertovat na HTTP odpověď
    @ExceptionHandler(ElementAlreadyExistsException.class)
    protected ResponseEntity<?> handleAlreadyExists(Exception e, WebRequest webRequest){
        String bodyOfResponse = "Element already exist";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handleNotExists(Exception e, WebRequest webRequest){
        String bodyOfResponse = "Element not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<?> handleNullPointer(Exception e, WebRequest webRequest){
        String bodyOfResponse = "Wrong data structure";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(CheckCustomerAgeWarningException.class)
    protected ResponseEntity<?> handleCustomerAgeCheck(Exception e, WebRequest webRequest){
        String bodyOfResponse = "You should check, whether customer is old enough.";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.OK, webRequest);
    }

    @ExceptionHandler(LuckyWinException.class)
    protected ResponseEntity<?> handleWinnerNotification(Exception e, WebRequest webRequest){
        String bodyOfResponse = "Customer hit lucky number: order is free of charge!";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.OK, webRequest);
    }
}
