import java.awt.*;

public class MyPlayer {
    public Chip[][] gameBoard;

    int[][] losingBoards = new int[20][3];
    int[][] winningBoards = new int[20][3];

    int losingCount = 0;
    int winningCount = 0;

    public MyPlayer() {
    }

    public Point move(Chip[][] pBoard) {

        System.out.println("MyPlayer Move");

        gameBoard = pBoard; //Current State of Board

        int[] board = getBoardState(pBoard);
        int r1 = board[0];
        int r2 = board[1];
        int r3 = board[2];

        System.out.println("Current board: " + r1 + "," + r2 + "," + r3);

        buildBoards();

        Point bestMove = optimalMove(r1, r2, r3);

        if (bestMove != null) {
            System.out.println("Choosing: (" + bestMove.x + "," + bestMove.y + ")");
            return bestMove;
        }//good

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (isChipPresent(pBoard, row, col)) {
                    return new Point(row, col);
                }
            }
        }

        return new Point(0, 0);
    }

    public int[] getBoardState(Chip[][] pBoard) {
        int[] rows = new int[3];

        for (int row = 0; row < 3; row++) {
            int count = 0;

            for (int col = 0; col < 3; col++) {
                if (isChipPresent(pBoard, row, col)) {
                    count++;
                }
            }

            rows[row] = count;
        }

        return rows;
    }

    public boolean isChipPresent(Chip[][] pBoard, int row, int col) {
        return pBoard[row][col] != null && pBoard[row][col].isAlive;
    }

    public void buildBoards() {
        losingCount = 0;
        winningCount = 0;

        addLosingBoard(1, 0, 0);

        for (int r1 = 1; r1 <= 3; r1++) {
            for (int r2 = 0; r2 <= r1; r2++) {
                for (int r3 = 0; r3 <= r2; r3++) {

                    if (r1 == 1 && r2 == 0 && r3 == 0) {
                        continue;
                    }

                    evaluateBoard(r1, r2, r3);
                }
            }
        }
    }

    public void evaluateBoard(int r1, int r2, int r3) {

        boolean winningBoard = false;
        int[] rows = {r1, r2, r3};

        for (int row = 0; row < 3; row++) {
            int length = rows[row];

            for (int col = 1; col <= length; col++) {

                int nr1 = r1;
                int nr2 = r2;
                int nr3 = r3;

                int newVal = col - 1;

                if (row == 0) {
                    nr1 = newVal;
                    nr2 = Math.min(nr2, newVal);
                    nr3 = Math.min(nr3, newVal);
                } else if (row == 1) {
                    nr2 = newVal;
                    nr3 = Math.min(nr3, newVal);
                } else {
                    nr3 = newVal;
                }

                if (nr1 == 0) continue;

                if (nr1 >= nr2 && nr2 >= nr3) {
                    if (isLosingBoard(nr1, nr2, nr3)) {
                        winningBoard = true;
                    }
                }
            }
        }

        if (winningBoard) {
            addWinningBoard(r1, r2, r3);
        } else {
            addLosingBoard(r1, r2, r3);
        }
    }

    public boolean isLosingBoard(int r1, int r2, int r3) {
        for (int i = 0; i < losingCount; i++) {
            if (losingBoards[i][0] == r1 &&
                    losingBoards[i][1] == r2 &&
                    losingBoards[i][2] == r3) {
                return true;
            }
        }
        return false;
    }

    public void addLosingBoard(int r1, int r2, int r3) {
        losingBoards[losingCount][0] = r1;
        losingBoards[losingCount][1] = r2;
        losingBoards[losingCount][2] = r3;
        losingCount++;
    }

    public void addWinningBoard(int r1, int r2, int r3) {
        winningBoards[winningCount][0] = r1;
        winningBoards[winningCount][1] = r2;
        winningBoards[winningCount][2] = r3;
        winningCount++;
    }

    public Point optimalMove(int r1, int r2, int r3) {

        int[] rows = {r1, r2, r3};

        for (int row = 0; row < 3; row++) {
            int length = rows[row];

            for (int col = 1; col <= length; col++) {

                int nr1 = r1;
                int nr2 = r2;
                int nr3 = r3;

                int newVal = col - 1;

                if (row == 0) {
                    nr1 = newVal;
                    nr2 = Math.min(nr2, newVal);
                    nr3 = Math.min(nr3, newVal);
                } else if (row == 1) {
                    nr2 = newVal;
                    nr3 = Math.min(nr3, newVal);
                } else {
                    nr3 = newVal;
                }

                if (nr1 == 0) continue;

                if (nr1 >= nr2 && nr2 >= nr3) {
                    if (isLosingBoard(nr1, nr2, nr3)) {
                        int x = col - 1;
                        int y = row;

                        return new Point(y, x); //returning move coordinates
                    }
                }
            }
        }

        return null;
    }
}