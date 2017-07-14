/**
 * Created by orps on 14.07.17.
 */
public class Board {

    private final int[][] data;

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
        }
    }

    public int dimension() {
        return data.length;
    }

    public int hamming() {
        return 0;
    }

    public int manhattan() {
        return 0;
    }

    public boolean isGoal()  {
        return false;
    }

    public Board twin() {
        return new Board(data);
    }

    public boolean equals(Object y) {
        return false;
    }

    public Iterable<Board> neighbors() {

    }

    public String toString() {
        return "";
    }

    public static void main(String[] args) {

    }
}