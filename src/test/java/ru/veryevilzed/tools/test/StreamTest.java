package ru.veryevilzed.tools.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    public void streamFilterTest() {

        List<Integer> list = Arrays.asList(0,1,2,3,4,5);

        Stream<Integer> stream = list.stream();

        List<Predicate<Integer>> preds = new ArrayList<>();
        preds.add((i) -> i % 2 == 0);
        preds.add((i) -> i > 3);


        for(Predicate<Integer> p : preds)
            stream = stream.filter(p);


        Integer[] array = stream.toArray(Integer[]::new);
        Assert.assertArrayEquals(array, new Integer[] {4} );
    }

    @Test
    public void streamMapTest() {

        List<Integer> list = Arrays.asList(0,1,2);

        Stream<Integer> stream = list.stream();

        List<Function<? super Integer, ? extends Integer>> funcs = new ArrayList<>();
        funcs.add((i) -> i * 2);
        funcs.add((i) -> i - 1);


        for(Function<? super Integer, ? extends Integer> f : funcs)
            stream = stream.map(f);




        Integer[] array = stream.toArray(Integer[]::new);
        Assert.assertArrayEquals(array, new Integer[] {-1, 1, 3} );



    }


}
