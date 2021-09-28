package edu.carleton.jondich.daleks;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements EventHandler<KeyEvent> {
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;
    @FXML private DaleksView daleksView;
    private DaleksModel daleksModel;

    public Controller() {
    }

    public void initialize() {
        this.daleksModel = new DaleksModel(this.daleksView.getRowCount(), this.daleksView.getColumnCount());
        this.update();
    }

    public double getBoardWidth() {
        return DaleksView.CELL_WIDTH * this.daleksView.getColumnCount();
    }

    public double getBoardHeight() {
        return DaleksView.CELL_WIDTH * this.daleksView.getRowCount();
    }

    private void update() {
        this.daleksView.update(this.daleksModel);
        this.scoreLabel.setText(String.format("Score: %d", this.daleksModel.getScore()));
        if (this.daleksModel.isGameOver()) {
            this.messageLabel.setText("Game Over. Hit G to start a new game.");
        } else if (this.daleksModel.isLevelComplete()) {
            this.messageLabel.setText("Nice job! Hit L to start the next level.");
        } else {
            this.messageLabel.setText("Use the keys surrounding the S to run from the daleks.");
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();

        String s = code.getChar();
        if (s.length() > 0) {
            char theCharacterWeWant = s.charAt(0);
        }

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            this.daleksModel.moveRunnerBy(0, -1);
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            this.daleksModel.moveRunnerBy(0, 1);
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            this.daleksModel.moveRunnerBy(-1, 0);
        } else if (code == KeyCode.DOWN || code == KeyCode.X) {
            this.daleksModel.moveRunnerBy(1, 0);
        } else if (code == KeyCode.Q) {
            this.daleksModel.moveRunnerBy(-1, -1);
        } else if (code == KeyCode.E) {
            this.daleksModel.moveRunnerBy(-1, 1);
        } else if (code == KeyCode.Z) {
            this.daleksModel.moveRunnerBy(1, -1);
        } else if (code == KeyCode.C) {
            this.daleksModel.moveRunnerBy(1, 1);
        } else if (code == KeyCode.S) {
            this.daleksModel.moveRunnerBy(0, 0);
        } else if (code == KeyCode.T) {
            this.daleksModel.teleportRunner();
        } else if (code == KeyCode.G) {
            if (this.daleksModel.isGameOver()) {
                this.daleksModel.startNewGame();
            }
        } else if (code == KeyCode.L) {
            if (this.daleksModel.isLevelComplete()) {
                this.daleksModel.startNextLevel();
            }
        } else {
            keyRecognized = false;
        }

        if (keyRecognized) {
            this.update();
            keyEvent.consume();
        }
    }
}
