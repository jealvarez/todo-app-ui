package org.jealvarez.todoapp.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.jealvarez.todoapp.ui.api.client")
public class Application {

    public static void main(final String... arguments) {
        SpringApplication.run(Application.class, arguments);
    }

}
