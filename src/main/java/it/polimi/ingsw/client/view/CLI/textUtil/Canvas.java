package it.polimi.ingsw.client.view.CLI.textUtil;

import it.polimi.ingsw.client.view.CLI.CLI;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * A drawable canvas that can be displayed in the CLI
 */
public class Canvas {
    String[][] matrix;

    int width,height;
    public static Canvas withBorder(int width, int height){
        Canvas printer = new Canvas();
        printer.height = height;
        printer.width = width;

        String lineChars = Characters.VERT_DIVIDER.getString()+" ".repeat(width)+Characters.VERT_DIVIDER.getString();
        printer.matrix = Stream.generate(()-> generateLine(lineChars)).limit(height).toArray(String[][]::new);

        return printer;
    }

    public static Canvas fromText(int width, int height,String s){
        Canvas printer = Canvas.withBorder(width,height);
        int x = StringUtil.startCenterWritingX(s, width);
        int y = StringUtil.startCenterWritingY(s, height);
        printer.drawWithDefaultColor(x,y,s);
        return printer;
    }

    @NotNull
    private static String[] generateLine(String lineChars) {
        return lineChars.chars().mapToObj(c->String.valueOf((char) c)).toArray(String[]::new);
    }


    public void drawWithDefaultColor(int x, int y, String s){
        draw(x,y,s,Color.DEFAULT,Background.DEFAULT);
    }

    public void drawCenterX(int y, String s,Color c,Background b){
        draw(StringUtil.startCenterWritingX(s, width),y,s,c,b);
    }

    public void drawCenterXDefault(int y, String s){
        draw(StringUtil.startCenterWritingX(s, width),y,s,Color.DEFAULT,Background.DEFAULT);
    }

    public void draw(Drawable d){
        draw(d.getXPos(),d.getYPos(),d.getString(),d.getColor(),d.getBackground());
    }

    public void draw(DrawableList d){
        d.get().forEach(this::draw);
    }

    public void drawShifted(int shiftX,int shiftY,DrawableList l){
        l.get().stream().map(d->Drawable.shifted(shiftX,shiftY,d)).forEach(this::draw);
    }

    /**
     * Draws the string in the canvas at the given position,
     * the output in the CLI is similar to that of System.out.print(s) but with the text starting form the given x,y position.
     */
    public void draw(int x, int y, String s, Color c, Background b){
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
