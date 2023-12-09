package com.example.demo19;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StickmanGame extends Application {

    private static final double PLATFORM_HEIGHT = 200;
    private static double platform_distance;

    AnchorPane root;
    StickmanController stickman;
    PlatformController platform;
    private Timeline growingTimeline;

    int score = 0;
    int cherrycounter=0;


    Label scorelabel;
    Label cherrylabel;
    Label GameEnd;
    Label StickManRunner;

    boolean stickSpawned = false;
    boolean isWalking = false;

    boolean growingInProgress;

    Scene scene;

    Scene SCENE3;

    AnchorPane root2;

    Button revive;
    Button mainmenu;
    Button newg;
    Stage stage;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root2 = new AnchorPane();
        revive = new Button("Revive");
        mainmenu = new Button("Mainmenu");
        newg = new Button("New Game");
        GameEnd=new Label("GAME ENDED");
        StickManRunner=new Label("STICKMAN RUN ");

        // Set font size and font style
        Font buttonFont = Font.font("Arial", FontWeight.BOLD, 25);
        revive.setFont(buttonFont);
        newg.setFont(buttonFont);
        mainmenu.setFont(buttonFont);
        GameEnd.setFont(Font.font("Berlin Sans FB", 100));
        GameEnd.setLayoutY(25);
        GameEnd.setLayoutX(120);

        revive.setPrefSize(150, 50);
        newg.setPrefSize(200, 50);
        mainmenu.setPrefSize(200, 50);
        Image backgroundimage = new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\photu.png");
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage bi = new BackgroundImage(backgroundimage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        Background background = new Background(bi);
        root2.getChildren().add(revive);
        root2.getChildren().add(newg);
        root2.getChildren().add(mainmenu);
        root2.getChildren().add(GameEnd);
        root2.setBackground(background);
        SCENE3 = new Scene(root2, 800, 600);
        revive.translateXProperty().bind(SCENE3.widthProperty().divide(2).subtract(revive.widthProperty().divide(2)));
        revive.translateYProperty().bind(SCENE3.heightProperty().divide(2).subtract(revive.heightProperty().divide(2)));
        newg.translateXProperty().bind(SCENE3.widthProperty().divide(2).subtract(newg.widthProperty().divide(2)));
        newg.translateYProperty().bind(SCENE3.heightProperty().divide(2).add(newg.heightProperty()));
        mainmenu.translateXProperty().bind(SCENE3.widthProperty().divide(2).subtract(mainmenu.widthProperty().divide(2)));
        mainmenu.translateYProperty().bind(SCENE3.widthProperty().divide(2).add(mainmenu.widthProperty().divide(2).multiply(0.75)));

        root = new AnchorPane();
        root.setPrefSize(800, 600);
        root.setBackground(background);
        scene = new Scene(root, 800, 600);
        Image icon = new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\561_longer-removebg-preview.png");
        stage.getIcons().add(icon);
        scene.getRoot().requestFocus();
        scorelabel = new Label("Score: 0");
        scorelabel.setFont(Font.font("Berlin Sans FB", 28));
        scorelabel.setLayoutY(20);
        scorelabel.setLayoutX(root.getWidth() - 150);
        root.getChildren().add(scorelabel);
        cherrylabel = new Label("Cherries: 0");
        cherrylabel.setFont(Font.font("Berlin Sans FB", 28));
        cherrylabel.setLayoutY(60);
        cherrylabel.setLayoutX(root.getWidth() - 150);
        root.getChildren().add(cherrylabel);

        createPlatform();
        createStickman();
        stickman.setStage(primaryStage);

        StickManRunner.setFont(Font.font("Berlin Sans FB", 100));
        StickManRunner.setLayoutY(25);
        StickManRunner.setLayoutX(70);

        Button button = new Button("NEW GAME");
        button.setFont(Font.font("Berlin Sans FB", 28));

        Button howToPlayButton = new Button("PLAYER STATS");
        howToPlayButton.setFont(Font.font("Berlin Sans FB", 28));

        Button playerStatsButton = new Button("HOW TO PLAY?");
        playerStatsButton.setFont(Font.font("Berlin Sans FB", 28));

        VBox buttonContainer = new VBox(50);
        buttonContainer.getChildren().addAll(button, howToPlayButton, playerStatsButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(20);

        root.getChildren().add(buttonContainer);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.setOnMouseEntered(event -> scaleTransition.playFromStart());
        button.setOnMouseExited(event -> scaleTransition.setToX(1.0));
        Pane root1 = new Pane();
        root1.setBackground(background);
        Scene SCENE1 = new Scene(root1, 800, 600);
        button.translateXProperty().bind(scene.widthProperty().divide(2).subtract(button.widthProperty().divide(2)));
        button.translateYProperty().bind(scene.heightProperty().divide(2).subtract(button.heightProperty().divide(2)));
        root1.getChildren().add(button);
        root1.getChildren().add(StickManRunner);

        howToPlayButton.translateXProperty().bind(scene.widthProperty().divide(2).subtract(howToPlayButton.widthProperty().divide(2)));
        howToPlayButton.translateYProperty().bind(scene.heightProperty().divide(2).add(button.heightProperty().divide(2)).add(20));
        root1.getChildren().add(howToPlayButton);

        playerStatsButton.translateXProperty().bind(scene.widthProperty().divide(2).subtract(playerStatsButton.widthProperty().divide(2)));
        playerStatsButton.translateYProperty().bind(scene.heightProperty().divide(2).add(button.heightProperty().divide(2)).add(100));
        root1.getChildren().add(playerStatsButton);

        button.setOnAction(e -> {
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            root.requestFocus();
            startGameLoop();
        });

        primaryStage.setTitle("StickmanGame");
        primaryStage.setScene(SCENE1);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
  
