package com.example.demo19;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;

public class PlatformController {

    public static final double PLATFORM_HEIGHT = 200;
    public static double platform_distance;
    public static ArrayList<Double> platforms = new ArrayList<Double>();

    private Pane root;
    Rectangle newplatform;
    Rectangle oldplatform;
    static int count;

    public PlatformController(Pane root) {
        this.root = root;
    }

    public void initialize() {
        Random rand = new Random();
        int random1 = -1;
        int random2 = -2;
        while (random1 > random2) {
            random1 = rand.nextInt(11, 30);
            random2 = rand.nextInt(random1 + 10, random1 + 30);
        }
        platforms.add((double) random1);
        platforms.add((double) random2);
        oldplatform = platform_creator(0, root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * (0.1), PLATFORM_HEIGHT, Color.web("#cbd1d7"));
        root.getChildren().add(oldplatform);
        newplatform = platform_creator(root.getWidth() * ((double) random1 / 100), root.getHeight() - PLATFORM_HEIGHT, root.getWidth() * (((double) random2 - (double) random1) / 100), PLATFORM_HEIGHT, Color.web("#cbd1d7"));
        root.getChildren().add(newplatform);
        platform_distance = root.getWidth() * ((platforms.get(0) / 100) - 0.1);
    }

    public void nextPlatform(StickmanGame stickmangame) {

        Duration duration = Duration.seconds(0.5);

        TranslateTransition transition = createTransition(duration, oldplatform, -1 * (platform_distance + oldplatform.getWidth()));
        TranslateTransition transition2 = createTransition(duration, newplatform, -1 * (platform_distance + newplatform.getWidth()));
        TranslateTransition transition3 = createTransition(duration, stickmangame.stickman.stick, -1 * (platform_distance + newplatform.getWidth()));
        TranslateTransition transition4 = createTransition(duration, stickmangame.stickman.stickman, -1 * (stickmangame.stickman.stickmanX - root.getWidth() * 0.1) - 60);

        transition.play();
        transition2.play();
        transition3.play();
        transition4.play();

        transition2.setOnFinished(e -> newPlatform(stickmangame));
    }

    private TranslateTransition createTransition(Duration duration, Node node, double byX) {
        TranslateTransition transition = new TranslateTransition(duration);
        transition.setByX(byX);
        transition.setNode(node);
        return transition;
    }


    private void newPlatform(StickmanGame stickmangame) {
        boolean isEvenCount = count % 2 == 0;

        Node removeNode = isEvenCount ? oldplatform : newplatform;
        root.getChildren().remove(removeNode);

        Random rand = new Random();
        int rand1 = -1;
        int rand2 = -2;

        while (rand1 > rand2) {
            rand1 = rand.nextInt(11, isEvenCount ? 30 : 100);
            rand2 = rand.nextInt(11, isEvenCount ? rand1 + 10 : rand1 + 20);
        }

        platforms.set(0, (double) rand1);
        platforms.set(1, (double) rand2);

        Node newNode = platform_creator(
                root.getWidth() * platforms.get(0) / 100,
                oldplatform.getY(),
                root.getWidth() * (platforms.get(1) - platforms.get(0)) / 100,
                oldplatform.getHeight(),
                Color.web("#cbd1d7"));

        if (isEvenCount) {
            oldplatform = (Rectangle) newNode;
        } else {
            newplatform = (Rectangle) newNode;
        }

        root.getChildren().add(newNode);

        this.count += 1;
        stickmangame.stickSpawned = false;
        stickmangame.isWalking = false;
        platform_distance = root.getWidth() * ((platforms.get(0) / 100) - 0.1);
        stickmangame.stickman.resetPosition(stickmangame.platform, stickmangame);
    }


    public double getDistance() {
        return platform_distance;
    }

    private Rectangle platform_creator(double x, double y, double width, double height, Color color) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(color);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);
        return rectangle;
    }
}
