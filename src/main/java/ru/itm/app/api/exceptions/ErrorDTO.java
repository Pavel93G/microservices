package ru.itm.app.api.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDTO {
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;

    public ErrorDTO(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
