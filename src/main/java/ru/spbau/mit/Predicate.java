package ru.spbau.mit;


public abstract class Predicate <T> extends Function1<T, Boolean> {

    public abstract Boolean apply(T x);

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {

        @Override
        public Boolean apply(Object x) {
            return true;
        }

    };

    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {

        @Override
        public Boolean apply(Object x) {
            return false;
        }

    };

    public Predicate<T> or(final Predicate<? super T> pr) {

        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) || pr.apply(x);
            }
        };

    }

    public Predicate<T> and(final Predicate<? super T> pr) {

        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) && pr.apply(x);
            }
        };

    }

    public Predicate<T> not() {

        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return !Predicate.this.apply(x);
            }
        };

    }

}
