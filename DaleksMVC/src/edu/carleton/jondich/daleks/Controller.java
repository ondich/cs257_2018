package edu.carleton.jondich.daleks;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements EventHandler<KeyEvent> {
    @FXML private DaleksView daleksView;
    private DaleksModel daleksModel;

    public Controller() {
    }

    public void initialize() {
        this.daleksModel = new DaleksModel(this.daleksView.getRowCount(), this.daleksView.getColumnCount());
        this.daleksView.update(this.daleksModel);
    }

    public double getBoardWidth() {
        return DaleksView.CELL_WIDTH * this.daleksView.getColumnCount();
    }

    public double getBoardHeight() {
        return DaleksView.CELL_WIDTH * this.daleksView.getRowCount();
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            this.daleksModel.moveHeroBy(0, -1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            this.daleksModel.moveHeroBy(0, 1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            this.daleksModel.moveHeroBy(-1, 0);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.DOWN || code == KeyCode.X) {
            this.daleksModel.moveHeroBy(1, 0);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.Q) {
            this.daleksModel.moveHeroBy(-1, -1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.E) {
            this.daleksModel.moveHeroBy(-1, 1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.Z) {
            this.daleksModel.moveHeroBy(1, -1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.C) {
            this.daleksModel.moveHeroBy(1, 1);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.S) {
            this.daleksModel.moveHeroBy(0, 0);
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        } else if (code == KeyCode.T) {
            this.daleksModel.teleportHero();
            this.daleksView.update(this.daleksModel);
            keyEvent.consume();
        }
    }

}
