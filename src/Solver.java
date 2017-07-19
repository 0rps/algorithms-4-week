import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
// import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by orps on 14.07.17.
 */
public class Solver {

    private class ManhattanComparator implements Comparator<TreeNode> {

        @Override
        public int compare(TreeNode a, TreeNode b) {
            int aKey = a.manhattan();
            int bKey = b.manhattan();
            if (aKey < bKey)       return -1;
            else if (bKey < aKey)  return 1;
            else                   return 0;
        }
    }

//    private class HammingComparator implements Comparator<TreeNode> {
//
//        @Override
//        public int compare(TreeNode a, TreeNode b) {
//            int aKey = a.hamming();
//            int bKey = b.hamming();
//            if (aKey < bKey)       return -1;
//            else if (bKey < aKey)  return 1;
//            else                   return 0;
//        }
//    }

    private class TreeNode {

        private Board board;
        private int height;
        private TreeNode parent;

        public TreeNode(Board board, int height) {
            this.board = board;
            this.height = height;
            this.parent = null;
        }

        public TreeNode(Board board, int height, TreeNode parent) {
            this.parent = parent;
            this.board = board;
            this.height = height;
        }

        public Board board() { return board; }

        public TreeNode parent() { return parent; }

        public int height() { return this.height; }

        public int manhattan() { return board.manhattan() + height; }

        // public int hamming() { return board.hamming() + height; }

    }

    private class GameTree {

        private HashMap<Integer, List<Board>> closedSet = new HashMap<>();
        private MinPQ<TreeNode> openSet;

        private ArrayList<Board> solution = new ArrayList<>();

        public GameTree(Board initial, Comparator<TreeNode> cmp) {
            openSet = new MinPQ<TreeNode>(cmp);
            openSet.insert(new TreeNode(initial, 0));

            if (initial.isGoal()) {
                makeSolution(openSet.delMin());
            }
        }

        public boolean next() {
            if (hasGoal()) { return false;}

            if (openSet.isEmpty()) { return false; }

            TreeNode currentNode = openSet.delMin();
            Board currentBoard = currentNode.board();
            addToClosedSet(currentBoard);

            Iterator<Board> neighbourIterator = currentBoard.neighbors().iterator();

            while(neighbourIterator.hasNext()) {
                Board neighbourBoard = neighbourIterator.next();
                //StdOut.println("Candidate: \n" + neighbourBoard);
                if ( inClosedSet(neighbourBoard) ) {
                    //StdOut.println("Candidate alredy in closed\n");
                    continue;
                }

                TreeNode neighbourNode = new TreeNode(neighbourBoard, currentNode.height() + 1, currentNode);
                TreeNode addedBoardKey = getFromOpenSet(neighbourBoard);

                if ( addedBoardKey == null ) {
                    if (neighbourNode.board().isGoal()) {
                        makeSolution(neighbourNode);
                        return false;
                    }
                    openSet.insert(neighbourNode);
                } else if (addedBoardKey.height() > neighbourNode.height()) {
                    openSet.insert(neighbourNode);
                }
            }

            return true;
        }

        public boolean hasGoal() {
            return !solution.isEmpty();
        }

        public boolean isUnsolvable() {
            return openSet.isEmpty() && !hasGoal();
        }

        public ArrayList<Board> solution() {
            return (ArrayList<Board>)solution.clone();
        }

        private void makeSolution(TreeNode leaf) {
            if (leaf == null) {
                return;
            }

            while(leaf != null) {
                solution.add(leaf.board());
                leaf = leaf.parent();
            }
            Collections.reverse(solution);
        }

        void addToClosedSet(Board key) {
            List<Board> list = closedSet.get(key.manhattan());
            if (list == null) {
                list = new ArrayList<>();
                closedSet.put(key.manhattan(), list);
            }

            list.add(key);
        }

        private boolean inClosedSet(Board key) {
            List<Board> list = closedSet.get(key.manhattan());
            if (list == null) {
                return false;
            }

            Iterator<Board> iterator = list.iterator();
            while(iterator.hasNext()) {
                if (iterator.next().equals(key)) {
                    return true;
                }
            }

            return false;
        }

        TreeNode getFromOpenSet(Board key) {
            Iterator<TreeNode> iterator = openSet.iterator();
            while(iterator.hasNext()) {
                TreeNode next = iterator.next();
                if (next.board().equals(key)) {
                    return next;
                }
            }

            return null;
        }
    }

    private ArrayList<Board> solution = new ArrayList<>();

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        Comparator<TreeNode> cmp = new ManhattanComparator();

        GameTree initialGame = new GameTree(initial, cmp);
        GameTree twinGame = new GameTree(initial.twin(), cmp);

        while (initialGame.next() && twinGame.next());

        if (initialGame.hasGoal()) {
            solution = initialGame.solution();
        } else if (initialGame.isUnsolvable() || twinGame.hasGoal()) {

        } else {
            // StdOut.println("twin is unsolvable");
            while (initialGame.next());
            solution = initialGame.solution();
        }
    }

    public boolean isSolvable() { return !solution.isEmpty(); }

    public int moves() { return solution.size() - 1; }

    public Iterable<Board> solution() {
        return (Iterable<Board>)solution.clone();
    }

    public static void main(String[] args) {
        for (String filename : args) {
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            if (solver.isSolvable()) {
                StdOut.println("Minimum number of moves = " + solver.moves() + "\n");
                Iterator<Board> solution = solver.solution().iterator();
                while (solution.hasNext()) {
                    StdOut.println(solution.next());
                }
            } else {
                StdOut.println("No solution possible");
            }
        }
    }
}