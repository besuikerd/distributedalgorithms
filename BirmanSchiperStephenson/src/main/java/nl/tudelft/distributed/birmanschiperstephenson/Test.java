package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Test {
	public static void main(String[] args){
		try {
			if(System.getSecurityManager() == null){
                System.setSecurityManager(new SecurityManager());
            }
			Registry registry = LocateRegistry.createRegistry(1337);
			
			Registry remoteRegistry = LocateRegistry.getRegistry("localhost", 1337);
			
			registry.rebind(DefaultEndpointBuffer.class.getName(), new DefaultEndpointBuffer(10, new RandomDelaySenderEndpoint(0,0)));
			IEndpointBuffer endpointBuffer = (IEndpointBuffer) remoteRegistry.lookup(DefaultEndpointBuffer.class.getName());
			
			endpointBuffer.receive("Blabalbal", 0, new int[0]);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
		}
	}
}
