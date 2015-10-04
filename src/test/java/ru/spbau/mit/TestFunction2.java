package  ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFunction2 {

    private static final Function2<String, Integer, Boolean> mySize = new Function2<String, Integer, Boolean>() {

        @Override
        public Boolean apply(String str, Integer len) {
            return str.length() == len;
        }

    };

    @Test
    public void testBind1() {

        Function1<Integer, Boolean> myBind1 = mySize.bind1("abcde");
        assertTrue(myBind1.apply(5));
        assertFalse(myBind1.apply(4));

    }

    @Test
    public void testBind2() {

        Function1<String, Boolean> myBind2 = mySize.bind2(3);
        assertTrue(myBind2.apply("Abc"));
        assertFalse(myBind2.apply("xy"));

    }

    @Test
    public void testCurry() {

        Function1<String, Function1<Integer, Boolean>> myCurry = mySize.curry();
        assertTrue(myCurry.apply("Abc").apply(3));
        assertFalse(myCurry.apply("xy").apply(3));

    }

}