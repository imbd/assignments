package  ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestPredicate {

    private static final Predicate <Integer> isEven = new Predicate<Integer>() {

        @Override
        public Boolean apply(Integer x) {
            return x % 2 == 0;
        }

    };

    private static final Predicate <Object> lenNotTwo = new Predicate<Object>() {

        @Override
        public Boolean apply(Object x) {
            return x.toString().length() != 2;
        }

    };


    @Test
    public void testAlwaysTrue() {

        assertTrue(Predicate.ALWAYS_TRUE.apply(5));
        assertTrue(Predicate.ALWAYS_TRUE.apply("abc"));

    }

    @Test
    public void testAlwaysFalse() {

        assertFalse(Predicate.ALWAYS_FALSE.apply(5));
        assertFalse(Predicate.ALWAYS_FALSE.apply("abc"));

    }

    @Test
    public void testOr() {

        Predicate<Integer> or = isEven.or(lenNotTwo);
        assertTrue(or.apply(5));
        assertTrue(or.apply(24));
        assertTrue(or.apply(6));
        assertFalse(or.apply(25));

    }

    @Test
    public void testAnd() {

        Predicate<Integer> and = isEven.and(lenNotTwo);

        assertTrue(and.apply(4));
        assertFalse(and.apply(24));
        assertFalse(and.apply(3));
        assertFalse(and.apply(23));

    }

    @Test
    public void testNot() {

        Predicate<Integer> notEven = isEven.not();

        assertTrue(notEven.apply(5));
        assertFalse(notEven.apply(4));

    }

}