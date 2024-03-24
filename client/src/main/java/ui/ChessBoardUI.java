package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawBoard(out);

        out.print(SET_BG_COLOR_DARK_GREY);
    }

    public static void drawBoard(PrintStream out) {
        letterBorder(out, new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "});
        rowChecker(out, new String[]{" 1 ", " 2 ",  " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "});
        out.println();
        letterBorder(out, new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "});
        out.println();
        for(int i = 0; i < 10; i++){
            out.print(SET_BG_COLOR_BLACK);
            System.out.print(EMPTY.repeat(1));

        }
        out.print(SET_BG_COLOR_DARK_GREY);
        out.println();
        letterBorder(out, new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "});
        rowChecker(out, new String[] {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "});
        out.println();
        letterBorder(out, new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "});
    }
    public static void rowChecker(PrintStream out, String[] string){
        int counter = 0;
        for (int i = 0; i <= 7; i++) {
            if (i % 2 == 1) {
                out.println();
                checkeredRowWhite(out, counter, string);
            } else {
                out.println();
                checkeredRowBlack(out, counter, string);
            }
            counter++;
        }

    }

    public static void letterBorder(PrintStream out, String[] string){
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(EMPTY.repeat(1));
        for(int i = 0; i< 8; i++){
            out.print(SET_BG_COLOR_GREEN);
            System.out.print(string[i]);
        }
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(EMPTY.repeat(1));
        out.print(SET_BG_COLOR_DARK_GREY);

    }

    public static void checkeredRowBlack(PrintStream out, int counter, String[] string){
        printRows(out, SET_BG_COLOR_BLACK, SET_BG_COLOR_WHITE, counter, string);

    }

    public static void checkeredRowWhite(PrintStream out, int counter, String[] string){
        printRows(out, SET_BG_COLOR_WHITE, SET_BG_COLOR_BLACK, counter, string);

    }

    private static void printRows(PrintStream out, String setBgColorWhite, String setBgColorBlack, int counter, String[] string) {
        for (int row = 0; row < 4; row++) {
            if(row == 0){
                numberBorder(out, counter, string);
            }
            out.print(setBgColorBlack);
            System.out.print(EMPTY.repeat(1));
            out.print(setBgColorWhite);
            System.out.print(EMPTY.repeat(1));
        }
        numberBorder(out, counter, string);
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void numberBorder(PrintStream out, int counter, String[] string){
        out.print(SET_BG_COLOR_GREEN);
        System.out.print(string[counter]);
    }
}
