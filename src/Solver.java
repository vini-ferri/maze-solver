import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Solver {
    private final int[][] maze;

    protected int[] startPos = new int[2];
    protected int[] endPos = new int[2];

    public Solver(int[][] maze) {
        this.maze = maze;
    }

    public char[][] solution() {
        char[][] charMaze = new char[maze.length][maze[0].length];

        for (char[] chars : charMaze) {
            Arrays.fill(chars, '-');
        }

        boolean[][] isVisited = new boolean[maze.length][maze[0].length];
        isVisited[startPos[0]][startPos[1]] = true;

        boolean isSolvable = finder(charMaze, isVisited, startPos[0], startPos[1]);

        if (isSolvable) {
            charMaze[startPos[0]][startPos[1]] = '2';
            charMaze[endPos[0]][endPos[1]] = '3';

            return charMaze;
        }

        System.out.println("Não é possível resolver o labirinto.");
        return new char[0][0];
    }

    public boolean isValidMove(boolean[][] isVisited, int newX, int newY) {
        if (newX < 0 || newX > maze.length - 1) {
            return false;
        }

        if (newY < 0 || newY > maze[newX].length - 1) {
            return false;
        }

        if (maze[newX][newY] == 1) {
            return false;
        }

        return !isVisited[newX][newY];
    }

    public boolean finder(char[][] charMaze, boolean[][] isVisited, int x, int y) {
        if (maze[x][y] == 3) {
            return true;
        }

        boolean found = false;

        // MOVE UP
        if (isValidMove(isVisited, x - 1, y)) {
            isVisited[x - 1][y] = true;
            found = finder(charMaze, isVisited, x - 1, y);
            if (found) {
                charMaze[x - 1][y] = 'S';
            }
        }

        // MOVE DOWN
        if (isValidMove(isVisited, x + 1, y)) {
            isVisited[x + 1][y] = true;
            found = finder(charMaze, isVisited, x + 1, y);
            if (found) {
                charMaze[x + 1][y] = 'S';
            }
        }

        // MOVE RIGHT
        if (!found && isValidMove(isVisited, x, y + 1)) {
            isVisited[x][y + 1] = true;
            found = finder(charMaze, isVisited, x, y + 1);
            if (found) {
                charMaze[x][y + 1] = 'S';
            }
        }

        // MOVE LEFT
        if (!found && isValidMove(isVisited, x, y - 1)) {
            isVisited[x][y - 1] = true;
            found = finder(charMaze, isVisited, x, y - 1);
            if (found) {
                charMaze[x][y - 1] = 'S';
            }
        }

        return found; 
    }

    public boolean isSolvable() {
        int startCount = 0;
        int endCount = 0;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                switch (maze[i][j]) {
                    case 0: // PATH
                    case 1: // WALL
                        break;
                    case 2: // START
                        if (++startCount > 1) {
                            return false;
                        }

                        startPos[0] = i;
                        startPos[1] = j;
                        break;
                    case 3: // END
                        if (++endCount > 1) {
                            return false;
                        }
                        endPos[0] = i;
                        endPos[1] = j;
                        break;
                    default:
                        return false;
                }
            }
        }

        return startCount == 1 && endCount == 1;
    }

    private static int[][] loadInputIntoArray() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.nextInt();
            }
        }
        return tiles;
    }

    public static void main(String[] args) {
        try {
            int[][] tiles = loadInputIntoArray();

            Solver solver = new Solver(tiles);

            if (solver.isSolvable()) {
                char[][] solution = solver.solution();
                System.out.println(toString(solution));
            } else {
                System.out.println("Não foi possível resolver este labirinto.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String toString(char[][] charMaze) {
        StringBuilder s = new StringBuilder();

        for (char[] chars : charMaze) {
            for (char aChar : chars) {
                s.append(aChar).append(" ");
            }
            s.append("\n");
        }

        return s.toString();
    }
}
