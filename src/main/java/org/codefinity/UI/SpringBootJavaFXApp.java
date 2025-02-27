package org.codefinity.UI;

import javafx.application.Application;
import javafx.stage.Stage;
import org.codefinity.Main;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBootJavaFXApp extends Application {
    private static ApplicationContext context;

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(Main.class);
    }

    @Override
    public void start(Stage primaryStage) {
        DotsGameUI ui = context.getBean(DotsGameUI.class);
        ui.setPrimaryStage(primaryStage); // Allow setting stage
        ui.start(primaryStage);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
