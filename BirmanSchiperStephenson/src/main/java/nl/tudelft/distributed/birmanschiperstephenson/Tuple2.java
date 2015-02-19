package nl.tudelft.distributed.birmanschiperstephenson;

import java.io.Serializable;

public class Tuple2<A,B> implements Serializable{
	private static final long serialVersionUID = 3299617204307238780L;
	
	public final A _1;
	public final B _2;
	
	public Tuple2(A _1, B _2) {
		this._1 = _1;
		this._2 = _2;
	}
	
	public static <A, B> Tuple2<A,B> create(A _1, B _2){
		return new Tuple2<A,B>(_1, _2);
	}
	
	@Override
	public String toString() {
		return String.format("(%s, %s)", _1, _2);
	}
}
