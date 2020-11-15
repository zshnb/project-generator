package com.zshnb.web.common

import com.zshnb.web.exception.InvalidArgumentsException
import org.springframework.http.*
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(InvalidArgumentsException::class)
    fun handleInvalidArgumentsException(exception: InvalidArgumentsException, webRequest: WebRequest):
            ResponseEntity<Any> {
        return handleExceptionInternal(exception, Response.error(message = exception.msg),
            HttpHeaders.EMPTY, BAD_REQUEST, webRequest)
    }
}