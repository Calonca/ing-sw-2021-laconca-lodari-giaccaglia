package it.polimi.ingsw.client.view.CLI.textUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A drawable canvas that can be displayed in the CLI
 */
public class Canvas {
    String[][] matrix;
    UUID[][] pointTracing;
    Map<UUID,DrawableList> lists;

    int width,height;
    public static Canvas withBorder(int width, int height){
        Canvas printer = new Canvas();
        printer.height = height;
        printer.width = width;

        String lineChars = Characters.VERT_DIVIDER.getString()+" ".repeat(width)+Characters.VERT_DIVIDER.getString();
        printer.matrix = Stream.generate(()-> generateLine(lineChars)).limit(height).toArray(String[][]::new);

        printer.pointTracing = new UUID[height][width];
        printer.lists=new HashMap<>();

        return printer;
    }

    public static Canvas fromText(int width, int height,String s){
        Canvas printer = Canvas.withBorder(width,height);
        int x = StringUtil.startCenterWritingX(s, width);
        int y = StringUtil.startCenterWritingY(s, height);
        DrawableList dwl = new DrawableList();
        dwl.add(new Drawable(x,y,s));
        printer.addDrawableList(dwl);
        return printer;
    }

    @NotNull
    private static String[] generateLine(String lineChars) {
        return lineChars.chars().mapToObj(c->String.valueOf((char) c)).toArray(String[]::new);
    }

    public void addDrawableList(DrawableList dwl){
        lists.put(dwl.getId(),dwl);
    }

    private void draw(){
        lists.values().forEach(this::drawList);
    }

    private void drawList(DrawableList dwl){
        dwl.get().forEach(d-> draw(d,dwl));
    }

    /**
     * Draws the string in the canvas at the given position,
     * the output in the CLI is similar to that of System.out.print(s) but with the text starting form the given x,y position.
     */
    private void draw(Drawable d,DrawableList dwl){
        int matX = d.getXPos();
        int matY = d.getYPos();
        Color c = d.getColor();
        Background b = d.getBackground();
        char[] chars = d.getString().toCharArray();
        for (int i=0;i<chars.length;i++) {
            char aChar = chars[i];
            boolean defaultColorAndBack = !(c.equals(Color.DEFAULT) && b.equals(Background.DEFAULT));
            int lastLineWidth = 0;
            if (aChar=='\n')//Go to next line
            {
                lastLineWidth = matX-1;
                matY+=1;
                matX = d.getXPos();
            } else {
                char toPrint;
                if (matrix[matY][matX].isBlank())//Check if writing on non-empty space, for debugging
                    toPrint = aChar;
                else
                    toPrint = '*';
                //Drawing
                if (matX==d.getXPos() && defaultColorAndBack)//Start of the printed line
                    matrix[matY][matX] = Color.startColorStringBackground(String.valueOf(toPrint),c,b);
                else
                    matrix[matY][matX] = String.valueOf(toPrint);
                pointTracing[matY][matY]=dwl.getId();
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        draw();
        return Arrays.stream(matrix).map(line-> Arrays.stream(line).reduce("",(a, b)->a+b)).reduce("",(a, b)->a+b+"\n");
    }
}
