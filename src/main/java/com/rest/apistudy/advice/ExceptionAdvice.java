package com.rest.apistudy.advice;

import com.rest.apistudy.exception.CUserNotFoundException;
import com.rest.apistudy.model.response.CommonResult;
import com.rest.apistudy.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private  final MessageSource ms;
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        Locale locale = request.getLocale();
        return responseService.getFailResult(Integer.valueOf(getMessage("unknown.code", locale)), getMessage("unknown.msg", locale));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        Locale locale = request.getLocale();
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code", locale)), getMessage("userNotFound.msg", locale));
    }

    private String getMessage(String code,Locale locale) {
        return getMessage(code, null, locale);
    }

    private String getMessage(String code, Object[] args, Locale locale) {
        return ms.getMessage(code, args, locale);
    }
}