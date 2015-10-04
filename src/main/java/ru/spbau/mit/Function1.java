package ru.spbau.mit;


public abstract class Function1 <T, R> {

    public abstract R apply(T x);

    public <R2>  Function1 <T, R2> compose(final Function1<? super R, R2> g) {

        return new Function1<T, R2>() {
            @Override
            public R2 apply(T x) {
                return g.apply(Function1.this.apply(x));
            }
        };

    }
}
