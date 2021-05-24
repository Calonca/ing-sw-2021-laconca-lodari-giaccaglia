package it.polimi.ingsw.client.view.CLI.textUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class MatrixPrinter {
    char matrix [][];
    int width,height;
    public static MatrixPrinter withBorder(int width, int height){
        MatrixPrinter printer = new MatrixPrinter();
        printer.height = height;
        printer.width = width;
        String line = Characters.VERT_DIVIDER.getString()+" ".repeat(width)+Characters.VERT_DIVIDER.getString();
        printer.matrix = Stream.generate(line::toCharArray).limit(height).toArray(char[][]::new);
        return printer;
    }

    public void print(int x,int y,String s){
        int matX = x;
        int matY = y;
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            if (aChar=='\n')
            {
                matY+=1;
                matX = x;
            } else {
                char toPrint;
                if (matrix[matY][matX] == ' ')
                    toPrint = aChar;
                else toPrint = '*';
                matrix[matY][matX] = toPrint;
                matX += 1;
            }
        }
    }

    private boolean isLineEnd(int x){
        return x>=width;
    }

    @Override
    public String toString() {
        return Arrays.stream(matrix).map(String::valueOf).reduce("",(a, b)->a+b+"\n");
    }
}
