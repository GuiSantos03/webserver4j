package dev.guisantos03.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;
    private HttpVersion bestCompatibleHttpVersion;

    HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) throws BadHttpVersionException, HttpParsingException {
        this.httpVersion = httpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(httpVersion);
        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public HttpVersion getBestCompatibleHttpVersion() {
        return bestCompatibleHttpVersion;
    }

    public void setBestCompatibleHttpVersion(HttpVersion bestCompatibleHttpVersion) {
        this.bestCompatibleHttpVersion = bestCompatibleHttpVersion;
    }
}
