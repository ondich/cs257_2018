package edu.carleton.jondich.daleks;

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
        double numerator = Math.pow(Math.E, (double)level);
        double denominator = numerator + 1.0;
        double dalekDensity = 0.4 * numerator / denominator;

        int numberOfDaleks = (int)Math.round(dalekDensity * (double)cellCount);
        if (numberOfDaleks < 6) {
            numberOfDaleks = 6;
        }

        return numberOfDaleks;
    }
}
