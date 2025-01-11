package com.ll.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authorizationRequest, request);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
        if (authorizationRequest == null || request == null) {
            return null;
        }

        // Extract custom state parameter from the request
        String redirectUrl = request.getParameter("redirectUrl");

        // Copy existing additional parameters and add the custom state parameter
        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            additionalParameters.put("state", redirectUrl);
        }

        // Build a new OAuth2AuthorizationRequest with the modified parameters
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .state(redirectUrl) // Set the custom state
                .build();
    }
}

