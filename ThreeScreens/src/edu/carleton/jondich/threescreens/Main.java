/**
 * ThreeScreens project
 * Jeff Ondich, 28 May 2018
 *
 * This project illustrates one approach to switching between distinct FXML-based screens
 * within a single window/Stage. Each FXML file in this project represents a screen intended
 * to be used as the root Node of the Stage's Scene. Each FXML file also works with a distinct
 * controller. You could let screens share a controller to reduce duplicated code. You might
 * test your understanding of this program's technique by replacing the three controllers with
 * one controller.
 */
package edu.carleton.jondich.threescreens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Three Screens");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("moose-screen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1024, 768);
        MooseController mooseController = loader.getController();
        scene.setOnKeyPressed(mooseController);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
