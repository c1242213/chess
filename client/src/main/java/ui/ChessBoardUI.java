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
        out.print(SET_TEXT_COLOR_BLACK);
    }

    public static void drawBoard(PrintStream out) {

        for(int i = 0; i <= 16; i++) {
            if(i%2 == 1) {
                border(out);
                out.println();
                checkeredRowWhite(out);
                out.println();
                checkeredRowWhite(out);
            }else{
                if(i == 8){
                    out.println();
                    border(out);
                    out.println();
                    border(out);
                }
                else {
                    border(out);
                    out.println();
                    checkeredRowBlack(out);
                    out.println();
                    checkeredRowBlack(out);
                }
            }

        }
        out.println();



    }
    public static void border(PrintStream out){
        for(int i = 0; i< 18; i++){
            out.print(SET_BG_COLOR_GREEN);
            System.out.print(EMPTY.repeat(1));
        }
        out.print(SET_BG_COLOR_DARK_GREY);

    }

//    public static void checkeredRow(PrintStream out, int i ) {
//        for (int row = 0; row < 6; row++) {
//
//            if (row == 0 || row == 5) {
//                out.print(SET_BG_COLOR_GREEN);
//                System.out.print(EMPTY.repeat(1));
//            } else {
//                if(i%2 == 0) {
//                        out.print(SET_BG_COLOR_BLACK);
//                        System.out.print(EMPTY.repeat(2));
//                        out.print(SET_BG_COLOR_WHITE);
//                        System.out.print(EMPTY.repeat(2));
//                        out.println();
//                }
//                else {
//                        out.print(SET_BG_COLOR_WHITE);
//                        System.out.print(EMPTY.repeat(2));
//                        out.print(SET_BG_COLOR_BLACK);
//
//                    out.println();
//                }
//                System.out.print(EMPTY.repeat(2));
//                out.println();
//            }
//
//        }
//        out.print(SET_BG_COLOR_DARK_GREY);
//
//    }
    public static void checkeredRowBlack(PrintStream out){
        for (int row = 0; row < 6; row++) {

            if (row == 0 || row == 5) {
                out.print(SET_BG_COLOR_GREEN);
                System.out.print(EMPTY.repeat(1));
            }else{
                out.print(SET_BG_COLOR_BLACK);
                System.out.print(EMPTY.repeat(2));
                out.print(SET_BG_COLOR_WHITE);
                System.out.print(EMPTY.repeat(2));
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY);

    }
    public static void checkeredRowWhite(PrintStream out){
        for (int row = 0; row < 6; row++) {

            if (row == 0 || row == 5) {
                out.print(SET_BG_COLOR_GREEN);
                System.out.print(EMPTY.repeat(1));
            }else{
                out.print(SET_BG_COLOR_WHITE);
                System.out.print(EMPTY.repeat(2));
                out.print(SET_BG_COLOR_BLACK);
                System.out.print(EMPTY.repeat(2));
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY);

    }
}
