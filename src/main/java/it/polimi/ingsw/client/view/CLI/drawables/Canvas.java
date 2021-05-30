package it.polimi.ingsw.client.view.CLI.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A drawable canvas that can be displayed in the CLI
 */
public class Canvas {
    String[][] matrix;
    UUID[][] pointTracing;
    Map<UUID, Drawable> lists;
    boolean isDebugging=false;

    int width,height;
    public static Canvas withBorder(int width, int height){
        Canvas printer = new Canvas();
        printer.height = height;
        printer.width = width;

        printer.resetDrawing();
        printer.lists=new HashMap<>();

        return printer;
    }

    public void resetDrawing(){
        String lineChars = Characters.VERT_DIVIDER.getString()+" ".repeat(width)+Characters.VERT_DIVIDER.getString();
        matrix = Stream.generate(()-> generateLine(lineChars)).limit(height).toArray(String[][]::new);

        pointTracing = new UUID[height][width];

    }

    public static Canvas fromText(int width, int height,String s){
        Canvas printer = Canvas.withBorder(width,height);
        int x = StringUtil.startCenterWritingX(s, width);
        int y = StringUtil.startCenterWritingY(s, height);
        Drawable dwl = new Drawable();
        dwl.add(new DrawableLine(x,y,s));
        printer.addDrawableList(dwl);
        return printer;
    }

    @NotNull
    private static String[] generateLine(String lineChars) {
        return lineChars.chars().mapToObj(c->String.valueOf((char) c)).toArray(String[]::new);
    }

    public void addDrawableList(Drawable dwl){
        lists.put(dwl.getId(),dwl);
    }

    private void draw(){
        lists.values().forEach(this::drawList);
    }

    private void drawList(Drawable dwl){
        dwl.get().forEach(d-> draw(d,dwl));
    }

    /**
     * Draws the string in the canvas at the given position,
     * the output in the CLI is similar to that of System.out.print(s) but with the text starting form the given x,y position.
     */
    private void draw(DrawableLine d, Drawable dwl){
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
                pointTracing[matY][matX]=dwl.getId();
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

    public void setDebugging(boolean debugging) {
        isDebugging = debugging;
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

    public List<Drawable> getLists(List<Pair<Integer,Integer>> cordYX){
        return cordYX.stream().map(yx->pointTracing[yx.getKey()][yx.getValue()]).distinct().filter(Objects::nonNull)
                .map(id->lists.get(id)).collect(Collectors.toList());
    }

    public List<Drawable> getListsInRow(int y){
        List<Pair<Integer,Integer>> cordYX = IntStream.range(1,width-1)
                .mapToObj(x->new Pair<>(y,x)).collect(Collectors.toList());
        return getLists(cordYX);
    }

    @Override
    public String toString() {
        resetDrawing();
        draw();
        if (!isDebugging)
            return Arrays.stream(matrix).map(line-> Arrays.stream(line).reduce("",(a, b)->a+b)).reduce("",(a, b)->a+b+"\n");
        else return Arrays.stream(pointTracing).map(line-> Arrays.stream(line).map(this::idToString)
                .reduce("",(a, b)->a+b))
            .reduce("",(a, b)->a +b+"\n");
    }

    private String idToString(UUID id)
    {
        if (id==null)
            return "░";
        else return "█";
    }
}
