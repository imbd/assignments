package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Collections {

    public static <T, R> Iterable<R> map(Function1<? super T, R> f, Iterable<T> l) {

        List<R> res = new ArrayList<>();
        for (T element: l) {
            res.add(f.apply(element));
        }
        return res;

    }

    public static <T> Iterable<T> filter(Predicate<? super T> p, Iterable<T> l) {

        List<T> res = new ArrayList<>();
        for (T element: l) {
            if (p.apply(element)) {
                res.add(element);
            }
        }
        
        return res;

    }

    private static <T> Iterable<T> takeWhileBoolean(Predicate<? super T> p, Iterable<T> l, boolean bool) {

        List<T> res = new ArrayList<>();
        for (T element: l) {
            if (p.apply(element) != bool) {
                break;
            }
            res.add(element);
        }
        return res;

    }

    public static <T> Iterable<T> takeWhile(Predicate<? super T> p, Iterable<T> l) {

        return takeWhileBoolean(p, l, true);

    }

    public static <T> Iterable<T> takeUnless(Predicate<? super T> p, Iterable<T> l) {

        return takeWhileBoolean(p, l, false);

    }

    public static <T, R> R foldl(Function2<? super R, ? super T, ? extends R> f, R start, Iterable<T> l) {

        for(T element: l) {
            start = f.apply(start, element);
        }

        return start;

    }

    public static <T, R> R foldr(Function2<? super T, ? super R, ? extends R> f, R start, Iterable<T> l) {

        List<T> tmp = new ArrayList<>();
        for(T element: l) {
            tmp.add(element);
        }

        for(int i = tmp.size() - 1; i >= 0; i--) {
            start = f.apply(tmp.get(i), start);
        }

        return start;

    }

}