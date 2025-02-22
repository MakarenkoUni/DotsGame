package org.codefinity.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.codefinity.Main;
import org.codefinity.PlayerController;
import org.codefinity.Leaderboard;
import org.codefinity.PlayerRepository;
import org.codefinity.game.GameEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

public class DotsGameUI extends Application {
    private final PlayerRepository playerRepository;

    public DotsGameUI() {
        this.playerRepository = SpringContext.getBean(PlayerRepository.class);
    }
    private static Stage primaryStage;
    private PlayerController playerController;
    private Leaderboard leaderboard;
    private Label messageLabel = new Label();
    private VBox loginBox;
    private VBox registerBox;
    private TextField nameField;
    private PasswordField passwordField;
    private static ApplicationContext context;

    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Dots Game");

        context = Main.getContext();
        if (context == null) {
            throw new IllegalStateException("Spring context is null! JavaFX cannot proceed.");
        }
        System.out.println("✅ JavaFX Successfully Started with Spring Context: " + context);

        playerController = context.getBean(PlayerController.class);
        leaderboard = context.getBean(Leaderboard.class);

        StackPane mainLayout = new StackPane();
        Scene mainScene = new Scene(mainLayout, 500, 500);

        VBox contentBox = createMainMenu();
        Button exitButton = createExitButton();

        mainLayout.getChildren().addAll(contentBox, exitButton);
        configureExitButton(exitButton);

        primaryStage.setFullScreen(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private VBox createMainMenu() {
        Text title = createTitle();
        Button loginButton = createStyledButton("Login", Color.DARKBLUE, Color.WHITE);
        Button registerButton = createStyledButton("Register", Color.DARKBLUE, Color.WHITE);
        Button leaderboardButton = createStyledButton("Leaderboard", Color.DARKBLUE, Color.WHITE);

        loginBox = createLoginBox();
        registerBox = createRegisterBox();

        configureButtons(loginButton, registerButton, leaderboardButton, loginBox, registerBox);

        VBox menuBox = new VBox(15, title, loginButton, loginBox, registerButton, registerBox, leaderboardButton, messageLabel);
        menuBox.setAlignment(Pos.CENTER);
        return menuBox;
    }

    private void configureButtons(Button loginButton, Button registerButton, Button leaderboardButton, VBox loginBox, VBox registerBox) {
        loginButton.setOnAction(e -> toggleVisibility(loginBox, registerBox));
        registerButton.setOnAction(e -> toggleVisibility(registerBox, loginBox));
        leaderboardButton.setOnAction(e -> showLeaderboard());
    }

    private Text createTitle() {
        Text title = new Text("Welcome to Dots Game");
        title.setFont(Font.font("Comic Sans MS", 40));
        title.setFill(Color.DARKRED);
        return title;
    }

    private VBox createLoginBox() {
        TextField loginUsername = new TextField();
        loginUsername.setPromptText("Enter Username");
        PasswordField loginPassword = new PasswordField();
        loginPassword.setPromptText("Enter Password");
        Button loginConfirm = createStyledButton("Confirm Login", Color.DARKBLUE, Color.WHITE);

        loginConfirm.setOnAction(e -> {
            System.out.println("🔹 Login Button Clicked");
            handleLogin(loginUsername.getText(), loginPassword.getText());
        });

        VBox loginBox = new VBox(10, loginUsername, loginPassword, loginConfirm);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setVisible(false);
        return loginBox;
    }

    private VBox createRegisterBox() {
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegister(nameField.getText(), passwordField.getText()));
        VBox vbox = new VBox(10, nameLabel, nameField, passwordLabel, passwordField, registerButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setVisible(false);
        return vbox;
    }


    private Button createExitButton() {
        Button exitButton = createStyledButton("Exit", Color.RED, Color.WHITE);
        exitButton.setOnAction(e -> primaryStage.close());
        return exitButton;
    }

    private void configureExitButton(Button exitButton) {
        StackPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(exitButton, new Insets(0, 20, 20, 0));
    }

    private void handleLogin(String username, String password) {
        if (playerController == null) {
            messageLabel.setText("System Error: Backend Not Connected");
            messageLabel.setTextFill(Color.RED);
            return;
        }
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Error: Fields cannot be empty");
            messageLabel.setTextFill(Color.RED);
            return;
        }
        String response = playerController.login(username, password);
        messageLabel.setText(response);
        messageLabel.setTextFill(response.startsWith("Error") ? Color.RED : Color.GREEN);

        if (!response.startsWith("Error")) {
            GameEngine gameEngine = new GameEngine();  // ✅ Create GameEngine instance
            GameUI gameUI = new GameUI(primaryStage, username, playerController, gameEngine, this);
        }
    }




    public void handleRegister(String name, String password) {
        if (name.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Error: Name and Password cannot be empty!");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        try {
            String result = playerController.registerPlayer(name, password);
            if (result.contains("already exists")) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(result);
            } else {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText(result);
            }

        } catch (Exception e) {
            messageLabel.setText("Error: Player with this name already exists!");
            messageLabel.setTextFill(Color.RED);
        }
    }

    private void showLeaderboard() {
        // Hide login & registration boxes
        toggleVisibility(new VBox(), loginBox);
        toggleVisibility(new VBox(), registerBox);

        // Create leaderboard display
        VBox leaderboardBox = new VBox(10);
        leaderboardBox.setAlignment(Pos.CENTER);
        leaderboardBox.setPadding(new Insets(20));
        leaderboardBox.setStyle("-fx-background-color: white;");

        Label leaderboardTitle = new Label("🏆 Leaderboard 🏆");
        leaderboardTitle.setFont(Font.font("Comic Sans MS", 30));
        leaderboardTitle.setTextFill(Color.DARKBLUE);

        VBox playerList = new VBox(5);
        playerList.setAlignment(Pos.CENTER);

        leaderboard.getTopPlayers().forEach(player -> {
            Label playerLabel = new Label(player.getName() + " - " + player.getMaxScore() + " pts");
            playerLabel.setFont(Font.font("Comic Sans MS", 20));
            playerLabel.setTextFill(Color.BLACK);
            playerList.getChildren().add(playerLabel);
        });

        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Comic Sans MS", 18));
        backButton.setOnAction(e -> {
            leaderboardBox.setVisible(false);
            loginBox.setVisible(false); // Restore login box if needed
            registerBox.setVisible(false); // Restore register box if needed
        });

        leaderboardBox.getChildren().addAll(leaderboardTitle, playerList, backButton);

        // Add the leaderboard to the main scene
        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        root.getChildren().add(leaderboardBox);
    }

    private Button createStyledButton(String text, Color bgColor, Color textColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Comic Sans MS", 20));
        button.setTextFill(textColor);
        button.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(5), Insets.EMPTY)));
        return button;
    }


    private void toggleVisibility(VBox toShow, VBox toHide) {
        toShow.setVisible(true);
        toHide.setVisible(false);
    }
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setTextFill(Color.RED); // Error messages in red
        } else {
            messageLabel.setTextFill(Color.GREEN); // Success messages in green
        }
    }
    public Scene createMainMenuScene() {
        StackPane mainLayout = new StackPane();
        VBox contentBox = createMainMenu();
        Button exitButton = createExitButton();

        mainLayout.getChildren().addAll(contentBox, exitButton);
        configureExitButton(exitButton);

        return new Scene(mainLayout, 500, 500);
    }
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }
}