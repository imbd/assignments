package  ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFunction1 {

    @Test
    public void testCompose() {

        Function1<Integer, Boolean> isEven = new Function1<Integer, Boolean>() {

            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }

        };

        Function1<String, Integer> stringToInt = new Function1<String, Integer>() {

            @Override
            public Integer apply(String x) {
                return Integer.valueOf(x);
            }

        };

        Function1<String,Boolean> myCompose = stringToInt.compose(isEven);

        assertTrue(stringToInt.apply("54321").equals(54321));
        assertTrue(isEven.apply(34));
        assertFalse(isEven.apply(33));

        assertTrue(myCompose.apply("100"));
        assertFalse(myCompose.apply("101"));

    }
}