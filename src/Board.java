import java.util.ArrayList;

/**
 * Created by orps on 14.07.17.
 */
public class Board {

    private final int[][] data;
    private int iZero;
    private int jZero;
    private int manhattanLength = 0;
    private int hammingLength = 0;


    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new java.lang.IllegalArgumentException();
        }

        final int rank = blocks.length;
        for (int[] i: blocks) {
            if (i.length != rank) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        data = new int[blocks.length][];
        int mustValue = 0;
        for (int i = 0; i < rank; i++) {
            data[i] = blocks[i].clone();
            for (int j = 0; j < rank; j++) {
                mustValue++;

                if (i == rank-1 && j == rank-1) {
                    mustValue = 0;
                }

                if (data[i][j] != mustValue) {
                    hammingLength++;
                }

                if (data[i][j] != mustValue) {
                    int iMust = data[i][j] / rank;
                    int jMust = data[i][j] % rank;
                    if (data[i][j] == 0) {
                        iMust = rank - 1;
                        jMust = rank - 1;
                    }
                    manhattanLength += Math.abs(i - iMust) + Math.abs(j - jMust);
                }

                if (blocks[i][j] == 0) {
                    iZero = i;
                    jZero = j;
                }

            }
        }
    }

    public int dimension() {
        return data.length;
    }

    public int hamming() { return hammingLength; }

    public int manhattan() { return manhattanLength; }

    public boolean isGoal()  { return hammingLength > 0; }

    public Board twin() {
        Board board = new Board(data);
        board.iZero = iZero;
        board.jZero = jZero;
        return board;
    }

    public boolean equals(Object y) {
        Board board = (Board)y;
        if (board.dimension() != this.dimension()) {
            return false;
        }

        final int length = dimension();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (data[i][j] != board.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int iDelta = -1;

        ArrayList<Board> boardArray = new ArrayList<Board>(4);
        int length = dimension();

        for (int i = 0; i < 3; i++) {
            int jDelta = -2;
            for (int j = 0; j < 3; j++) {
                jDelta++;
                if (Math.abs(i) + Math.abs(j) != 1)
                    continue;

                int newIZero = iZero + iDelta;
                int newJZero = jZero + jDelta;

                if (newIZero < 0 || newIZero >= length || newJZero < 0 || newJZero >= length) {
                    
                    data[iZero][jZero] = data[newIZero][newJZero];
                    data[newIZero][newJZero] = 0;

                    Board board = this.twin();

                    data[newIZero][newJZero] = data[iZero][jZero];
                    data[iZero][jZero] = 0;

                    boardArray.add(board);
                }
            }
            iDelta++;
        }

        return boardArray;
    }

    public String toString() {

        final int length = dimension();
        int dSquare = length*length;

        int space;
        if (dSquare > 99) {
            space = 3;
        } else if (dSquare > 9) {
            space = 2;
        } else {
            space = 1;
        }
        String format = "%" + space + "d";

        String result = new String();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                result += String.format(format, data[i][j]) + " ";
            }
            result += "\n";
        }

        return result;
    }

    public static void main(String[] args) {

    }
}