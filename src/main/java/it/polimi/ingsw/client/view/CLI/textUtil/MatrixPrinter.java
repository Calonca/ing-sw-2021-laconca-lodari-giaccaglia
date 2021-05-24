package it.polimi.ingsw.client.view.CLI.textUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

public class MatrixPrinter {
    String[][] matrix;

    int width,height;
    public static MatrixPrinter withBorder(int width, int height){
        MatrixPrinter printer = new MatrixPrinter();
        printer.height = height;
        printer.width = width;

        String lineChars = Characters.VERT_DIVIDER.getString()+" ".repeat(width)+Characters.VERT_DIVIDER.getString();
        printer.matrix = Stream.generate(()-> generateLine(lineChars)).limit(height).toArray(String[][]::new);

        return printer;
    }

    @NotNull
    private static String[] generateLine(String lineChars) {
        return lineChars.chars().mapToObj(c->String.valueOf((char) c)).toArray(String[]::new);
    }


    public void printWhiteText(int x, int y, String s){
        printWithColorAndBackground(x,y,s,Color.DEFAULT,Background.DEFAULT);
    }

    public void printWithColorAndBackground(int x, int y, String s, Color c, Background b){
        int matX = x;
        int matY = y;
        char[] chars = s.toCharArray();
        for (int i=0;i<chars.length;i++) {
            char aChar = chars[i];
            boolean defaultColorAndBack = !(c.equals(Color.DEFAULT) && b.equals(Background.DEFAULT));
            int lastLineWidth = 0;
            if (aChar=='\n')//Go to next line
            {
                lastLineWidth = matX-1;
                matY+=1;
                matX = x;
            } else {
                char toPrint;
                if (matrix[matY][matX].isBlank())//Check if writing on non-empty space, for debugging
                    toPrint = aChar;
                else
                    toPrint = '*';
                if (matX==x && defaultColorAndBack)//Start of the printed line
                    matrix[matY][matX] = Color.startColorStringBackground(String.valueOf(toPrint),c,b);
                else
                    matrix[matY][matX] = String.valueOf(toPrint);
                matX += 1;
            }
            if (defaultColorAndBack)//Resets color
                if (aChar == '\n') {
                    matrix[matY-1][lastLineWidth] = Color.endColorString(matrix[matY-1][lastLineWidth]);
                } else if (i == chars.length - 1) {
                    matrix[matY][matX-1] = Color.endColorString(matrix[matY][matX-1]);
                }

        }
    }

    private boolean isLineEnd(int x){
        return x>=width;
    }

    @Override
    public String toString() {
        return Arrays.stream(matrix).map(line-> Arrays.stream(line).reduce("",(a, b)->a+b)).reduce("",(a, b)->a+b+"\n");
    }
}
