package com.beassolution.ruleengine.security;

import com.beassolution.ruleengine.properties.KeycloakProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
public class KeycloakAuthService {

    private final OkHttpClient httpClient;
    private final KeycloakProperties keycloakProperties;

    private static final MediaType FORM =
            MediaType.parse("application/x-www-form-urlencoded");

    public KeycloakAuthService(OkHttpClient httpClient, KeycloakProperties keycloakProperties) {
        this.httpClient = httpClient;
        this.keycloakProperties = keycloakProperties;
    }

    public String fetchTokenWithClientCredentials() {
        String form = buildForm(Map.of(
                "grant_type", keycloakProperties.getGrantType(),
                "client_id", keycloakProperties.getClientId(),
                "client_secret", keycloakProperties.getClientSecret()
        ));

        RequestBody body = RequestBody.create(form, FORM);

        Request request = new Request.Builder()
                .url(keycloakProperties.getTokenEndpoint())
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Token retrieve failed: "
                        + response.code() + " - " + response.message());
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("No body in response");
            }

            return responseBody.string();
        } catch (IOException e) {
            throw new RuntimeException("Keycloak access error", e);
        }
    }

    private String buildForm(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> {
            if (!sb.isEmpty()) sb.append('&');
            sb.append(encode(k)).append('=').append(encode(v));
        });
        return sb.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
