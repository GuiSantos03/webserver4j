package dev.guisantos03.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20; // 32 - Space
    private static final int CR = 0x0D; // 13 - Carriage return
    private static final int LF = 0x0A; // 18 - Line Feed

    /**
     * Parses an HTTP request from an input stream.
     *
     * @param  inputStream   the input stream containing the HTTP request
     * @return               the parsed HTTP request object
     * @throws HttpParsingException if there was an error parsing the HTTP request
     */
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        parseHeaders(reader, request);
        parseBody(reader, request);
        return request;
    }

    // TODO OMFG, do I really need to refactor this shit?
    /**
     * Parses the request line from the given input stream reader and updates the provided HttpRequest object.
     *
     * @param streamReader the input stream reader from which to read the request line
     * @param request      the HttpRequest object to update with the parsed request line
     * @throws IOException           if an I/O error occurs while reading the request line
     * @throws HttpParsingException  if the request line cannot be parsed correctly
     */
    private void parseRequestLine(InputStreamReader streamReader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder sb = new StringBuilder(); // Create a StringBuilder to store the parsed characters
        boolean methodParsed = false; // Flag to check if the request method has been parsed
        boolean requestTargetParsed = false; // Flag to check if the request target has been parsed
        int byteValue;
        while ((byteValue = streamReader.read()) >= 0) { // Read the input stream until the end
            if (byteValue == CR) { // Check if the byte value is a carriage return
                byteValue = streamReader.read(); // Read the next byte value
                if (byteValue == LF) { // Check if the next byte value is a line feed
                    processRequestLine(sb, methodParsed, requestTargetParsed, request); // Process the parsed request line
                    return; // Return from the function
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST); // Throw an exception if the next byte value is not a line feed
                }
            }
            if (byteValue == SP) { // Check if the byte value is a space
                if (!methodParsed) { // Check if the request method has not been parsed yet
                    processRequestMethod(sb, request); // Process the parsed request method
                    methodParsed = true; // Set the methodParsed flag to true
                } else if (!requestTargetParsed) { // Check if the request target has not been parsed yet
                    processRequestTarget(sb, request); // Process the parsed request target
                    requestTargetParsed = true; // Set the requestTargetParsed flag to true
                }
                sb.delete(0, sb.length()); // Clear the sb
            } else {
                sb.append((char) byteValue); // Append the parsed character to the sb
            }
        }
    }

    /**
     * Processes the request line and sets the HTTP version of the request.
     *
     * @param  processingDataBuffer   a StringBuilder containing the request line
     * @param  methodParsed          a boolean indicating if the HTTP method has been parsed
     * @param  requestTargetParsed   a boolean indicating if the request target has been parsed
     * @param  request               the HttpRequest object
     * @throws HttpParsingException  if the request line is invalid
     */
    private void processRequestLine(StringBuilder processingDataBuffer, boolean methodParsed, boolean requestTargetParsed, HttpRequest request) throws HttpParsingException {
        LOGGER.debug("Request Line VERSION to Process: {}", processingDataBuffer);
        if (!methodParsed || !requestTargetParsed) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        try {
            request.setHttpVersion(processingDataBuffer.toString());
        } catch (BadHttpVersionException e) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    /**
     * Processes the request method.
     *
     * @param  processingDataBuffer  the data buffer used for processing
     * @param  request              the HTTP request object
     * @throws HttpParsingException if there is an error parsing the HTTP request
     */
    private void processRequestMethod(StringBuilder processingDataBuffer, HttpRequest request) throws HttpParsingException {
        LOGGER.debug("Request Line METHOD to Process: {}", processingDataBuffer);
        request.setMethod(processingDataBuffer.toString());
    }

    /**
     * A description of the entire Java function.
     *
     * @param  processingDataBuffer     description of parameter
     * @param  request                  description of parameter
     */
    private void processRequestTarget(StringBuilder processingDataBuffer, HttpRequest request) {
        LOGGER.debug("Request Line REQUEST TARGET to Process: {}", processingDataBuffer);
        request.setRequestTarget(processingDataBuffer.toString());
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {
    }
}
