package edu.carleton.jondich.daleks;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DaleksView extends Group {
    public final static double CELL_WIDTH = 20.0;

    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
    private Image dalekImage;

    public DaleksView() {
        this.dalekImage = new Image(getClass().getResourceAsStream("/res/dalek.png"));
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }

    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    public void update(DaleksModel model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                DaleksModel.CellValue cellValue = model.getCellValue(row, column);
                if (cellValue == DaleksModel.CellValue.DALEK) {
                    this.cellViews[row][column].setImage(this.dalekImage);
                } else if (cellValue == DaleksModel.CellValue.SCRAPHEAP) {
                    Image image = new Image(getClass().getResourceAsStream("/res/scrapheap.png"));
                    this.cellViews[row][column].setImage(image);
                } else if (cellValue == DaleksModel.CellValue.RUNNER) {
                    Image image = new Image(getClass().getResourceAsStream("/res/runner.png"));
                    this.cellViews[row][column].setImage(image);
                } else {
                    this.cellViews[row][column].setImage(null);
                }
            }
        }
    }
}
