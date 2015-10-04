package  ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestCollections {

    private static final Predicate<Object> lenMod5Is3 = new Predicate<Object>() {

        @Override
        public Boolean apply(Object x) {
            return x.toString().length() % 5 == 3;
        }

    };

    private static final Function2<Integer, Integer, Integer> myOperation = new Function2<Integer, Integer, Integer>() {

        @Override
        public Integer apply(Integer x, Integer y) {
            if (x+y >= 5) {
                return (x+y) * 2;
            }
            else {
                return x + y;
            }
        }

    };

    @Test
    public void testMap() {

        List<Integer> myList = Arrays.asList(1, 12, 123);
        List<Boolean> realRes = Arrays.asList(false, false, true);
        assertTrue(realRes.equals(Collections.map(lenMod5Is3, myList)));

    }

    @Test
    public void testFilter() {

        List<Integer> myList = Arrays.asList(1, 212, 123);
        List<Integer> realRes = Arrays.asList(212,123);
        assertTrue(realRes.equals(Collections.filter(lenMod5Is3, myList)));

    }

    @Test
    public void testTakeWhile() {

        List<Integer> myList = Arrays.asList(999, 123, 12);
        List<Integer> realRes = Arrays.asList(999,123);
        assertTrue(realRes.equals(Collections.takeWhile(lenMod5Is3, myList)));

    }

    @Test
    public void testTakeUnless() {

        List<Integer> myList = Arrays.asList(12, 99, 123);
        List<Integer> realRes = Arrays.asList(12,99);
        assertTrue(realRes.equals(Collections.takeUnless(lenMod5Is3, myList)));

    }

    @Test
    public void testFoldlAndFoldr() {

        List<Integer> myList = Arrays.asList(1,1,4);
        assertTrue(Collections.foldl(myOperation, 1, myList) == 14);
        assertTrue(Collections.foldr(myOperation, 1, myList) == 46);
    }


}