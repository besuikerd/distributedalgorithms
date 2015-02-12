package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointBuffer implements IEndpointBuffer{
	private List<Tuple3<Object, Integer, int[]>> buffer;
	private int[] vectorClock;
	private IEndpoint endpoint;
	
	public DefaultEndpointBuffer(int n, IEndpoint endpoint) {
		buffer = new LinkedList<Tuple3<Object,Integer, int[]>>();
		this.vectorClock = new int[n];
		this.endpoint = endpoint;
	}
	
	@Override
	public void receive(Object message, int sender, int[] vector) throws RemoteException {
		if(passesCondition(sender, vector)){
			endpoint.deliver(message);
			int foundAmount;
			do{
				foundAmount = 0;
				for(int i = 0 ; i < buffer.size() ; i++){
					Tuple3<Object, Integer, int[]> entry = buffer.get(i);
					if(passesCondition(entry._2, entry._3)){
						endpoint.deliver(message);
						buffer.remove(i);
						foundAmount++;
						i--;
					}
				}
			} while(foundAmount != 0);
		} else{
			buffer.add(Tuple3.create(message, sender, vector));
		}
	}
	
	private boolean passesCondition(int sender, int[] vector){
		int[] localClock = vectorClock.clone();
		localClock[sender] += 1;
		for(int i = 0 ; i < localClock.length && i < vector.length ; i++){
			if(localClock[sender] < vector[i]){
				return false;
			}
		}
		return true;
	}
}
