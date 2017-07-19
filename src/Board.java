// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

/**
 * Created by orps on 14.07.17.
 */
public class Board {

    private final int[][] data;
    private int iZero;
    private int jZero;
    private final int manhattanLength;
    private final int hammingLength;


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
        int ml = 0;
        int hl = 0;
        for (int i = 0; i < rank; i++) {
            data[i] = blocks[i].clone();
            for (int j = 0; j < rank; j++) {
                mustValue++;

                if (i == rank-1 && j == rank-1) {
                    mustValue = 0;
                }

                if (data[i][j] != mustValue && data[i][j] != 0) {
                    hl++;
                }

                if (data[i][j] != mustValue && data[i][j] != 0) {
                    int iMust = (data[i][j] - 1) / rank;
                    int jMust = (data[i][j] - 1) % rank;
                    ml += Math.abs(i - iMust) + Math.abs(j - jMust);
                }

                if (blocks[i][j] == 0) {
                    iZero = i;
                    jZero = j;
                }

            }
        }
        hammingLength = hl;
        manhattanLength = ml;
    }

    public int dimension() {
        return data.length;
    }

    public int hamming() { return hammingLength; }

    public int manhattan() { return manhattanLength; }

    public boolean isGoal()  { return hammingLength == 0; }

    public Board twin() {
        Board result;

        if (iZero != 0) {
            int a = data[0][0];
            data[0][0] = data[0][1];
            data[0][1] = a;

            result = new Board(data);

            data[0][1] = data[0][0];
            data[0][0] = a;

        } else {
            int a = data[1][0];
            data[1][0] = data[1][1];
            data[1][1] = a;

            result = new Board(data);

            data[1][1] = data[1][0];
            data[1][0] = a;
        }
        return result;
    }

    public boolean equals(Object y) {
        if (y == null) { return false; }

        if (!(y instanceof Board)) { return false; }

        Board board = (Board) y;
        final int length = dimension();
        if (length != board.dimension() && this.manhattanLength != board.manhattanLength) {
            return false;
        }

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
        ArrayList<Board> boardArray = new ArrayList<Board>(4);
        int length = dimension();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) + Math.abs(j) != 1)
                    continue;

                int newIZero = iZero + i;
                int newJZero = jZero + j;

                if (newIZero >= 0 && newIZero < length && newJZero >= 0 && newJZero < length) {

                    data[iZero][jZero] = data[newIZero][newJZero];
                    data[newIZero][newJZero] = 0;

                    boardArray.add(new Board(data));

                    data[newIZero][newJZero] = data[iZero][jZero];
                    data[iZero][jZero] = 0;


                }
            }
        }

        return boardArray;
    }

    public String toString() {

        final int length = dimension();
        int dSquare = length*length;

        int space;
        if (dSquare > 99) {
            space = 3;
        } else {
            space = 2;
        }
        String format = "%" + space + "d";
        StringBuilder builder = new StringBuilder();
        builder.append("" + length + "\n");
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                builder.append(String.format(format, data[i][j]) + " ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}