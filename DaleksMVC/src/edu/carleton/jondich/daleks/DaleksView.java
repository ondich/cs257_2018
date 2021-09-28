package edu.carleton.jondich.daleks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DaleksView extends Group {
    public final static double CELL_WIDTH = 20.0;

    @FXML private int rowCount;
    @FXML private int columnCount;
    private Rectangle[][] cellViews;
//    private ImageView[][] cellViews;
    private Image emptyImage;
    private Image dalekImage;
    private Image scrapHeapImage;
    private Image runnerImage;

    public DaleksView() {
        this.emptyImage = new Image(getClass().getResourceAsStream("/res/empty.png"));
        this.dalekImage = new Image(getClass().getResourceAsStream("/res/dalek.png"));
        this.scrapHeapImage = new Image(getClass().getResourceAsStream("/res/scrapheap.png"));
        this.runnerImage = new Image(getClass().getResourceAsStream("/res/runner.png"));

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 1. Get our hands on a reference to the Controller
                // 2. Figure out the row and column of the click
                // 3. Call controller.cellGotClicked(row, column)
                int r = (int)event.getY() / (int)CELL_WIDTH;
                int c = (int)event.getX();
                c = c % (int)CELL_WIDTH;
                System.err.println(String.format("(x, y) = (%.1f, %.1f); (r, c) = (%d, %d)", event.getX(), event.getY(), r, c));
            }
        });
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
            this.cellViews = new Rectangle[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX((double)column * CELL_WIDTH);
                    rectangle.setY((double)row * CELL_WIDTH);
                    rectangle.setWidth(CELL_WIDTH);
                    rectangle.setHeight(CELL_WIDTH);
                    this.cellViews[row][column] = rectangle;
                    this.getChildren().add(rectangle);
//                    ImageView imageView = new ImageView();
//                    imageView.setX((double)column * CELL_WIDTH);
//                    imageView.setY((double)row * CELL_WIDTH);
//                    imageView.setFitWidth(CELL_WIDTH);
//                    imageView.setFitHeight(CELL_WIDTH);
//                    this.cellViews[row][column] = imageView;
//                    this.getChildren().add(imageView);
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
                    this.cellViews[row][column].setFill(Color.RED);
                } else if (cellValue == DaleksModel.CellValue.SCRAPHEAP) {
                    this.cellViews[row][column].setFill(Color.BLACK);
                } else if (cellValue == DaleksModel.CellValue.RUNNER) {
                    this.cellViews[row][column].setFill(Color.GREEN);
                } else {
                    this.cellViews[row][column].setFill(Color.WHITE);
                }
//                if (cellValue == DaleksModel.CellValue.DALEK) {
//                    this.cellViews[row][column].setImage(this.dalekImage);
//                } else if (cellValue == DaleksModel.CellValue.SCRAPHEAP) {
//                    this.cellViews[row][column].setImage(this.scrapHeapImage);
//                } else if (cellValue == DaleksModel.CellValue.RUNNER) {
//                    this.cellViews[row][column].setImage(this.runnerImage);
//                } else {
//                    this.cellViews[row][column].setImage(this.emptyImage);
//                }
            }
        }
    }
}
