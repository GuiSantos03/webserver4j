package dev.guisantos03.http;

public class HttpParsingException extends Exception {

    private final HttpStatusCode errorCode;

    public HttpParsingException(HttpStatusCode errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
