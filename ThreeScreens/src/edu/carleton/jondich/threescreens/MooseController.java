/*
 * MooseController.java
 * ThreeScreens project
 * Jeff Ondich, 28 May 2018
 *
 * You may be amazed to learn that this is the controller for the moose screen.
 */
package edu.carleton.jondich.threescreens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class MooseController implements EventHandler<KeyEvent> {
    @FXML BorderPane mooseRootPane;

    public MooseController() {
    }

    public void initialize() {
    }

    public void onGoatButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("goat-screen.fxml"));
            Parent newRootNode = loader.load();
            Scene scene = this.mooseRootPane.getScene();
            scene.setRoot(newRootNode);
            GoatController goatController = loader.getController();
            scene.setOnKeyPressed(goatController);
        } catch (Exception e) {
             System.err.println(e.getMessage());
        }
    }

    public void onYakButton(ActionEvent actionEvent) {
        try {
            Parent newRootNode = FXMLLoader.load(getClass().getResource("yak-screen.fxml"));
            Scene scene = this.mooseRootPane.getScene();
            scene.setRoot(newRootNode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void handle(KeyEvent event) {
        System.err.println("Moose KeyEvent");
    }
}
