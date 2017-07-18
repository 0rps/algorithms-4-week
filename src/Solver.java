import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

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

        public int manhattan() { return privateBoard.manhattan() + step; }

        public int hamming() { return privateBoard.hamming() + step; }

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
        openSet = new MinPQ<>(new ManhattanComparator());
        openSet.insert(new BoardKey(initial, 0));
        closedSet = new ArrayList<>();

//        BoardKey k = new BoardKey(initial.neighbors().iterator().next(), 0);
//        openSet.insert(k);
//
//        StdOut.println(openSet.delMin().board().hamming());
//        StdOut.println(openSet.delMin().board().hamming());
//        return;

        // Board previousBoard = null;
        if (openSet.min().board().isGoal()) {
            goal = openSet.min();
        }
        while(openSet.size() != 0 && goal == null) {
            BoardKey current = openSet.delMin();
            Board currentBoard = current.board();

            Iterator<Board> neighbourIterator = currentBoard.neighbors().iterator();
            //StdOut.println("");
            //StdOut.println("Current: " + currentBoard);
            //StdOut.println("length: " + current.manhattan());
            //StdOut.println("------------------");
            int i = 0;
            while(neighbourIterator.hasNext()) {
                Board neighbourBoard = neighbourIterator.next();
                //StdOut.println("Candidate: \n" + neighbourBoard);
                if ( inClosedSet(neighbourBoard) ) {
                    //StdOut.println("Candidate alredy in closed\n");
                    continue;
                }

                BoardKey neighbourKey = new BoardKey(neighbourBoard, current.step() + 1, current);
                BoardKey addedBoardKey = getFromOpenSet(neighbourBoard);

                if ( addedBoardKey == null ) {
                    if (neighbourKey.board().isGoal()) {
                        //StdOut.println("GOOOOAAAALl\n");
                        goal = neighbourKey;
                        break;
                    }
                    openSet.insert(neighbourKey);
                    //StdOut.println("Added\n");
                } else if (addedBoardKey.step() > neighbourKey.step()) {
                    //StdOut.println("added as copy with little metric \n");
                    openSet.insert(neighbourKey);
                } else {
                    //StdOut.println("Candidate has too mush metric\n");
                }
            }
        }

        if (goal == null) {
            isSolvable = false;
            return;
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