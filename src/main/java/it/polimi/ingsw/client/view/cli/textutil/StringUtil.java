package it.polimi.ingsw.client.view.cli.textutil;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class StringUtil {
    public static String untilReachingSize(String s, int size){
        return Stream.concat(s.chars().mapToObj(c->(char)c),Stream.generate(()->' ')).limit(size).collect(Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString));
    }

    public static String untilReachingSize(int i, int size){
        return untilReachingSize(String.valueOf(i),size);
    }

    public static String twoDigitsRight(int i){
        return i>9? String.valueOf(i) :" "+i;
    }

    public static String emptyLineWithBorder(int size){
        return "║"+spaces(size-2)+"║";
    }

    public static String spaces(int numOf){
        return " ".repeat(numOf);
    }

    public static int maxWidth(String s){
        return Arrays.stream(s.split("\n")).mapToInt(String::length).max().orElse(0);
    }

    public static int maxHeight(String s){
        return s.split("\n").length;
    }

    public static int startCenterWritingX(String s,int canvasWidth){
        return (canvasWidth-maxWidth(s))/2;
    }

    public static int startCenterWritingY(String s,int canvasHeight) {
        return (canvasHeight - maxHeight(s))/2;
    }


}
