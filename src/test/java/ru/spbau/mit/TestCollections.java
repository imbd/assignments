package  ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestCollections {

    private static final Predicate<Integer> MoreThanNull = new Predicate<Integer>() {

        @Override
        public Boolean apply(Integer x) {
            return x > 0;
        }

    };

    private static final Function2<Integer, Integer, Integer> Substraction = new Function2<Integer, Integer, Integer>() {

        @Override
        public Integer apply(Integer x, Integer y) {
            return x - y;
        }

    };

    @Test
    public void testMap() {

        List<Integer> myList = Arrays.asList(0, -12, 123);
        List<Boolean> realRes = Arrays.asList(false, false, true);
        assertEquals(realRes, Collections.map(MoreThanNull, myList));

    }

    @Test
    public void testFilter() {

        List<Integer> myList = Arrays.asList(-1, 212, 123);
        List<Integer> realRes = Arrays.asList(212, 123);
        assertEquals(realRes, Collections.filter(MoreThanNull, myList));

    }

    @Test
    public void testTakeWhile() {

        List<Integer> myList = Arrays.asList(999, 123, -12);
        List<Integer> realRes = Arrays.asList(999, 123);
        assertEquals(realRes, Collections.takeWhile(MoreThanNull, myList));

    }

    @Test
    public void testTakeUnless() {

        List<Integer> myList = Arrays.asList(-12, -99, 123);
        List<Integer> realRes = Arrays.asList(-12, -99);
        assertEquals(realRes, Collections.takeUnless(MoreThanNull, myList));

    }

    @Test
    public void testFoldlAndFoldr() {

        List<Integer> myList = Arrays.asList(1,1,4);
        assertTrue(Collections.foldl(Substraction, 1, myList) == -5);
        assertTrue(Collections.foldr(Substraction, 1, myList) == 3);
    }


}