package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class DefaultEndpointBuffer extends UnicastRemoteObject implements IEndpointBuffer {

    private static final long serialVersionUID = -7906377175640753915L;
    private final List<Message> buffer;
    private IEndpoint endpoint;

    public DefaultEndpointBuffer(IEndpoint endpoint) throws RemoteException {
        buffer = new LinkedList<>();
        this.endpoint = endpoint;
    }

    @Override
    public synchronized void receive(Message message) throws RemoteException {
        buffer.add(message);
        if (passesCondition(message)) {
            int foundAmount;
            do {
                foundAmount = 0;
                int messageCount = buffer.size();
                for (int i = 0; i < messageCount; i++) {
                    Message entry = buffer.get(i);
                    if (passesCondition(entry)) {
                        endpoint.deliver(message);
                        endpoint.getClock().increment(message.getSender());
                        Object a = buffer.remove(i);
                        System.out.println(endpoint.getNodeId() + " " + a.toString());
                        messageCount--;
                        i--;
                        foundAmount++;
                    }
                }
            } while (foundAmount != 0);
        }
    }

    private boolean passesCondition(Message message) {
        int sender = message.getSender();
        return endpoint.getClock().immutableIncrement(sender).greaterThanOrEquals(message.getClock());
    }
}
