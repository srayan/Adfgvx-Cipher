import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Adfgvx {

    private static final char[] morse = { 'A', 'D', 'F', 'G', 'V', 'X' };

    private String key;

    private char[][] grid;

    private Column[] col;

    private Column[] colAlpha;

    public Adfgvx(String key) {
        ArrayList<Character> a1 = new ArrayList<Character>();
        for (char c = 'A'; c <= 'Z'; c++)
            a1.add(c);
        for (char c = '0'; c <= '9'; c++)
            a1.add(c);

        Random ran = new Random();

        grid = new char[morse.length][morse.length];

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                int index = ran.nextInt(a1.size());
                grid[i][j] = a1.remove(index);
            }

        setKey(key);

    }

    public void setKey(String key) {
        if (key == null) {
            this.key = "";
            return;
        }

        char[] digit = key.toCharArray();
        int len = digit.length;

        for (int i = 0; i < len - 1; i++)
            for (int j = i + 1; j < len; j++)
                if (digit[i] == digit[j]) {
                    this.key = "";
                    return;
                }
        this.key = key;

        col = new Column[len];
        colAlpha = new Column[len];
        for (int i = 0; i < len; i++) {
            col[i] = new Column(digit[i]);
            colAlpha[i] = col[i];
        }

        Arrays.sort(colAlpha);
    }

    public String encode(String clear) {
        char[] digit = msgToProcess(clear, true);
        if (digit.length == 0)
            return "";

        prepareColumns(digit.length * 2);

        int k = 0;
        for (char c : digit) {
            Point p = findPos(c);
            col[k++].add(morse[p.x]);
            k %= col.length;
            col[k++].add(morse[p.y]);
            k %= col.length;
        }

        StringBuilder sb = new StringBuilder(digit.length * 2);
        for (Column c : colAlpha) {
            sb.append(c.toString());
        }

        digit = sb.toString().toCharArray();
        sb = new StringBuilder(digit.length + (digit.length / 2));

        sb.append(digit[0]);
        sb.append(digit[1]);

        for (int i = 2; i < digit.length; i += 2) {
            sb.append(' ');
            sb.append(digit[i]);
            sb.append(digit[i + 1]);
        }

        return sb.toString();
    }

    public String decode(String coded) {
        char[] digit = msgToProcess(coded, false);
        if (digit.length == 0)
            return "";

        prepareColumns(digit.length);

        int k = 0;
        for (Column c : colAlpha) {
            int size = c.getSize();
            for (int i = 0; i < size; i++)
                c.add(digit[k++]);
        }

        StringBuilder sb = new StringBuilder(digit.length);
        int size = col[0].getSize();
        for (int row = 0; row < size; row++) {
            for (Column c : col) {
                if (row >= c.getSize())
                    break;
                sb.append(c.getChar(row));
            }
        }

        digit = sb.toString().toCharArray();
        char[] decoded = new char[digit.length / 2];
        for (int i = 0; i < digit.length; i += 2) {
            int x = 0;
            for (; x < morse.length; x++) {
                if (digit[i] == morse[x])
                    break;
            }
            int y = 0;
            for (; y < morse.length; y++) {
                if (digit[i + 1] == morse[y])
                    break;
            }
            decoded[i / 2] = grid[x][y];
        }
        return new String(decoded).toLowerCase();
    }

    private char[] msgToProcess(String str, boolean coding) {
        if (str == null)
            return new char[0];
        if (key.length() == 0)
            return new char[0];
        StringBuilder sb = new StringBuilder(key.length());
        char[] digit = str.toUpperCase().toCharArray();
        for (char c : digit) {
            if (coding) {
                if ((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
                    sb.append(c);
            } else {
                for (char m : morse) {
                    if (m == c) {
                        sb.append(c);
                        break;
                    }
                }
            }
        }
        digit = sb.toString().toCharArray();
        if (digit.length == 0)
            return digit;
        if (!coding) {
            if (digit.length % 2 != 0)
                return new char[0];
        }
        return digit;
    }

    private void prepareColumns(int len) {
        int nbPerCol = len / col.length;
        int[] nb = new int[col.length];
        for (int i = 0; i < col.length; i++)
            nb[i] = nbPerCol;
        int reminder = len - (col.length * nbPerCol);
        for (int i = 0; i < reminder; i++)
            nb[i]++;

        for (int i = 0; i < col.length; i++) {
            col[i].setSize(nb[i]);
        }
    }

    private Point findPos(char c) {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                if (c == grid[x][y])
                    return new Point(x, y);
            }
        }
        throw new IllegalStateException("Character " + c + " not found in Grid");
    }

    public void dumpGrid() {
        System.out.println("      GRID");
        System.out.println();
        System.out.print("    ");
        for (int i = 0; i < morse.length; i++)
            System.out.print(" " + morse[i]);
        System.out.println();
        System.out.print("  +--");
        for (int i = 0; i < morse.length; i++)
            System.out.print("--");
        System.out.println();
        for (int i = 0; i < morse.length; i++) {
            System.out.print(morse[i] + " | ");
            for (int j = 0; j < morse.length; j++) {
                System.out.print(" " + grid[i][j]);
            }
            System.out.println();
        }
    }

    public char[][] getGrid() {
        return grid;
    }

    public char[] getMorse() {
        return morse;
    }

    public static void main(String[] args) {
        Adfgvx adfgvx = new Adfgvx("JOHN");
        adfgvx.dumpGrid();
        String message = "This is the message to encode";
        String coded = adfgvx.encode(message);
        System.out.println("Original: " + message);
        System.out.println("   Coded:   " + coded);
        System.out.println(" Decoded: " + adfgvx.decode(coded));
        Scanner scan = new Scanner(System.in);
        System.out.print("\n    Enter a Key: ");
        String key = scan.nextLine();
        Adfgvx user = new Adfgvx(key);
        System.out.print("Enter a message: ");
        String msg = scan.nextLine();
        user.dumpGrid();
        System.out.println("  Key is: " + user.key);
        System.out.println("Original: " + msg);
        String cypher = user.encode(msg);
        System.out.println("   Coded: " + cypher);
        System.out.println(" Decoded: " + user.decode(cypher));
        scan.close();
    }

    private class Column implements Comparable<Column> {

        private char header;
        private char[] letters;
        private int index;

        Column(char header) {
            this.header = header;
        }

        void setSize(int size) {
            letters = new char[size];
            index = 0;
        }

        int getSize() {
            return letters.length;
        }

        void add(char c) {
            letters[index++] = c;
        }

        char getChar(int n) {
            return letters[n];
        }

        public String toString() {
            return new String(letters);
        }

        public int compareTo(Column other) {
            return header - other.header;
        }
    }

}