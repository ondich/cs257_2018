/*
 * GoatController.java
 * ThreeScreens project
 * Jeff Ondich, 28 May 2018
 *
 * You may be startled to learn that this is the controller for the goat screen.
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

public class GoatController implements EventHandler<KeyEvent> {
    @FXML BorderPane goatRootPane;

    public GoatController() {
    }

    public void initialize() {
    }

    public void onMooseButton(ActionEvent actionEvent) {
        try {
            Parent newRootNode = FXMLLoader.load(getClass().getResource("moose-screen.fxml"));
            Scene scene = this.goatRootPane.getScene();
            scene.setRoot(newRootNode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void onYakButton(ActionEvent actionEvent) {
        try {
            Parent newRootNode = FXMLLoader.load(getClass().getResource("yak-screen.fxml"));
            Scene scene = this.goatRootPane.getScene();
            scene.setRoot(newRootNode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void handle(KeyEvent event) {
        System.err.println("Goat KeyEvent");
    }
}
