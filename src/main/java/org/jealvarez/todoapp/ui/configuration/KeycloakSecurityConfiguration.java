package org.jealvarez.todoapp.ui.configuration;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

@Configuration
public class KeycloakSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    private static final String[] AUTH_ALLOWED_LIST = List.of(
            "/webjars/**",
            "/assets/**",
            "/"
    ).toArray(String[]::new);

    @Override
    protected void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        // @formatter:off
        httpSecurity
                .logout()
                    .addLogoutHandler(keycloakLogoutHandler())
                    .logoutUrl("/sso/logout").permitAll()
                    .logoutSuccessUrl("/")
                        .deleteCookies("KEYCLOAK_NETCORE_SESSION", "KEYCLOAK_NETCORE_SESSIONC1", "KEYCLOAK_NETCORE_SESSIONC2")
                    .and()
                .authorizeRequests()
                    .antMatchers(AUTH_ALLOWED_LIST).permitAll()
                    .antMatchers("/**").authenticated()
                .anyRequest().permitAll();
        // @formatter:on
    }


    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Bean
    @Scope(scopeName = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
    public KeycloakSecurityContext keycloakSecurityContext() {

        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Principal principal = Objects.requireNonNull(servletRequestAttributes).getRequest().getUserPrincipal();
        if (principal == null) {
            return null;
        }

        if (principal instanceof KeycloakAuthenticationToken) {
            principal = (Principal) ((KeycloakAuthenticationToken) principal).getPrincipal();
        }

        if (principal instanceof KeycloakPrincipal) {
            return ((KeycloakPrincipal) principal).getKeycloakSecurityContext();
        }

        return null;
    }

}
