package com.innowise.swimdom.util;

import static com.innowise.swimdom.util.TestConstants.USER_AUTH_URI;
import static com.innowise.swimdom.util.TestConstants.USER_INFO_RESPONSE_DTO;
import static com.innowise.swimdom.util.TestConstants.VALID_AUTHENTICATION_REQUEST_DTO;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Utility class for tests.
 */
public class TestUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Stubs user service authentication endpoint for a given login.
     */
    public static void mockUserService() {
        String jsonUser;
        String jsonAuthorizationRequestDto;
        try {
            jsonUser = MAPPER.writeValueAsString(USER_INFO_RESPONSE_DTO);
            jsonAuthorizationRequestDto = MAPPER.writeValueAsString(VALID_AUTHENTICATION_REQUEST_DTO);
        } catch (JsonProcessingException e) {
            jsonUser = null;
            jsonAuthorizationRequestDto = null;
        }

        stubFor(post(USER_AUTH_URI)
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
            .withRequestBody(equalToJson(jsonAuthorizationRequestDto))
            .willReturn(aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(jsonUser)));
    }
}
