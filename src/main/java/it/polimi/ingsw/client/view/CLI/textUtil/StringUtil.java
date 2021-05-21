package it.polimi.ingsw.client.view.CLI.textUtil;

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
}
