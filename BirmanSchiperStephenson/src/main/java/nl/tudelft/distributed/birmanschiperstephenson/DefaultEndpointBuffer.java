package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointBuffer extends UnicastRemoteObject implements IEndpointBuffer{
	
	private static final long serialVersionUID = -7906377175640753915L;
    private List<Message> buffer;
    private IEndpoint endpoint;
	
	public DefaultEndpointBuffer(IEndpoint endpoint) throws RemoteException {
        buffer = new LinkedList<>();
        this.endpoint = endpoint;
	}
	
	@Override
    public synchronized void receive(Message message) throws RemoteException {
        if (passesCondition(message)) {
            buffer.add(message);
            int foundAmount;
			do{
				foundAmount = 0;
				for(int i = 0 ; i < buffer.size() ; i++){
                    Message entry = buffer.get(i);
                    if (passesCondition(entry)) {
                        endpoint.deliver(message);
                        endpoint.getClock().increment(message.getSender());
                        buffer.remove(i);
                        foundAmount++;
                    }
				}
			} while(foundAmount != 0);
		} else{
            buffer.add(message);
        }
	}

    private boolean passesCondition(Message message) {
        int sender = message.getSender();
        return endpoint.getClock().immutableIncrement(sender).greaterThanOrEquals(message.getClock());
    }
}
