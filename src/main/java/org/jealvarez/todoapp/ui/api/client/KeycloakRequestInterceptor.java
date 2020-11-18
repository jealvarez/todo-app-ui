package org.jealvarez.todoapp.ui.api.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class KeycloakRequestInterceptor implements RequestInterceptor {

    private final KeycloakSecurityContext keycloakSecurityContext;

    KeycloakRequestInterceptor(final KeycloakSecurityContext keycloakSecurityContext) {
        this.keycloakSecurityContext = keycloakSecurityContext;
    }

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        ensureTokenIsStillValid();

        // We use the Access-Token of the current user to call the service
        // Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlWE...
        requestTemplate.header(AUTHORIZATION, "Bearer " + keycloakSecurityContext.getTokenString());
    }

    private void ensureTokenIsStillValid() {
        if (keycloakSecurityContext instanceof RefreshableKeycloakSecurityContext) {
            ((RefreshableKeycloakSecurityContext) keycloakSecurityContext).refreshExpiredToken(true);
        }
    }

}
