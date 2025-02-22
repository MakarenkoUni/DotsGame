package org.codefinity.UI;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.codefinity.game.Dot;
import org.codefinity.game.GameEngine;
import org.codefinity.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class GameUI {
    private final Stage stage;
    private final String playerName;
    private final PlayerController playerController;
    private final GameEngine game;
    private final Label scoreLabel;
    private final DotsGameUI mainMenuUI;
    private List<Dot> selectedDots = new ArrayList<>();
    private Group gameGroup;
    private final Label errorLabel = new Label();

    public GameUI(Stage stage, String playerName, PlayerController playerController, GameEngine game, DotsGameUI mainMenuUI) {
        this.stage = stage;
        this.playerName = playerName;
        this.playerController = playerController;
        this.game = game;
        this.mainMenuUI = mainMenuUI;

        scoreLabel = new Label("Score: 0");

        setupUI();
    }

    private void setupUI() {
        Label nameLabel = new Label("Player: " + playerName);

        gameGroup = new Group();
        updateGrid();

        Label controlsLabel = new Label("Controls:\n🖱️ Click on dots to select them\n⏎ Click on Finish Move to submit move\n⎋ X to clear selection\n");
        controlsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        controlsLabel.setTextFill(Color.BLACK);

        Button finishMoveButton = new Button("Finish Move");
        finishMoveButton.setOnAction(e -> finishMove());

        Button exitButton = new Button("Exit to Main Menu");
        exitButton.setOnAction(e -> exitToMainMenu());

        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox topContainer = new VBox(nameLabel, scoreLabel, errorLabel);
        topContainer.setAlignment(Pos.TOP_CENTER);
        topContainer.setSpacing(10);

        HBox bottomButtons = new HBox(10, finishMoveButton, exitButton);
        bottomButtons.setAlignment(Pos.CENTER);

        VBox bottomContainer = new VBox(controlsLabel, bottomButtons);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setSpacing(10);

        BorderPane root = new BorderPane();
        root.setTop(topContainer);
        root.setCenter(gameGroup);
        root.setBottom(bottomContainer);
        BorderPane.setAlignment(bottomContainer, Pos.BOTTOM_CENTER);

        Scene gameScene = new Scene(root);

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.X) {
                cancelSelection();
            }
        });

        stage.setScene(gameScene);
        stage.setFullScreen(true);
        stage.show();

        // ✅ Resize grid only when window resizes
        stage.widthProperty().addListener((obs, oldVal, newVal) -> updateGrid());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> updateGrid());
    }

    private void updateGrid() {
        gameGroup.getChildren().clear();
        int size = game.grid.getSize();
        double screenWidth = stage.getWidth();
        double screenHeight = stage.getHeight();
        double spacing = Math.min(screenWidth, screenHeight) / (size + 2);
        double buttonSize = spacing * 0.8;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Dot dot = game.grid.getDot(x, y);
                final int finalX = x;
                final int finalY = y;

                Button dotButton = new Button();
                dotButton.setMinSize(buttonSize, buttonSize);
                dotButton.setMaxSize(buttonSize, buttonSize);
                dotButton.setShape(new Circle(buttonSize / 2));
                dotButton.setStyle("-fx-background-color: " + getColor(dot.getColor()) + ";");

                dotButton.setLayoutX((x + 1) * spacing);
                dotButton.setLayoutY((y + 1) * spacing);

                dotButton.setOnAction(e -> handleDotClick(finalX, finalY));

                gameGroup.getChildren().add(dotButton);
            }
        }
    }

    private void handleDotClick(int x, int y) {
        Dot selectedDot = game.grid.getDot(x, y);
        if (selectedDot == null) return;

        if (selectedDots.isEmpty()) {
            game.clearSelection();
            game.selectDot(x, y);
            selectedDots.add(selectedDot);
        } else {
            Dot lastDot = selectedDots.get(selectedDots.size() - 1);

            if (!game.isANeighbour(x, y)) {
                errorLabel.setText("❌ Dots must be adjacent!");
            } else if (!lastDot.getColor().equals(selectedDot.getColor())) {
                errorLabel.setText("❌ Dots must be the same color!");
            } else {
                game.selectDot(x, y);
                selectedDots.add(selectedDot);
                drawConnection(lastDot, selectedDot);
                errorLabel.setText("");
            }
        }
    }

    private void drawConnection(Dot from, Dot to) {
        double spacing = Math.min(stage.getWidth(), stage.getHeight()) / (game.grid.getSize() + 2);
        double radius = spacing * 0.4; // Half of dot size

        double startX = (from.getX() + 1) * spacing + radius;
        double startY = (from.getY() + 1) * spacing + radius;
        double endX = (to.getX() + 1) * spacing + radius;
        double endY = (to.getY() + 1) * spacing + radius;

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.web(getColor(from.getColor()))); // Line matches dot color
        line.setStrokeWidth(5);

        gameGroup.getChildren().add(line);
    }


    private void finishMove() {
        if (selectedDots.size() > 1) {
            game.finishMove();
            scoreLabel.setText("Score: " + game.getPoints());
            selectedDots.clear();
            updateGrid();
        }
    }

    private void cancelSelection() {
        selectedDots.clear(); // ✅ Clear UI selection
        game.clearSelection(); // ✅ Also clear backend selection to reset properly
        updateGrid(); // ✅ Keep UI updated
        System.out.println("❌ Selection fully cleared.");
    }

    private void exitToMainMenu() {
        playerController.updateMaxScore(playerName, game.getPoints());
        Scene mainScene = mainMenuUI.createMainMenuScene();
        DotsGameUI.switchScene(mainScene);
    }


    private String getColor(String color) {
        return switch (color.toUpperCase()) {
            case "RED" -> "red";
            case "BLUE" -> "blue";
            case "GREEN" -> "green";
            case "YELLOW" -> "yellow";
            case "PURPLE" -> "purple";
            default -> "gray";
        };
    }
}

