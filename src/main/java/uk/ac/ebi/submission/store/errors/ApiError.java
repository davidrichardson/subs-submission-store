package uk.ac.ebi.submission.store.errors;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * The payload of an API Problem
 * <p>
 * Example payload:
 */
@Data
public class ApiError {

    /**
     * An URL to a submissionDocument describing the error condition (optional).
     */
    private String type;
    /**
     * A brief title for the error condition (required and should be the same for every problem of the same {@link ApiError#type}.
     */
    private String title;
    /**
     * The HTTP status code for the current request (required).
     */
    private int status;
    /**
     * URI identifying the specific instance of this problem (optional).
     */
    private String instance;
    /**
     * Error details specific to this request (optional).
     */
    private List<String> errors;

    public ApiError() {
    }

    public ApiError(HttpStatus httpStatus) {
        this.title = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
    }

    public ApiError(String type, HttpStatus httpStatus, String instance, List<String> errors) {
        this.type = type;
        this.title = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.instance = instance;
        this.errors = errors;
    }

    public ApiError(String type, HttpStatus httpStatus, String instance, String error) {
        this.type = type;
        this.title = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.instance = instance;
        this.errors = Arrays.asList(error);
    }
}