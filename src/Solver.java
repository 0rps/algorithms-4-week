import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by orps on 14.07.17.
 */
public class Solver {

    private class TreeNode implements Comparable<TreeNode> {

        private final Board board;
        private final int height;
        private final TreeNode parent;

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

        public int metric() { return board.manhattan() + height; }

        @Override
        public int compareTo(TreeNode node) {
            int aKey = metric();
            int bKey = node.metric();
            if (aKey < bKey)       return -1;
            else if (bKey < aKey)  return 1;
            else                   return 0;
        }
    }

    private class GameTree {

        private final MinPQ<TreeNode> openSet;

        private final ArrayList<Board> solution = new ArrayList<>();

        public GameTree(Board initial) {
            openSet = new MinPQ<>();
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
            Board previousBoard = null;
            if (currentNode.parent() != null) {
                previousBoard = currentNode.parent().board();
            }

            for (Board neighbourBoard: currentBoard.neighbors()) {
                if ( neighbourBoard.equals(previousBoard) ) {
                    continue;
                }

                TreeNode neighbourNode = new TreeNode(neighbourBoard, currentNode.height() + 1, currentNode);
                if (neighbourNode.board().isGoal()) {
                    makeSolution(neighbourNode);
                    return false;
                }
                openSet.insert(neighbourNode);
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
            ArrayList<Board> result = new ArrayList<>(solution.size());
            result.addAll(solution);

            return result;
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
    }

    private final ArrayList<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }

        GameTree initialGame = new GameTree(initial);
        GameTree twinGame = new GameTree(initial.twin());

        boolean flag = true; while (flag) { flag = initialGame.next() && twinGame.next(); }

        if (initialGame.hasGoal()) {
            solution = initialGame.solution();
        } else if (initialGame.isUnsolvable() || twinGame.hasGoal()) {
            solution = null;
        } else {
            flag = true; while (flag) { flag = initialGame.next(); };
            solution = initialGame.solution();
        }
    }

    public boolean isSolvable() { return solution != null; }

    public int moves() {
        if (isSolvable()) {
            return solution.size() - 1;
        }

        return 0;
    }

    public Iterable<Board> solution() {
        if (solution == null) { return null;}
        ArrayList<Board> result = new ArrayList<>(solution.size());
        result.addAll(solution);
        return result;
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