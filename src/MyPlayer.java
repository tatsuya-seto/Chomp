import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyPlayer {
    public Chip[][] gameBoard;

    // Memo: board state -> isWinning
    private Map<String, Boolean> memo = new HashMap<>();

    public MyPlayer() {
    }

    public Point move(Chip[][] pBoard) {
        System.out.println("MyPlayer Move");

        gameBoard = pBoard;

        int[] rows = getBoardState(pBoard);

        System.out.println("Current board: " + Arrays.toString(rows));

        Point bestMove = optimalMove(rows);

        if (bestMove != null) {
            System.out.println("Choosing: (" + bestMove.x + "," + bestMove.y + ")");
            return bestMove; // x = row, y = col
        }

        // Fallback: first alive chip
        for (int row = 0; row < pBoard.length; row++) {
            for (int col = 0; col < pBoard[row].length; col++) {
                if (isChipPresent(pBoard, row, col)) {
                    return new Point(row, col);
                }
            }
        }

        return new Point(0, 0);
    }

    public int[] getBoardState(Chip[][] pBoard) {
        int size = pBoard.length;
        int[] rows = new int[size];

        for (int row = 0; row < size; row++) {
            int count = 0;
            for (int col = 0; col < pBoard[row].length; col++) {
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

    // Finds a move that sends opponent to a losing board
    public Point optimalMove(int[] rows) {
        int size = rows.length;

        for (int row = 0; row < size; row++) {
            int length = rows[row];

            for (int col = 0; col < length; col++) {
                // Skip poison square move directly here unless forced by fallback
                if (row == 0 && col == 0) {
                    continue;
                }

                int[] next = applyMove(rows, row, col);

                // Do not allow empty-top-row states in main search
                if (next[0] == 0) {
                    continue;
                }

                if (!isWinning(next)) {
                    return new Point(row, col); // coordinate starts at 0,0
                }
            }
        }

        return null;
    }

    // Returns true if current state is winning for player to move
    public boolean isWinning(int[] rows) {
        String key = encode(rows);

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Base losing state: only poison square remains
        if (isOnlyPoisonLeft(rows)) {
            memo.put(key, false);
            return false;
        }

        int size = rows.length;

        for (int row = 0; row < size; row++) {
            int length = rows[row];

            for (int col = 0; col < length; col++) {
                // Skip taking poison directly in recursive search
                if (row == 0 && col == 0) {
                    continue;
                }

                int[] next = applyMove(rows, row, col);

                if (next[0] == 0) {
                    continue;
                }

                if (!isWinning(next)) {
                    memo.put(key, true);
                    return true;
                }
            }
        }

        memo.put(key, false);
        return false;
    }

    // Apply click at (row, col):
    // remove everything at rows >= row and cols >= col
    public int[] applyMove(int[] rows, int moveRow, int moveCol) {
        int[] next = Arrays.copyOf(rows, rows.length);

        for (int r = moveRow; r < next.length; r++) {
            next[r] = Math.min(next[r], moveCol);
        }

        return next;
    }

    public boolean isOnlyPoisonLeft(int[] rows) {
        if (rows[0] != 1) {
            return false;
        }

        for (int i = 1; i < rows.length; i++) {
            if (rows[i] != 0) {
                return false;
            }
        }

        return true;
    }

    public String encode(int[] rows) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows.length; i++) {
            sb.append(rows[i]).append(',');
        }

        return sb.toString();
    }
}