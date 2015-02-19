package nl.tudelft.distributed.birmanschiperstephenson;

import java.io.Serializable;

public class VectorClock implements Serializable{
	private static final long serialVersionUID = -8960683549670043809L;
	private int[] vector;
	
	public VectorClock(int size) {
		this(new int[size]);
	}
	
	private VectorClock(int[] vector){
		this.vector = vector;
	}
	
	public synchronized void increment(int i){
		vector[i]++;
	}
	
	public synchronized void decrement(int i){
		vector[i]--;
	}
	
	public VectorClock copy(){
		int[] copy = new int[vector.length];
		System.arraycopy(this.vector, 0, copy, 0, vector.length);
		return new VectorClock(copy);
	}
	
	public VectorClock immutableIncrement(int i){
		VectorClock copy = copy();
		copy.increment(i);
		return copy;
	}
	
	public VectorClock immutableDecrement(int i){
		VectorClock copy = copy();
		copy.decrement(i);
		return copy;
	}

	public synchronized boolean greaterThan(VectorClock other){
		synchronized(other.vector){
			if(vector.length != other.vector.length){
				throw new RuntimeException("Unable to compare vectors of different size");
			}
			
			for(int i = 0 ; i < vector.length ; i++){
				if(vector[i] < other.vector[i]){
					return false;
				}
			}
		}
		return true;
	}
	
	public synchronized boolean greaterThanOrEquals(VectorClock other){
		return greaterThan(other) || equals(other);
	}
	
	public synchronized boolean smallerThan(VectorClock other){
		synchronized(other.vector){
			if(vector.length != other.vector.length){
				throw new RuntimeException("Unable to compare vectors of different size");
			}
			
			for(int i = 0 ; i < vector.length ; i++){
				if(vector[i] > other.vector[i]){
					return false;
				}
			}
		}
		return true;
	}
	
	public synchronized boolean smallerThanOrEquals(VectorClock other){
		return smallerThan(other) || equals(other);
	}
	
	public synchronized boolean equals(VectorClock other){
		synchronized(other.vector){
			if(vector.length != other.vector.length){
				throw new RuntimeException("Unable to compare vectors of different size");
			}
			
			for(int i = 0 ; i < vector.length ; i++){
				if(vector[i] != other.vector[i]){
					return false;
				}
			}
		}
		return true;
	}
}
