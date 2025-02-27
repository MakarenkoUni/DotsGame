package org.codefinity;

import javafx.application.Application;
import org.codefinity.UI.DotsGameUI;
import org.codefinity.UI.SpringBootJavaFXApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Start Spring Boot first
        context = SpringApplication.run(Main.class, args);

        // Then launch JavaFX
        Application.launch(DotsGameUI.class, args);

        // Ensure Spring shuts down when JavaFX exits
        context.close();
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
