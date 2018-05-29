/*
 * MooseController.java
 * ThreeScreens project
 * Jeff Ondich, 28 May 2018
 *
 * You may be amazed to learn that this is the controller for the moose screen.
 */
package edu.carleton.jondich.threescreens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MooseController {
    @FXML BorderPane mooseRootPane;

    public MooseController() {
    }

    public void initialize() {
    }

    public void onGoatButton(ActionEvent actionEvent) {
        try {
            Parent newRootNode = FXMLLoader.load(getClass().getResource("goat-screen.fxml"));
            Scene scene = this.mooseRootPane.getScene();
            scene.setRoot(newRootNode);
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
}
