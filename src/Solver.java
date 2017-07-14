/**
 * Created by orps on 14.07.17.
 */
public class Solver {

    private Board board;

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        board = initial;
    }

    public boolean isSolvable() {
        return false;
    }

    public int moves() {
        return 0;
    }

    public Iterable<Board> solution() {
        return board.neighbors();
    }

    public static void main(String[] args) {

    }
}