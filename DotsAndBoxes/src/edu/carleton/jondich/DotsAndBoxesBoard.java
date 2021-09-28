package edu.carleton.jondich;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DotsAndBoxesBoard extends Group {

    private final static double DOT_RADIUS = 5;
    private final static double BOX_WIDTH = 30;
    private final static Color DOT_COLOR = Color.gray(0.0);

    public final static int LEFT_WALL = 1;
    public final static int TOP_WALL = 2;
    public final static int RIGHT_WALL = 4;
    public final static int BOTTOM_WALL = 8;

    private class Box {
        private String owner;
        private int walls;

        Box() {
            this.walls = 0;
            this.owner = "";
        }

        public boolean hasWall(int wall) {
            assert wall == LEFT_WALL || wall == TOP_WALL || wall == RIGHT_WALL || wall == BOTTOM_WALL;
            return (walls & wall) != 0;
        }

        public void addWall(int wall) {
            assert wall == LEFT_WALL || wall == TOP_WALL || wall == RIGHT_WALL || wall == BOTTOM_WALL;
            this.walls = this.walls | wall;
        }

        public void deleteWall(int wall) {
            assert wall == LEFT_WALL || wall == TOP_WALL || wall == RIGHT_WALL || wall == BOTTOM_WALL;
            this.walls = this.walls & ~wall;
        }
    }

    private Box[][] boxes;
    private Line[][] verticalWalls;
    private Line[][] horizontalWalls;
    @FXML private int rowCount;
    @FXML private int columnCount;

    public DotsAndBoxesBoard() {
    }

    public DotsAndBoxesBoard(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.buildBoard();
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.buildBoard();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.buildBoard();
    }

    private void buildBoard() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.boxes = new Box[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    this.boxes[row][column] = new Box();
                }
            }

            this.verticalWalls = new Line[this.rowCount][this.columnCount + 1];
            this.horizontalWalls = new Line[this.rowCount + 1][this.columnCount];

            this.getChildren().clear();
            for (int row = 0; row <= this.rowCount; row++) {
                for (int column = 0; column <= this.columnCount; column++) {
                    Circle circle = new Circle();
                    circle.setCenterX(column * BOX_WIDTH);
                    circle.setCenterY(row * BOX_WIDTH);
                    circle.setRadius(DOT_RADIUS);
                    circle.setFill(DOT_COLOR);
                    this.getChildren().add(circle);
                }
            }
        }
    }

    public void addWall(int row, int column, int wall) {
        assert row >= 0 && row < this.rowCount;
        assert column >= 0 && column < this.columnCount;

        Line line;
        this.boxes[row][column].addWall(wall);
        switch (wall) {
            case LEFT_WALL:
                line = new Line(column * BOX_WIDTH, row * BOX_WIDTH, column * BOX_WIDTH, (row + 1) * BOX_WIDTH);
                this.verticalWalls[row][column] = line;
                this.getChildren().add(line);
                if (column > 0) {
                    this.boxes[row][column - 1].addWall(RIGHT_WALL);
                }
                break;

            case TOP_WALL:
                line = new Line(column * BOX_WIDTH, row * BOX_WIDTH, (column + 1) * BOX_WIDTH, row * BOX_WIDTH);
                this.horizontalWalls[row][column] = line;
                this.getChildren().add(line);
                if (row > 0) {
                    this.boxes[row - 1][column].addWall(BOTTOM_WALL);
                }
                break;

            case RIGHT_WALL:
                line = new Line((column + 1) * BOX_WIDTH, row * BOX_WIDTH, column * BOX_WIDTH, (row + 1) * BOX_WIDTH);
                this.verticalWalls[row][column] = line;
                this.getChildren().add(line);
                if (column > 0) {
                    this.boxes[row][column - 1].addWall(RIGHT_WALL);
                }
                break;

            case BOTTOM_WALL:
                line = new Line(column * BOX_WIDTH, row * BOX_WIDTH, column * BOX_WIDTH, (row + 1) * BOX_WIDTH);
                this.verticalWalls[row][column] = line;
                this.getChildren().add(line);
                if (column > 0) {
                    this.boxes[row][column - 1].addWall(RIGHT_WALL);
                }
                break;

            default:
                break;
        }
        if (wall == LEFT_WALL && column > 0) {
            this.boxes[row][column - 1].addWall(RIGHT_WALL);
        } else if (wall == TOP_WALL && row > 0) {
            this.boxes[row - 1][column].addWall(BOTTOM_WALL);
        } else if (wall == RIGHT_WALL && column + 1 < this.columnCount) {
            this.boxes[row][column + 1].addWall(LEFT_WALL);
        } else if (wall == BOTTOM_WALL && row + 1 < this.rowCount) {
            this.boxes[row + 1][column].addWall(TOP_WALL);
        }
    }

    public void deleteWall(int row, int column, int wall) {
        assert row >= 0 && row < this.rowCount;
        assert column >= 0 && column < this.columnCount;
        this.boxes[row][column].deleteWall(wall);
        if (wall == LEFT_WALL && column > 0) {
            this.boxes[row][column - 1].deleteWall(RIGHT_WALL);
        } else if (wall == TOP_WALL && row > 0) {
            this.boxes[row - 1][column].deleteWall(BOTTOM_WALL);
        } else if (wall == RIGHT_WALL && column + 1 < this.columnCount) {
            this.boxes[row][column + 1].deleteWall(LEFT_WALL);
        } else if (wall == BOTTOM_WALL && row + 1 < this.rowCount) {
            this.boxes[row + 1][column].deleteWall(TOP_WALL);
        }
    }
}
