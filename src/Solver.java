import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by orps on 14.07.17.
 */
public class Solver {

    class ManhattanComparator implements Comparator<BoardKey> {

        @Override
        public int compare(BoardKey a, BoardKey b) {
            int aKey = a.manhattan();
            int bKey = b.manhattan();
            if (aKey < bKey)       return -1;
            else if (bKey < aKey)  return 1;
            else                   return 0;
        }
    }

    class HammingComparator implements Comparator<BoardKey> {

        @Override
        public int compare(BoardKey a, BoardKey b) {
            int aKey = a.hamming();
            int bKey = b.hamming();
            if (aKey < bKey)       return -1;
            else if (bKey < aKey)  return 1;
            else                   return 0;
        }
    }

    class BoardKey {

        private Board privateBoard;
        private int step;
        private BoardKey previous;

        public BoardKey(Board b, int s) {
            this.privateBoard = b;
            this.step = s;
            this.previous = null;
        }

        public BoardKey(Board b, int s, BoardKey p) {
            this.previous = p;
            this.privateBoard = b;
            this.step = s;
        }

        public Board board() { return privateBoard; }

        public BoardKey previousBoard() { return previous; }
        public void setPreviousBoard(BoardKey p) { previous = p;}

        public int step() { return this.step; }
        public void setStep(int newStep) { this.step = newStep; }

        public int manhattan() { return privateBoard.manhattan(); }

        public int hamming() { return privateBoard.hamming(); }

    }

    private MinPQ<BoardKey> openSet;
    private ArrayList<BoardKey> closedSet;
    private int movesCount = 0;
    private boolean isSolvable = true;
    private BoardKey goal;
    private ArrayList<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }
        openSet = new MinPQ<>(new HammingComparator());
        openSet.insert(new BoardKey(initial, 0));
        closedSet = new ArrayList<>();

        while(openSet.size() != 0) {
            BoardKey current = openSet.delMin();
            Board currentBoard = current.board();
            closedSet.add(current);

            Iterator<Board> neighbourIterator = currentBoard.neighbors().iterator();
            while(neighbourIterator.hasNext()) {
                Board neighbourBoard = neighbourIterator.next();

                if ( inClosedSet(neighbourBoard) ) {
                    continue;
                }

                BoardKey neighbourKey;

                if ( !inOpenSet(neighbourBoard) ) {
                    neighbourKey = new BoardKey(neighbourBoard, current.step() +1, current);
                    if (neighbourKey.board().isGoal()) {
                        goal = neighbourKey;
                        break;
                    }
                    openSet.insert(neighbourKey);
                } else {
                    neighbourKey = getFromOpenSet(neighbourBoard);
                    if (neighbourKey.step() > current.step()+1) {
                        neighbourKey.setStep(current.step + 1);
                        neighbourKey.setPreviousBoard( current );
                    }
                }
            }
        }

        movesCount = goal.step();
        solution = new ArrayList<>();
        BoardKey currentKey = goal;
        while(currentKey != null) {
            solution.add(currentKey.board());
            currentKey = currentKey.previousBoard();
        }
        Collections.reverse(solution);
    }

    public boolean isSolvable() { return isSolvable; }

    public int moves() {
        return movesCount;
    }

    public Iterable<Board> solution() {
        return (Iterable<Board>)solution.clone();
    }

    public static void main(String[] args) {

    }

    BoardKey getFromOpenSet(Board key) {
        Iterator<BoardKey> iterator = openSet.iterator();
        while(iterator.hasNext()) {
            BoardKey next = iterator.next();
            if (next.board().equals(key)) {
                return next;
            }
        }

        return null;
    }

    private boolean inClosedSet(BoardKey key) {
        return inClosedSet(key.board());
    }

    private boolean inOpenSet(BoardKey key) {
        return inOpenSet(key.board());
    }

    private boolean inClosedSet(Board key) {
        Iterator<BoardKey> iterator = closedSet.iterator();
        while(iterator.hasNext()) {
            BoardKey next = iterator.next();
            if (next.board().equals(key)) {
                return true;
            }
        }

        return false;
    }

    private boolean inOpenSet(Board key) {
        Iterator<BoardKey> iterator = openSet.iterator();
        while(iterator.hasNext()) {
            BoardKey next = iterator.next();
            if (next.board().equals(key)) {
                return true;
            }
        }

        return false;
    }
}