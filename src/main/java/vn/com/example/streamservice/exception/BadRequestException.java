package vn.com.example.streamservice.exception;

import org.zalando.problem.AbstractThrowableProblem;

public class BadRequestException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String messageCode) {
        this(null, messageCode);
    }

    public BadRequestException(Object object, String messageCode) {
        // TODO Auto-generated constructor stub
    }

}
