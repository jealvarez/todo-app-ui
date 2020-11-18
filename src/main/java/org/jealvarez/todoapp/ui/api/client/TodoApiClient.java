package org.jealvarez.todoapp.ui.api.client;

import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.jealvarez.todoapp.ui.domain.Task;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@FeignClient(name = "todo-app-api",
             url = "${todo-app-api}",
             configuration = TodoApiClient.Configuration.class)
public interface TodoApiClient {

    @GetMapping(value = "/tasks", consumes = APPLICATION_JSON_VALUE)
    List<Task> getTasks();

    @GetMapping(value = "/tasks/{id}", consumes = APPLICATION_JSON_VALUE)
    Task getTaskById(@PathVariable("id") long id);

    @PostMapping(value = "/tasks", consumes = APPLICATION_JSON_VALUE)
    Task createTask(Task task);

    @PutMapping(value = "/tasks", consumes = APPLICATION_JSON_VALUE)
    Task updateTask(Task task);

    @DeleteMapping(value = "/tasks/{id}", consumes = APPLICATION_JSON_VALUE)
    void deleteTask(@PathVariable("id") long id);

    class Configuration {

        @Bean
        RequestInterceptor keycloakRequestInterceptor(final KeycloakSecurityContext keycloakSecurityContext) {
            return new KeycloakRequestInterceptor(keycloakSecurityContext);
        }

        @Bean
        Encoder feignFormEncoder(final ObjectFactory<HttpMessageConverters> httpMessageConverters) {
            return new SpringFormEncoder(new SpringEncoder(httpMessageConverters));
        }

    }

}
