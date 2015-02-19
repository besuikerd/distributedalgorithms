package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public abstract class AbstractEndpoint implements IEndpoint {

    protected int nodeId;
    protected String[] remotes;
    protected VectorClock vectorClock;

    public AbstractEndpoint(int nodeId, String[] remotes) {
        this.nodeId = nodeId;
        this.remotes = remotes;
        this.vectorClock = new VectorClock(remotes.length);
    }

    @Override
    public void broadcast(Object message) {
        vectorClock.increment(nodeId);
        int i = 0;
        for (String remote : remotes) {
            // Don't broadcast to self
            if (i++ == nodeId) {
                continue;
            }
            try {
                Object o = Naming.lookup(remote);
                if (o instanceof IEndpointBuffer) {
                    Message m = new Message(nodeId, i, vectorClock, message);
                    ((IEndpointBuffer) o).receive(m);
                }
            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public VectorClock getClock() {
        return vectorClock;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }
}
