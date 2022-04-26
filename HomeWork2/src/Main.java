import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static void findCells(String str, ArrayList<Cell> move) throws Exception {
        Pattern p = Pattern.compile("[a-hA-H]\\d");
        Matcher m = p.matcher(str);
        while (m.find()) {
            char x = str.charAt(m.start());
            char y = str.charAt(m.start() + 1);
            Cell c = new Cell(x, y);
            move.add(c);
        }
    }

    static void recognize(Matcher m, String str, int color) {
        while (m.find()) {
            char x = str.charAt(m.start());
            char y = str.charAt(m.start() + 1);
            if ((int) (x) < 96) {
                new Missis(x, y, color);
            } else {
                new Checker(x, y, color);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String w = sc.nextLine();
        String b = sc.nextLine();

        Pattern pattern = Pattern.compile("[a-hA-H]\\d");
        Pattern regularMoveW = Pattern.compile("^[a-hA-H]\\d-[a-hA-H]\\d");
        Pattern regularMoveB = Pattern.compile("[a-hA-H]\\d-[a-hA-H]\\d$");
        Pattern fightMoveW = Pattern.compile("^([a-hA-H]\\d:)+[a-hA-H]\\d");
        Pattern fightMoveB = Pattern.compile("([a-hA-H]\\d:)+[a-hA-H]\\d$");
        Matcher matcherWhite = pattern.matcher(w);
        Matcher matcherBlack = pattern.matcher(b);
        recognize(matcherWhite, w, 0);
        recognize(matcherBlack, b, 1);
        try {
            while (sc.hasNext()) {
                String move = sc.nextLine();
                Matcher WRegMove = regularMoveW.matcher((move));
                Matcher BRegMove = regularMoveB.matcher((move));
                Matcher WFightMove = fightMoveW.matcher((move));
                Matcher BFightMove = fightMoveB.matcher((move));
                String strW;
                String strB;
                ArrayList<Cell> WhiteMove = new ArrayList<>();
                ArrayList<Cell> BlackMove = new ArrayList<>();
                if (WFightMove.find()) {
                    strW = move.substring(WFightMove.start(), WFightMove.end());
                    findCells(strW, WhiteMove);
                    Figure.fightMove(WhiteMove);
                } else if (WRegMove.find()) {

                    strW = move.substring(WRegMove.start(), WRegMove.end());
                    findCells(strW, WhiteMove);
                    Figure.regularMove(WhiteMove);
                }

                if (BFightMove.find()) {
                    strB = move.substring(BFightMove.start(), BFightMove.end());
                    findCells(strB, BlackMove);
                    Figure.fightMove(BlackMove);
                } else if (BRegMove.find()) {
                    strB = move.substring(BRegMove.start(), BRegMove.end());
                    findCells(strB, BlackMove);
                    Figure.regularMove(BlackMove);
                }
            }
            Figure.print();
        } catch (CheckersExceptions ex) {
            System.out.println(ex.getMessage());
        }

    }
}

class CheckersExceptions extends Exception {
    public CheckersExceptions(String message) {
        super(message);
    }
}

class Cell {
    int x;
    int y;

    Cell(char x, char y) throws Exception {
        this.x = Math.abs(8 - Integer.parseInt(String.valueOf(y)));
        this.y = (int) (x) >= 97 ? (int) (x) - 97 : (int) (x) - 65;
        if (this.x < 0 || this.x > 7 || this.y < 0 || this.y > 7) {
            throw new CheckersExceptions("error");
        }
    }
}


class FigureComparator implements Comparator<Figure> {
    public int compare(Figure f1, Figure f2) {
        char c1 = f1 instanceof Missis ? (char) (f1.y + 65) : (char) (f1.y + 97);
        char c2 = f2 instanceof Missis ? (char) (f2.y + 65) : (char) (f2.y + 97);

        if (c1 > c2) {
            return 1;
        } else if (c1 < c2) {
            return -1;
        } else {
            return Integer.compare(f2.x, f1.x);
        }
    }
}

abstract class Figure implements Comparator<Figure> {
    protected int x;
    protected int y;
    protected int color;
    protected boolean alive = true;
    protected static Figure[][] board = new Figure[8][8];
    protected static ArrayList<Figure> white = new ArrayList<>();
    protected static ArrayList<Figure> black = new ArrayList<>();

    protected static Comparator<Figure> fComp = new FigureComparator();

    abstract boolean checkFight();

    abstract void regularMove(Cell cell) throws Exception;

    abstract void fightMove(Cell cell) throws Exception;


    static void regularMove(ArrayList<Cell> moveCells) throws Exception {

        if (board[moveCells.get(0).x][moveCells.get(0).y] == null) {
            throw new CheckersExceptions("error");
        } else {
            if (board[moveCells.get(0).x][moveCells.get(0).y].color == 0) {
                for (Figure x : white) {
                    if (x.checkFight()) {
                        throw new CheckersExceptions("invalid move");
                    }
                }
            } else {
                for (Figure x : black) {
                    if (x.checkFight()) {
                        throw new CheckersExceptions("invalid move");
                    }
                }
            }
            board[moveCells.get(0).x][moveCells.get(0).y].regularMove(moveCells.get(1));
        }
    }

    static void fightMove(ArrayList<Cell> killBill) throws Exception {
        for (int i = 0; i < killBill.size() - 1; i++) {
            board[killBill.get(i).x][killBill.get(i).y].fightMove(killBill.get(i + 1));
        }
        int n = killBill.size();
        if (board[killBill.get(n - 1).x][killBill.get(n - 1).y] != null && !board[killBill.get(n - 1).x][killBill.get(n - 1).y].checkFight()) {

        } else {
            throw new CheckersExceptions("invalid move");
        }
    }

    static void print() {
        white.sort(fComp);
        for (int i = 0; i < white.size(); i++) {
            if (white.get(i).alive) {
                if (i != white.size() - 1) {
                    white.get(i).printFigure();
                } else {
                    white.get(i).printFigure();
                }
            }
        }
        System.out.println();
        black.sort(fComp);
        for (int i = 0; i < black.size(); i++) {
            if (black.get(i).alive) {
                if (i != black.size() - 1) {

                    black.get(i).printFigure();

                } else {
                    black.get(i).printFigure();
                }
            }
        }
    }

    abstract void printFigure();
}

class Checker extends Figure {
    int lastXFight = 0;
    int lastYFight = 0;

    // 0 - white 1 - black
    Checker(char x, char y, int color) {
        this.x = Math.abs(8 - Integer.parseInt(String.valueOf(y)));
        this.y = (int) (x) >= 97 ? (int) (x) - 97 : (int) (x) - 65;
        this.color = color;
        board[this.x][this.y] = this;
        if (color == 0) {
            white.add(this);
        } else {
            black.add(this);
        }
    }

    Checker(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    boolean checkFight() {
        boolean res = false;
        if (!alive) {
            res = false;
        } else if (x + 2 < 8 && y - 2 >= 0 && board[x + 2][y - 2] == null && board[x + 1][y - 1] != null && board[x + 1][y - 1].color != color) {
            res = true;
        } else if (x - 2 >= 0 && y - 2 >= 0 && board[x - 2][y - 2] == null && board[x - 1][y - 1] != null && board[x - 1][y - 1].color != color) {
            res = true;
        } else if (x - 2 >= 0 && y + 2 < 8 && board[x - 2][y + 2] == null && board[x - 1][y + 1] != null && board[x - 1][y + 1].color != color) {
            res = true;
        } else if (x + 2 < 8 && y + 2 < 8 && board[x + 2][y + 2] == null && board[x + 1][y + 1] != null && board[x + 1][y + 1].color != color) {
            res = true;
        } else {
            res = false;
        }
        lastXFight = 0;
        lastYFight = 0;
        return res;
    }

    @Override
    void regularMove(Cell cell) throws Exception {
        if (board[cell.x][cell.y] == null && Math.abs(x - cell.x) == 1 && Math.abs(y - cell.y) == 1) {
            board[cell.x][cell.y] = board[x][y];
            board[x][y] = null;
            x = cell.x;
            y = cell.y;
            if (color == 0 && x == 0) { //Missis
                Missis m = new Missis(x, y, color, 0, 0);
                board[x][y] = m;
                white.add(m);
                white.remove(this);
            } else if (color == 1 && x == 7) {
                Missis m = new Missis(x, y, color, 0, 0);
                board[x][y] = m;
                black.add(m);
                black.remove(this);
            }
        } else {
            if (board[cell.x][cell.y] != null) {
                throw new CheckersExceptions("busy cell");
            } else if ((cell.x + cell.y) % 2 == 0) {
                throw new CheckersExceptions("white cell");
            } else {
                throw new CheckersExceptions("error");
            }
        }
    }

    @Override
    void fightMove(Cell cell) throws Exception {
        int x2 = (x + cell.x) / 2;
        int y2 = (y + cell.y) / 2;
        if (board[cell.x][cell.y] == null && board[x2][y2] != null && Math.abs(x - cell.x) == 2 &&
                Math.abs(y - cell.y) == 2 && board[x2][y2].color != color) {
            lastXFight = (cell.x - x) / 2;
            lastYFight = (cell.y - y) / 2;
            board[x2][y2].alive = false;
            board[x2][y2] = null;
            board[cell.x][cell.y] = board[x][y];
            board[x][y] = null;
            x = cell.x;
            y = cell.y;

            if (color == 0 && x == 0) { //Missis
                Missis m = new Missis(x, y, color, lastXFight, lastYFight);
                board[x][y] = m;
                white.add(m);
                white.remove(this);
            } else if (color == 1 && x == 7) {
                Missis m = new Missis(x, y, color, lastXFight, lastYFight);
                board[x][y] = m;
                black.add(m);
                black.remove(this);
            }

        } else {
            if (board[cell.x][cell.y] != null) {
                throw new CheckersExceptions("busy cell");
            } else if ((cell.x + cell.y) % 2 == 0) {
                throw new CheckersExceptions("white cell");
            } else {
                throw new CheckersExceptions("error");
            }
        }
    }

    @Override
    public int compare(Figure o1, Figure o2) {
        FigureComparator c = new FigureComparator();
        return c.compare(o1, o2);
    }

    @Override
    void printFigure() {
        System.out.print((char) (y + 97) + "" + Math.abs(x - 8) + " ");
    }
}

class Missis extends Figure {
    int lastXFight = 0;
    int lastYFight = 0;

    Missis(char x, char y, int color) {
        this.x = Math.abs(8 - Integer.parseInt(String.valueOf(y)));
        this.y = (int) (x) >= 97 ? (int) (x) - 97 : (int) (x) - 65;
        this.color = color;
        board[this.x][this.y] = this;
        if (color == 0) {
            white.add(this);
        } else {
            black.add(this);
        }
    }

    Missis(int x, int y, int color, int lastXFight, int lastYFight) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.lastXFight = lastXFight;
        this.lastYFight = lastYFight;
    }

    boolean checkFight() {

        if (!alive) {
            return false;
        }
        int[] pair = {-1, 1};
        for (int i : pair) {
            for (int j : pair) {
                for (int k = 1; k < 8; k++) {

                    int x1 = x + i * k;
                    int y1 = y + j * k;
                    if (x1 + i >= 0 && x1 + i < 8 && y1 + j >= 0 && y1 + j < 8) {
                        if (board[x1][y1] != null && board[x1 + i][y1 + j] == null && board[x1][y1].color != color
                                && ((lastXFight != -i) || (lastYFight != -j))) {
                            lastXFight = 0;
                            lastYFight = 0;
                            return true;
                        }
                    }
                }
            }
        }
        lastXFight = 0;
        lastYFight = 0;
        return false;
    }

    void regularMove(Cell cell) throws Exception {
        if (board[cell.x][cell.y] == null && Math.abs(x - cell.x) == Math.abs(y - cell.y)) {
            board[cell.x][cell.y] = board[x][y];
            board[x][y] = null;
            x = cell.x;
            y = cell.y;
        } else {
            if (board[cell.x][cell.y] != null) {
                throw new CheckersExceptions("busy cell");
            } else if ((cell.x + cell.y) % 2 == 0) {
                throw new CheckersExceptions("white cell");
            } else {
                throw new CheckersExceptions("error");
            }
        }
    }

    void fightMove(Cell cell) throws Exception {
        int dx = (cell.x - x) / Math.abs(cell.x - x);
        int dy = (cell.y - y) / Math.abs(cell.y - y);
        int x2 = -1;
        int y2 = -1;
        for (int i = 0; i < Math.abs(cell.x - x); i++) {
            if (board[x + i * dx][y + i * dy] != null && board[x + i * dx][y + i * dy].color != color) {
                x2 = x + i * dx;
                y2 = y + i * dy;
            }
        }

        if (x2 != -1 && y2 != -1 && board[cell.x][cell.y] == null && board[x2][y2] != null &&
                Math.abs(x - cell.x) == Math.abs(y - cell.y) && board[x2][y2].color != color) {
            board[x2][y2].alive = false;
            board[x2][y2] = null;
            board[cell.x][cell.y] = board[x][y];
            board[x][y] = null;
            x = cell.x;
            y = cell.y;
            lastXFight = dx;
            lastYFight = dy;
        } else {
            if (board[cell.x][cell.y] != null) {
                throw new CheckersExceptions("busy cell");
            } else if ((cell.x + cell.y) % 2 == 0) {
                throw new CheckersExceptions("white cell");
            } else {
                throw new CheckersExceptions("error");
            }
        }
    }

    @Override
    public int compare(Figure o1, Figure o2) {
        FigureComparator c = new FigureComparator();
        return c.compare(o1, o2);
    }

    @Override
    void printFigure() {
        System.out.print((char) (y + 65) + "" + Math.abs(x - 8) + " ");
    }
}


