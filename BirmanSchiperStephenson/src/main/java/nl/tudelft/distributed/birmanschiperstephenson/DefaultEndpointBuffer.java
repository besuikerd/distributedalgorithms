package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointBuffer extends UnicastRemoteObject implements IEndpointBuffer{
	
	private static final long serialVersionUID = -7906377175640753915L;
	private List<Tuple3<Object, Integer, int[]>> buffer;
	private IEndpoint endpoint;
	
	public DefaultEndpointBuffer(IEndpoint endpoint) throws RemoteException {
		buffer = new LinkedList<Tuple3<Object,Integer, int[]>>();
		this.endpoint = endpoint;
	}
	
	@Override
	public synchronized void receive(Object message, int sender, int[] vector) throws RemoteException {
		if(passesCondition(sender, vector)){
            synchronized (buffer) {
                buffer.add(Tuple3.create(message, sender, vector));
            }
            int foundAmount;
			do{
				foundAmount = 0;
				for(int i = 0 ; i < buffer.size() ; i++){
					Tuple3<Object, Integer, int[]> entry = buffer.get(i);
					if(passesCondition(entry._2, entry._3)){
						endpoint.deliver(message);
                        endpoint.vectorClock()[sender]++;
                        buffer.remove(i);
                        
                        foundAmount++;
                        //i--;
                    }
				}
			} while(foundAmount != 0);
		} else{
			buffer.add(Tuple3.create(message, sender, vector));
		}
	}

    private boolean passesCondition(int sender, int[] remoteVector) {
        int[] ourVector = endpoint.vectorClock().clone();
        ourVector[sender]++;
        for (int i = 0; i < ourVector.length && i < remoteVector.length; i++) {
            if (ourVector[i] < remoteVector[i]) {
                return false;
            }
		}
		return true;
	}
}
