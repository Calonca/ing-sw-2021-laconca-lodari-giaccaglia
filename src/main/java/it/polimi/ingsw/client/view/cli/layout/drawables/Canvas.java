package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Characters;
import it.polimi.ingsw.client.view.cli.textutil.Color;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A drawable canvas that can be displayed in the CLI
 */
public class Canvas {
    String[][] matrix;
    UUID[][] pointTracing;
    Map<UUID, Drawable> lists;
    static final boolean IS_DEBUGGING =false;

    int width;
    int height;
    public static Canvas withBorder(int width, int height){
        Canvas printer = new Canvas();
        printer.height = height;
        printer.width = width;

        printer.resetDrawing();
        printer.lists=new HashMap<>();

        return printer;
    }

    public void resetDrawing(){
        String lineChars = Characters.VERT_DIVIDER.getString()+" ".repeat(width-2)+Characters.VERT_DIVIDER.getString();
        matrix = Stream.generate(()-> generateLine(lineChars)).limit(height).toArray(String[][]::new);

        pointTracing = new UUID[height][width];
    }

    private static String[] generateLine(String lineChars) {
        return lineChars.chars().mapToObj(c->String.valueOf((char) c)).toArray(String[]::new);
    }

    public void addDrawable(Drawable dwl){
        lists.put(dwl.getId(),dwl);
    }

    private void draw(){
        lists.values().forEach(this::drawList);
    }

    private void drawList(Drawable dwl){
        //Adding selected color if present
        int minX;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        if (dwl.isSelected()){
            minX=dwl.getMinX();
            minY=dwl.getMinY();
            maxX=minX+dwl.getWidth();
            maxY=minY+dwl.getHeight();
            IntStream.range(minY,maxY).forEach(y->{
                if (get(y,minX).isBlank())
                    set(y, minX,Color.startColorStringBackground(get(y,minX),Color.BRIGHT_WHITE,Background.ANSI_BRIGHT_BLACK_BACKGROUND));
            });
        }

        //Drawing
        dwl.get().forEach(d-> draw(d,dwl));

        //Resetting selected color
        if (dwl.isSelected()){
            int finalMaxX = maxX-1;
            IntStream.range(minY,maxY).forEach(y-> set(y, finalMaxX,Color.endColorString(get(y,finalMaxX))));
        }
    }

    /**
     * Draws the string in the canvas at the given position,
     * the output in the CLI is similar to that of System.out.print(s) but with the text starting form the given x,y position.
     */
    private void draw(DrawableLine d, Drawable dwl){
        int matX = d.getXPos();
        int matY = d.getYPos();
        Color c = Drawable.getColorWithSelected(dwl,d);
        Background b = Drawable.getBackWithSelected(dwl,d);
        char[] chars = d.getString().toCharArray();
        for (int i=0;i<chars.length;i++) {

            char aChar = chars[i];
            boolean notDefaultColorAndBack = !(c.equals(Color.DEFAULT) && b.equals(Background.DEFAULT));
            int lastLineWidth = 0;
            if (aChar=='\n')//Go to next line
            {
                lastLineWidth = matX-1;
                matY+=1;
                matX = d.getXPos();
            } else {
                char toPrint;
                if (get(matY,matX).isBlank()||matX==dwl.getMinX())//Check if writing on non-empty space, for debugging
                    toPrint = aChar;
                else
                    toPrint = '*';
                //Drawing
                if (matX==d.getXPos() )//Start of the printed line
                    set(matY,matX,Color.startColorStringBackground(String.valueOf(toPrint),c,b));
                else
                    set(matY,matX,String.valueOf(toPrint));
                setPointTracing(matY,matX,dwl.getId());
                matX += 1;
            }
            //Resets color
            if (aChar == '\n' ) {
                if (notDefaultColorAndBack)
                    set(matY-1,lastLineWidth,Color.endColorString(get(matY-1,lastLineWidth)));
            } else if (i == chars.length - 1) {
                if (dwl.isSelected() && matX<dwl.getMinX()+dwl.getWidth())
                    set(matY,matX-1,get(matY,matX-1)+Color.startColorStringBackground("",Color.BRIGHT_WHITE,Background.ANSI_BRIGHT_BLACK_BACKGROUND));
                else
                    set(matY,matX-1,Color.endColorString(get(matY,matX-1)));
            } else if (matX==dwl.getMinX()+dwl.getWidth()) {
                set(matY,matX-1,Color.endColorString(get(matY,matX-1)));
            }

        }
    }

    private boolean outOfBound(int y,int x){
        return (y>matrix.length-1||x>matrix[0].length-1||x<0||y<0);
    }

    private String get(int y, int x){
        if (outOfBound(y,x))
        {
            return "";
        } else return matrix[y][x];
    }

    private void set(int y, int x,String value){
        if (!outOfBound(y,x))
        {
            matrix[y][x]=value;
        }
    }

    private void setPointTracing(int y, int x,UUID id){
        if (!outOfBound(y,x))
        {
            pointTracing[y][x]=id;
        }
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        resetDrawing();
        draw();
        if (!IS_DEBUGGING)
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
