package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public abstract class AbstractEndpoint implements IEndpoint{
	
	protected int nodeId;
	protected String[] remotes;
	protected int[] vectorClock;
	
	
	
	public AbstractEndpoint(int nodeId, String[] remotes) {
		this.nodeId = nodeId;
		this.remotes = remotes;
		this.vectorClock = new int[remotes.length + 1];
	}

	@Override
	public void broadcast(Object message) {
        vectorClock[nodeId]++;
        int i = 0;
        for(String remote : remotes){
            // Don't broadcast to self
            if (i++ == nodeId) {
                continue;
            }
            try {
				Object o = Naming.lookup(remote);
				if(o instanceof IEndpointBuffer){
					((IEndpointBuffer) o).receive(message, nodeId, vectorClock);
				}
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
	}
	
	@Override
	public int[] vectorClock() {
		return vectorClock;
	}
}
