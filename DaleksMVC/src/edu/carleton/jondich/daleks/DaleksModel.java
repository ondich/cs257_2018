package edu.carleton.jondich.daleks;

import javafx.scene.control.Cell;

import java.util.Random;

public class DaleksModel {

    public enum CellValue {
        EMPTY, HERO, DALEK, SCRAPHEAP
    };

    private CellValue[][] cells;
    private int score;
    private int level;
    private int heroRow;
    private int heroColumn;

    public DaleksModel(int rowCount, int columnCount) {
        assert rowCount > 0 && columnCount > 0;
        this.cells = new CellValue[rowCount][columnCount];
        this.level = 1;
        this.initializeLevel();
    }

    private void initializeLevel() {
        int rowCount = this.cells.length;
        int columnCount = this.cells[0].length;

        // Empty all the cells
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.cells[row][column] = CellValue.EMPTY;
            }
        }

        // Place the hero
        Random random = new Random();
        this.heroRow = random.nextInt(rowCount);
        this.heroColumn = random.nextInt(columnCount);
        this.cells[this.heroRow][this.heroColumn] = CellValue.HERO;

        // Place the daleks
        int dalekCount = this.numberOfDaleksForLevel(this.level);
        for (int k = 0; k < dalekCount; k++) {
            int row = random.nextInt(rowCount);
            int column = random.nextInt(columnCount);
            if (this.cells[row][column] == CellValue.EMPTY) {
                this.cells[row][column] = CellValue.DALEK;
            }
        }
    }

    private int numberOfDaleksForLevel(int level) {
        int rowCount = this.cells.length;
        int columnCount = this.cells[0].length;
        int cellCount = rowCount * columnCount;

        // Using a sigmoid function shifted and scaled to gradually increase the density of daleks
        // up to an asymptotic limit.
        double numerator = Math.pow(Math.E, (double)level - 8.0);
        double denominator = numerator + 1.0;
        double dalekDensity = 0.2 * numerator / denominator;
        if (dalekDensity < 0.008) {
            dalekDensity = 0.008;
        }

        return (int)Math.round(dalekDensity * (double)cellCount);
    }

    public int getRowCount() {
        return this.cells.length;
    }

    public int getColumnCount() {
        assert this.cells.length > 0;
        return this.cells[0].length;
    }

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.cells.length && column >= 0 && column < this.cells[0].length;
        return this.cells[row][column];
    }

    public void moveHeroBy(int rowChange, int columnChange) {
        int newRow = this.heroRow + rowChange;
        if (newRow < 0) {
            newRow = 0;
        }
        if (newRow >= this.getRowCount()) {
            newRow = this.getRowCount() - 1;
        }

        int newColumn = this.heroColumn + columnChange;
        if (newColumn < 0) {
            newColumn = 0;
        }
        if (newColumn >= this.getColumnCount()) {
            newColumn = this.getColumnCount() - 1;
        }


        this.cells[this.heroRow][this.heroColumn] = CellValue.EMPTY;
        this.heroRow = newRow;
        this.heroColumn = newColumn;
        this.moveDaleksToFollowHero();
    }

    public void teleportHero() {
        int rowCount = this.cells.length;
        int columnCount = this.cells[0].length;
        Random random = new Random();
        int newRow = random.nextInt(rowCount);
        int newColumn = random.nextInt(columnCount);
        this.cells[this.heroRow][this.heroColumn] = CellValue.EMPTY;
        this.heroRow = newRow;
        this.heroColumn = newColumn;
        this.moveDaleksToFollowHero();
    }

    // Assumes that the hero has been removed from this.cells.
    private void moveDaleksToFollowHero() {
        // Initialize a new game board
        int rowCount = this.cells.length;
        int columnCount = this.cells[0].length;
        CellValue[][] newCells = new CellValue[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                newCells[row][column] = CellValue.EMPTY;
            }
        }

        // Move the daleks on the old game board to their new positions on
        // the new game board. If a collision occurs, adjust score, check for
        // game-over, check for level-complete, etc.
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                CellValue cellValue = this.cells[row][column];
                if (cellValue != CellValue.EMPTY) {
                    int newRow = row;
                    int newColumn = column;
                    if (cellValue == CellValue.DALEK){
                        if (newRow < this.heroRow) {
                            newRow++;
                        } else if (newRow > this.heroRow) {
                            newRow--;
                        }

                        if (newColumn < this.heroColumn) {
                            newColumn++;
                        } else if (newColumn > this.heroColumn) {
                            newColumn--;
                        }
                    }

                    if (newCells[newRow][newColumn] == CellValue.EMPTY) {
                        newCells[newRow][newColumn] = cellValue;
                    } else {
                        newCells[newRow][newColumn] = CellValue.SCRAPHEAP;
                    }
                }
            }
        }

        if (newCells[this.heroRow][this.heroColumn] == CellValue.EMPTY) {
            newCells[this.heroRow][this.heroColumn] = CellValue.HERO;
        } else {
            newCells[this.heroRow][this.heroColumn] = CellValue.SCRAPHEAP;
        }

        this.cells = newCells;
    }
}
