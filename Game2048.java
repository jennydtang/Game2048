package com.codegym.games.game2048;

import java.util.Arrays;
import com.codegym.engine.cell.*;
import com.codegym.engine.cell.Game;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize() {
        // Here we perform all actions to initialize the game and its objects
        setScreenSize(4, 4);
        createGame();
        drawScene();
    }

    private void createGame() {
        gameField = new int[4][4];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) { // make loop
            for (int y = 0; y < SIDE; y++) {
                // x: horizontal; y: vertical, but in array of arrays, first number is vertical
                // (switch coordinates to y,i)
                setCellColoredNumber(y, i, gameField[i][y]);
            }
        }
        // setCellColor(0, 0, Color.BLUE);
        // setCellColor(0, 1, Color.BLUE);
        // setCellColor(0, 2, Color.BLUE);
        // setCellColor(0, 3, Color.BLUE);
        // setCellColor(1, 0, Color.BLUE);
        // setCellColor(1, 1, Color.BLUE);
        // setCellColor(1, 2, Color.BLUE);
        // setCellColor(1, 3, Color.BLUE);
        // setCellColor(2, 0, Color.BLUE);
        // setCellColor(2, 1, Color.BLUE);
        // setCellColor(2, 2, Color.BLUE);
        // setCellColor(2, 3, Color.BLUE);
        // setCellColor(3, 0, Color.BLUE);
        // setCellColor(3, 1, Color.BLUE);
        // setCellColor(3, 2, Color.BLUE);
        // setCellColor(3, 3, Color.BLUE);
    }

    private void createNewNumber() {
        int maxTileValue = getMaxTileValue();
        if (maxTileValue == 2048) {
            win();
        }

        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[y][x] != 0) {
            createNewNumber();
        } else {
            int i = getRandomNumber(10);
            int chance;
            if (i == 9) {
                chance = 4;
            } else {
                chance = 2;
                gameField[y][x] = chance;
            }
        }
    }

    private Color getColorByValue(int value) {
        // return cell colors based on value
        if (value == 0) {
            return Color.WHITE;
        } else if (value == 2) {
            return Color.BLUE;
        } else if (value == 4) {
            return Color.RED;
        } else if (value == 8) {
            return Color.GREEN;
        } else if (value == 16) {
            return Color.CYAN;
        } else if (value == 32) {
            return Color.ORANGE;
        } else if (value == 64) {
            return Color.GRAY;
        } else if (value == 128) {
            return Color.PURPLE;
        } else if (value == 256) {
            return Color.BLACK;
        } else if (value == 512) {
            return Color.PINK;
        } else if (value == 1024) {
            return Color.YELLOW;
        } else if (value == 2048) {
            return Color.AQUA;
        } else
            return Color.WHITE;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        // if value = 0, do not display the value
        // call getColorByValue method
        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    // implement left shift of non-zero items
    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowTemp = row.clone(); // copy of row to point to instead of using same row
        boolean hasMoved = false;
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length - i - 1; j++) {
                if (row[j] == 0) {
                    // affects rows passed in
                    temp = row[j];
                    row[j] = row[j + 1];
                    row[j + 1] = temp;
                }
            }
        }
        // Returns true if changes have been made
        if (!Arrays.equals(row, rowTemp))
            hasMoved = true;
        return hasMoved;
    }

    private boolean mergeRow(int[] row) {
        boolean hasMoved = false;
        for (int i = 0; i < row.length - 1; i++) {
            // merge adjacent non-zero items as you shift to left
            if (row[i] == row[i + 1] && row[i] != 0) {
                // score increases if merge occurs
                score += (row[i] + row[i + 1]);
                setScore(score);
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                hasMoved = true;
            }
        }
        return hasMoved;
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveLeft() {
        int move = 0;
        for (int i = 0; i < SIDE; i++) {
            boolean compressed = compressRow(gameField[i]); // var to call compressRow
            boolean merged = mergeRow(gameField[i]); // var to call mergeRow
            boolean compresses = compressRow(gameField[i]); // var to call compressRow
            // if one shift or merge takes places,move has been made
            if (compressed || merged || compresses)
                move++;
        }
        // add the new number to the array
        if (move != 0) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        for (int i = 0; i < SIDE / 2; i++) {
            for (int j = i; j < SIDE - i - 1; j++) {
                // swap elements per cycle
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;

            }
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            }
        } else if (canUserMove()) {
            if (key == Key.LEFT) {
                moveLeft();
            } else if (key == Key.RIGHT) {
                moveRight();
            } else if (key == Key.UP) {
                moveUp();
            } else if (key == Key.DOWN) {
                moveDown();
                // Make visible to screen by calling drawScene()
            } // if(key==Key.LEFT || key==Key.RIGHT||key==Key.UP||key==Key.DOWN){
            drawScene();
        } else {
            gameOver();
        }
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > max) {
                    max = gameField[i][j];
                }
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You Won", Color.GREEN, 50);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You Lost", Color.RED, 50);
    }

    private boolean canUserMove() {
        // check for zeros, then horizonal neighbors, then vertical neighbors
        // with same value
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE - 1; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == gameField[i + 1][j]) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE - 1; j++) {
                if (gameField[i][j] == gameField[i][j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }
}