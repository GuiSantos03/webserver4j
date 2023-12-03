package dev.guisantos03.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/", request.getRequestTarget());
        assertEquals("HTTP/1.1", request.getHttpVersion());
        assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleHttpVersion());
    }

    @Test
    void parseHttpRequestBadMethod1() {
        try {
            httpParser.parseHttpRequest(generateBadTestCaseMethodName1());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestBadMethod2() {
        try {
            httpParser.parseHttpRequest(generateBadTestCaseMethodName2());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestInvNumItems1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseRequestLineInvNumItems1());
            assertEquals("/", request.getRequestTarget());
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpEmptyRequestLine() {
        try {
            httpParser.parseHttpRequest(generateBadTestCaseEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestLineCRnoLF() {
        try {
            httpParser.parseHttpRequest(generateBadTestCaseRequestLineOnlyCRnoLF());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestBadHttpVersion() {
        try {
            httpParser.parseHttpRequest(generateBadHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            httpParser.parseHttpRequest(generateUnsuportedHttpVersionTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, e.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestSupportedHttpVersion1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateSupportedHttpVersion1());
            assertNotNull(request);
            assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleHttpVersion());
            assertEquals("HTTP/1.2", request.getHttpVersion());
        } catch (HttpParsingException e) {
            fail();
        }
    }

    private InputStream generateValidGETTestCase() {
        String rawData = """
                GET / HTTP/1.1\r
                Host: localhost:8080\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r
                Sec-Fetch-User: ?1\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-Mode: navigate\r
                Accept-Encoding: gzip, deflate, br\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadTestCaseMethodName1() {
        String rawData = """
                GeT / HTTP/1.1\r
                Host: localhost:8080\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadTestCaseMethodName2() {
        String rawData = """
                GETTTT / HTTP/1.1\r
                Host: localhost:8080\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadTestCaseRequestLineInvNumItems1() {
        String rawData = """
                GET / AAAAAA HTTP/1.1\r
                Host: localhost:8080\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadTestCaseEmptyRequestLine() {
        String rawData = """
                \r
                Host: localhost:8080\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF() {
        String rawData = "GET / HTTP/1.1\r" + // <----- no LF
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateBadHttpVersionTestCase() {
        String rawData = """
                GET / HTP/1.1\r
                Host: localhost:8080\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r
                Sec-Fetch-User: ?1\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-Mode: navigate\r
                Accept-Encoding: gzip, deflate, br\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateUnsuportedHttpVersionTestCase() {
        String rawData = """
                GET / HTTP/2.1\r
                Host: localhost:8080\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r
                Sec-Fetch-User: ?1\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-Mode: navigate\r
                Accept-Encoding: gzip, deflate, br\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }

    private InputStream generateSupportedHttpVersion1() {
        String rawData = """
                GET / HTTP/1.2\r
                Host: localhost:8080\r
                Connection: keep-alive\r
                Upgrade-Insecure-Requests: 1\r
                User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r
                Sec-Fetch-User: ?1\r
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r
                Sec-Fetch-Site: none\r
                Sec-Fetch-Mode: navigate\r
                Accept-Encoding: gzip, deflate, br\r
                Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r
                \r
                """;

        return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
    }
}