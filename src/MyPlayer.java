import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyPlayer {
    // Storing the current board
    public Chip[][] gameBoard;

    // HashMap to remember if a board is winning or losing
    private Map<String, Boolean> memo = new HashMap<>();//hashmap to store winning moves. Hashmap instead of arraylist allows for very fast returning of stored variables.

    // Basic constructor
    public MyPlayer() {
    }

    // Main method that chooses the move
    public Point move(Chip[][] pBoard) {
        System.out.println("MyPlayer Move");

        // Saving the current board
        gameBoard = pBoard;

        // Turning the board into row counts
        int[] rows = getBoardState(pBoard);

        // Printing the row counts
        System.out.println("Current board: " + Arrays.toString(rows));

        // Trying to find the best possible move
        Point bestMove = optimalMove(rows);

        // If a best move is found, return it
        if (bestMove != null) {
            System.out.println("Choosing: (" + bestMove.x + "," + bestMove.y + ")");
            return bestMove; // x = row, y = col
        }

        // Backup plan: just find the first alive chip
        for (int row = 0; row < pBoard.length; row++) {
            for (int col = 0; col < pBoard[row].length; col++) {
                if (isChipPresent(pBoard, row, col)) {
                    return new Point(row, col);
                }
            }
        }

        // Final backup if nothing else works
        return new Point(0, 0);
    }

    // Returning current state of the board
    public int[] getBoardState(Chip[][] pBoard) {
        // Getting board size and making array for row counts
        int size = pBoard.length;
        int[] rows = new int[size];

        // Going through each row
        for (int row = 0; row < size; row++) {
            int count = 0;

            // Counting alive chips in that row
            for (int col = 0; col < pBoard[row].length; col++) {
                if (isChipPresent(pBoard, row, col)) {
                    count++;
                }
            }

            // Saving count for that row
            rows[row] = count;
        }

        // Returning row counts
        return rows;
    }

    // Checking if a chip is still there and alive
    public boolean isChipPresent(Chip[][] pBoard, int row, int col) {
        return pBoard[row][col] != null && pBoard[row][col].isAlive;

    }

    // Finds a move that sends opponent to a losing board
    public Point optimalMove(int[] rows) {
        int size = rows.length;

        // Going through every row
        for (int row = 0; row < size; row++) {
            int length = rows[row];

            // Going through every chip in that row
            for (int col = 0; col < length; col++) {
                // Skipping the poison square unless forced later
                if (row == 0 && col == 0) {
                    continue;
                }

                // Making the board that would happen after this move
                int[] next = applyMove(rows, row, col);

                // Skipping impossible top-empty states
                if (next[0] == 0) {
                    continue;
                }

                // If opponent gets a losing board, take this move
                if (!isWinning(next)) {
                    return new Point(row, col); // coordinate starts at 0,0
                }
            }
        }

        // If no best move was found
        return null;
    }

    // Returns true if current state is winning for player to move
    public boolean isWinning(int[] rows) {
        // Turning board into a string key
        String key = encode(rows);

        // If already solved before, return saved answer
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Base losing state: only poison square remains
        if (isOnlyPoisonLeft(rows)) {
            memo.put(key, false);
            return false;
        }

        int size = rows.length;

        // Trying every possible move
        for (int row = 0; row < size; row++) {
            int length = rows[row];

            for (int col = 0; col < length; col++) {
                // Skipping poison square in the search
                if (row == 0 && col == 0) {
                    continue;
                }

                // Simulating the next board after the move
                int[] next = applyMove(rows, row, col);

                // Ignoring bad empty-top-row states
                if (next[0] == 0) {
                    continue;
                }

                // If one move gives the opponent a losing state, this is winning
                if (!isWinning(next)) {
                    memo.put(key, true);
                    return true;
                }
            }
        }

        // If every move helps the opponent, this board is losing
        memo.put(key, false);
        return false;
    }

    // Apply click at (row, col):
    // remove everything at rows >= row and cols >= col
    public int[] applyMove(int[] rows, int moveRow, int moveCol) {
        // Copying the board so original does not change
        int[] next = Arrays.copyOf(rows, rows.length);

        // Shortening all rows from moveRow downward
        for (int r = moveRow; r < next.length; r++) {
            next[r] = Math.min(next[r], moveCol);
        }

        // Returning the new board
        return next;
    }

    // Checking if only the poison chip is left
    public boolean isOnlyPoisonLeft(int[] rows) {
        // First row must only have one chip
        if (rows[0] != 1) {
            return false;
        }

        // All other rows must be empty
        for (int i = 1; i < rows.length; i++) {
            if (rows[i] != 0) {
                return false;
            }
        }

        // This means only poison is left
        return true;
    }

    // Turning the board array into a string
    public String encode(int[] rows) {
        StringBuilder sb = new StringBuilder();

        // Adding each row count into the string
        for (int i = 0; i < rows.length; i++) {
            sb.append(rows[i]).append(',');
        }

        // Returning the finished key
        return sb.toString();
    }
}