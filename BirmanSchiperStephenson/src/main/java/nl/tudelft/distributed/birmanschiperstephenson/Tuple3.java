package nl.tudelft.distributed.birmanschiperstephenson;

import java.io.Serializable;

public class Tuple3<A, B, C> implements Serializable {
    private static final long serialVersionUID = 7053801908588383863L;
    public final A _1;
    public final B _2;
    public final C _3;

    public Tuple3(A _1, B _2, C _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public static <A, B, C> Tuple3<A, B, C> create(A _1, B _2, C _3) {
        return new Tuple3<>(_1, _2, _3);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", _1, _2, _3);
    }
}