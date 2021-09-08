package it.polimi.ingsw.network.util;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util
{

    private Util(){}
    /**
     * Returns the sum of two arrays a and b, an array of the given length<br>
     * If the dimensions differ it will sum with zero.<br>
     * If any array is shorten then the given length zeroes will be added until reaching the given length.<br>
     * If any array is longer than the given length the return array will be cut to length.<br>
     * @param a the first array, != null
     * @param b the second array, != null
     * @param len the requested length for the sum array, positive
     * @return sum of two arrays
     */
    public static int[] sumArray(final int[] a,final int[] b,int len){
        final int[] aa = IntStream.concat(Arrays.stream(a),IntStream.generate(()->0)).limit(len).toArray();
        final int[] bb = IntStream.concat(Arrays.stream(b),IntStream.generate(()->0)).limit(len).toArray();
        return IntStream.range(0,len).map(i->aa[i]+ bb[i]).toArray();
    }

    public static int resourcesToChooseOnSetup(int playerNumber){
        playerNumber++;
        int resourcesFromPlayerNumber = (11 % playerNumber);
        return (playerNumber == 3 || playerNumber == 4) ?
                resourcesFromPlayerNumber - 1 :
                resourcesFromPlayerNumber;
    }

    public static int initialFaithPoints(int playerNumber){
        int resources = resourcesToChooseOnSetup(playerNumber);
        playerNumber++;

        if(playerNumber%2 == 0)
            return resources-1;
        else
            return resources;
    }

    public static <K, V> Optional<K> getKeyByValue(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Entry::getKey)
                .findFirst();
    }

    public static <K, V> List<Map<K,V>> getListOfSmallerMaps(Map<K,V> map, int batchSize) {

    //collector for obtaining a list of maps of a specified size
    Collector<Entry<K,V>, ?, List<Map<K, V>>> batchesCollector = unorderedBatches(batchSize,
                    Collectors.toMap(Entry::getKey, Entry::getValue), Collectors.toList());

        return map.entrySet().stream().collect(batchesCollector);
}


    //intermediate transformation for every batch, downstream collector is applied after batchCollector
    public static <T, C, A, B, R> Collector<T, ?, R> unorderedBatches(int batchSize,
                                                                       Collector<T, C, B> batchCollector, //collects elements in a certain way
                                                                       Collector<B, A, R> downstream) { //collects elements in a certain way after batchCollector
        return unorderedBatches(
                        batchSize,
                        Collectors.mapping(list -> list.stream().collect(batchCollector), downstream)
        ); //downstream is another collector which will accept mapped values
    }

    public static <T, A, R> Collector<T, ?, R> unorderedBatches(int batchSize,
                                                               Collector<List<T>, A, R> downstream) {
    class Acc {
        List<T> cur = new ArrayList<>();
        A accumulator = downstream.supplier().get(); //the result of the supplier of downstream is stored
    }


    BiConsumer<Acc, T> accumulator = (acc, t) -> {
        //adds t to the cur list
        acc.cur.add(t);

        if(acc.cur.size() == batchSize) {
            downstream.accumulator().accept(acc.accumulator, acc.cur);
            acc.cur = new ArrayList<>();
        }
    };


    return Collector.of(

            Acc::new, //Supplier

            accumulator, //BiConsumer

            (acc1, acc2) -> {  //BinaryOperator
                acc1.accumulator = downstream.combiner().apply(acc1.accumulator, acc2.accumulator);
                for(T t : acc2.cur) accumulator.accept(acc1, t);
                return acc1;
            },

            acc -> { //Function
                if(!acc.cur.isEmpty())
                    downstream.accumulator().accept(acc.accumulator, acc.cur);
                return downstream.finisher().apply(acc.accumulator);
            },

            Collector.Characteristics.UNORDERED

    );
}


}