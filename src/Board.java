import java.util.ArrayList;

/**
 * Created by orps on 14.07.17.
 */
public class Board {

    private final int[][] data;
    private int iZero;
    private int jZero;


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
        for (int i = 0; i < rank; i++) {
            data[i] = blocks[i].clone();
            for (int j = 0; j < rank; j++) {
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

    public int hamming() {
        int result = 0;

        final int length = dimension();
        int mustValue = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                mustValue++;
                if (i == length-1 && j == length-1) {
                    mustValue = 0;
                }
                if (data[i][j] != mustValue) {
                    result++;
                }
            }
        }
        return result;
    }

    public int manhattan() {
        int result = 0;

        final int length = dimension();
        int mustValue = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                mustValue++;
                if (i == length-1 && j == length-1) {
                    mustValue = 0;
                }

                if (data[i][j] != mustValue) {
                    int iMust = data[i][j] / length;
                    int jMust = data[i][j] % length;
                    if (data[i][j] == 0) {
                        iMust = length-1;
                        jMust = length-1;
                    }
                    result += Math.abs(i - iMust) + Math.abs(j - jMust);
                }
            }
        }

        return result;
    }

    public boolean isGoal()  {
        final int length = dimension();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int mustValue = i * length + j + 1;
                if (i == length-1 && j == length-1) {
                    mustValue = 0;
                }
                if (data[i][j] != mustValue) {
                    return false;
                }
            }
        }
        return true;
    }

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
                    Board board = this.twin();
                    board.data[iZero][jZero] = board.data[newIZero][newJZero];
                    board.data[newIZero][newJZero] = 0;
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