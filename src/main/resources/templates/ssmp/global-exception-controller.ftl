package ${packageName};

<#list dependencies! as d>
	import ${d}.*;
</#list>
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidArgumentException.class)
	public ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException e,
	                                                              WebRequest webRequest) {
		return handleExceptionInternal(e, Response.error(e.getMessage()),
			HttpHeaders.EMPTY, BAD_REQUEST, webRequest);
	}
}
