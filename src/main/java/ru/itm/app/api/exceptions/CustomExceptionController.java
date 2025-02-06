package ru.itm.app.api.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class CustomExceptionController implements ErrorController {
    private final ErrorAttributes errorAttributes;

    public CustomExceptionController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ResponseEntity<ErrorDTO> error(WebRequest webRequest) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS)
        );

        Integer status = attributes.get("status") != null
                ? (Integer) attributes.get("status")
                : HttpStatus.INTERNAL_SERVER_ERROR.value();

        String error = attributes.get("error") != null ? attributes.get("error").toString() : "Error";
        String message = attributes.get("message") != null ? attributes.get("message").toString() : "No message available";

        ErrorDTO errorDTO = new ErrorDTO(error, message);

        return ResponseEntity.status(status).body(errorDTO);
    }
}
