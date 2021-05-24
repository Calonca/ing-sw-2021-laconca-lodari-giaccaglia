package it.polimi.ingsw.client.view.CLI.textUtil;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class StringUtil {
    public static String stringUntilReachingSize(String s, int size){
        return Stream.concat(s.chars().mapToObj(c->(char)c),Stream.generate(()->' ')).limit(size).collect(Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString));
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
}
