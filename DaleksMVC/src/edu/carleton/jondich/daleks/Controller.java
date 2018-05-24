package edu.carleton.jondich.daleks;

import javafx.fxml.FXML;

public class Controller {
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
}
