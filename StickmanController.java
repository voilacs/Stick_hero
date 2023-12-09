package com.example.demo19;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

import static com.example.demo19.PlatformController.*;

public class StickmanController {

    private static final double STICKMAN_SIZE = 50;
    public boolean stickmanfall;

    private Pane root;
    ImageView stickman;
    Line stick;

    private boolean endgame=false;

    private double stickHeight = 0;
    double stickmanX = 0;
    private double stickmanY = 0;

    ImageView cherry;

    Random random;

    private boolean isWalking = false;

    private Timeline walkingTimeline;

    private Timeline fallingTimeline;

    private Timeline rotatingTimeline;

    boolean reverse=false;

    boolean isreversible=false;

    private boolean isrotating;

    private Stage stage;

    private int rotateangle=0;

    Rotate rotate;

    StickmanController stickmann;

    private double positionX=0,positionY=0;

    boolean cherr_iscollected=false;

    private static StickmanController sc=null;
    public static StickmanController getInstance(Pane root){
        if(sc==null){
            sc=new StickmanController(root);
        }
        return sc;
    }
    private StickmanController(Pane root) {
        this.root=root;
    }


    public void initialize() {
        Image stickmanImage = new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\561_longer-removebg-preview.png");
        stickman = new ImageView(stickmanImage);
        stickman.setFitHeight(STICKMAN_SIZE);
        stickman.setFitWidth(STICKMAN_SIZE);
        stickman.setLayoutY(root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE);
        root.getChildren().add(stickman);
        cherry=new ImageView();
        cherry.setImage(new Image("D:\\ap_project\\ap_project\\demo19\\src\\main\\resources\\com\\example\\demo19\\photu6.png"));
        cherry.setFitHeight(40);
        cherry.setFitWidth(40);
        cherry.setY(root.getHeight() - PLATFORM_HEIGHT);
        random=new Random();
    }
    public void grow() {
        if (stick == null) {
            initializeStick();
        }

        stickHeight += 1;
        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
    }

    public void initializeStick() {
        stick = new Line(root.getWidth() * 0.1, root.getHeight() - PLATFORM_HEIGHT,
                root.getWidth() * 0.1, root.getHeight() - PLATFORM_HEIGHT);
        stick.setStroke(Color.BLACK);
        stick.setStrokeWidth(5);
        root.getChildren().add(stick);
    }


    public void stopGrowing(PlatformController platform, StickmanGame stickmangame) {
        rotate = new Rotate();
        rotate.setPivotX(stickmanX + STICKMAN_SIZE / 2 + 55);
        rotate.setPivotY(root.getHeight() - PLATFORM_HEIGHT);
        stick.getTransforms().add(rotate);
        rotatingTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> rotatestick(platform,stickmangame)));
        rotatingTimeline.setCycleCount(Timeline.INDEFINITE);
        rotatingTimeline.play();

    }

    private void rotatestick(PlatformController platform, StickmanGame stickmangame) {
        rotateangle+=1;
        rotate.setAngle(rotateangle);
        if (rotateangle==90){
            rotatingTimeline.stop();
            isWalking = true;
            startWalking(platform, stickmangame);
        }
    }

    public void startWalking(PlatformController platform, StickmanGame stickmangame) {
        this.isreversible = true;

        if (walkingTimeline == null || !walkingTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            walkingTimeline = new Timeline(
                    new KeyFrame(Duration.millis(10), e -> walkStickman(platform, stickmangame))
            );
            walkingTimeline.setCycleCount(Timeline.INDEFINITE);
            walkingTimeline.play();

            setupKeyEvents(stickmangame);
        }
    }

    private void setupKeyEvents(StickmanGame stickmangame) {
        stickmangame.stickman.stickman.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && isreversible && isWalking && stickmanX > root.getWidth() * 0.1 - STICKMAN_SIZE / 2) {
                reverse = !reverse;

                if (reverse) {
                    stickman.setRotate(180);
                    stickman.setLayoutY(stickman.getLayoutY() + 60);
                } else {
                    stickman.setRotate(0);
                    stickman.setLayoutY(stickman.getLayoutY() - 60);
                }
            }
        });

        stickmangame.stickman.stickman.requestFocus();
    }


    private void walkStickman(PlatformController platform, StickmanGame stickmangame) {
        if (stickHeight<platform_distance+root.getWidth() * (((double) platforms.get(1) - (double) platforms.get(0))/ 100)){
            if (isWalking) {
                if (stickmanX > stickHeight + STICKMAN_SIZE && stickHeight < platform_distance) {
                    isWalking = false;
                    walkingTimeline.pause();
                    stickmanfall(platform, stickmangame);
                    isreversible = false;
                } else {
                    stickmanX += 1;
                    stickman.setTranslateX(stickmanX);
                    if (stickmanX - cherry.getX() >= -25 && stickmanX - cherry.getX() <= 20 && stickman.getLayoutY() > root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE) {
                        root.getChildren().remove(cherry);
                        cherr_iscollected = true;
                        stickmangame.cherrycounter++;
                    }
                    if (root.getWidth() * platforms.get(0) / 100 - stickmanX < STICKMAN_SIZE - 10) {
                        if (stickman.getRotate() == 180) {
                            walkingTimeline.stop();
                            stickmanfall(platform, stickmangame);
                        }
                    }
                    if (stickmanX >= (root.getWidth() * (platforms.get(0)) / 100)) {
                        isWalking = false;
                        isreversible = false;
                        walkingTimeline.pause();
                        stickmangame.score++;
                        stickmangame.scorelabel.setText("Score: " + stickmangame.score);
                        stickmangame.cherrycounter=stickmangame.cherrycounter/10;
                        stickmangame.cherrylabel.setText("Cherries: " + stickmangame.cherrycounter);

                        platform.nextPlatform(stickmangame);
                    }
                }
            }

        }
        else{
            if (isWalking) {
                if (stickmanX > stickHeight + STICKMAN_SIZE) {
                    isWalking = false;
                    walkingTimeline.pause();
                    stickmanfall(platform, stickmangame);
                    isreversible = false;
                } else {
                    stickmanX += 1;
                    stickman.setTranslateX(stickmanX);
                    if (stickmanX - cherry.getX() >= -20 && stickmanX - cherry.getX() <= 20 && stickman.getLayoutY() > root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE) {
                        root.getChildren().remove(cherry);
                        cherr_iscollected = true;
                    }
                    if (root.getWidth() * platforms.get(0) / 100 - stickmanX < STICKMAN_SIZE - 10) {
                        if (stickman.getRotate() == 180) {
                            walkingTimeline.stop();
                            stickmanfall(platform, stickmangame);
                        }
                    }

                }
            }
        }

    }

    private void stickmanfall(PlatformController platform, StickmanGame stickmanGame) {
        if (fallingTimeline == null || !fallingTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            fallingTimeline = new Timeline(
                    new KeyFrame(Duration.millis(2), e -> Stickmanfall(platform, stickmanGame))
            );
            fallingTimeline.setCycleCount(500);

            fallingTimeline.setOnFinished(e -> handleFallFinished(platform, stickmanGame));
            fallingTimeline.play();

        }
    }

    private void handleFallFinished(PlatformController platform, StickmanGame stickmanGame) {
        stickmanY = 0;
        resetPosition(platform, stickmanGame);
        stickmanGame.stickSpawned = false;
        reverse = false;
        stickman.setRotate(0);
        stickman.setTranslateY(-1 * 0.5);
        stickman.setTranslateX(0);
        stickman.setLayoutY(root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE);

        stickmanGame.stage.setScene(stickmanGame.SCENE3);

        stickmanGame.revive.setOnAction(e -> {
            stickmanGame.stage.setScene(stickmanGame.scene);
            stickmanGame.startGameLoop();
        });

        stickmanGame.newg.setOnAction(d -> {
            stickmanGame.root.getChildren().clear();
            platforms.clear();
            stickmanGame.stage.setScene(stickmanGame.scene);
            stickmanGame.createPlatform();
            stickmanGame.createStickman();
            stickmanGame.startGameLoop();
        });
    }



    private void Stickmanfall(PlatformController platform, StickmanGame stickmanGame) {
        stickmanY += 0.5;
        stickman.setTranslateY(stickmanY);
    }
    

    public void resetPosition(PlatformController platform, StickmanGame stickmanGame) {
        if (!cherr_iscollected) {
            root.getChildren().remove(cherry);
        }

        if (platform_distance > root.getWidth() * 0.1) {
            int temp = random.nextInt(5, 90);
            cherry.setX(root.getWidth() * 0.1 + (platform_distance * temp / 100));
            root.getChildren().add(cherry);
        }

        cherr_iscollected = false;
        rotateangle = 0;
        isWalking = false;
        stickHeight = 0;
        stickmanX = 0;
        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
        root.getChildren().remove(stick);

        stickmanGame.growingInProgress = false;
    }



    public Stage getStage() {
        return stage;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public boolean checkContinue() {
        return stick.getEndX() - stick.getStartX() < platform_distance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
