package ru.spbau.mit;


public abstract class Function2 <T1, T2, R> {

    public abstract R apply(T1 x, T2 y);

    public <R2> Function2 <T1, T2, R2> compose(final Function1<? super R, R2> g) {

        return new Function2<T1, T2, R2>() {
            @Override
            public R2 apply(T1 x,T2 y) {
                return g.apply(Function2.this.apply(x,y));
            }
        };

    }

    public Function1<T2, R> bind1(final T1 x) {

        return new Function1<T2, R>() {
            @Override
            public R apply(T2 y) {
                return Function2.this.apply(x, y);
            }
        };

    }

    public Function1<T1, R> bind2(final T2 y) {

        return new Function1<T1, R>() {
            @Override
            public R apply(T1 x) {
                return Function2.this.apply(x,y);
            }
        };

    }

    public Function1 <T1, Function1<T2, R>> curry() {

        return  new Function1<T1, Function1<T2, R>>() {
            @Override
            public Function1<T2, R> apply(T1 x) {
                return bind1(x);
            }
        };

    }
}
